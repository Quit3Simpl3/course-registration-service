package bgu.spl.net.impl.bgrs;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.srv.Client;
import bgu.spl.net.srv.Course;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.User;

import javax.xml.crypto.Data;
import java.util.*;
import java.util.function.Function;

import static java.lang.Integer.parseInt;

public class BGRSProtocol implements MessagingProtocol<String> {
    // Private fields:
    final HashMap<Integer, Function<String[], String>> messageHandlers; // messageHandlers<opcode, function>
    Database database;
    String clientId;

    private void addMessageHandler(int opcode, Function<String[], String> function) {
        this.messageHandlers.put(opcode, function);
    }

    private Function<String[], String> getHandler(int opcode) {
        return this.messageHandlers.get(opcode);
    }

    private String createUser(int msg_opcode, String username, String password, boolean isAdmin) {
        try {
            database.createUser(username, password, isAdmin);
        }
        catch (IllegalArgumentException e) {
            return error(msg_opcode);
        }
        return ack(1);
    }

    private boolean isAdmin() {
        return (this.database.Clients().get(this.clientId).getUser().isAdmin());
    }

    public BGRSProtocol() {
        // Create Database instance:
        this.database = Database.getInstance();
        this.clientId = this.database.Clients().assign().getId();
        // Create message handlers and add them to the hashmap:
        this.messageHandlers = new HashMap<>();
        this.addMessageHandler(
                1, // "ADMINREG"
                (words)->{
                    // words[1] = username, words[2] = password
                    String username = words[1];
                    String password = words[2];

                    return createUser(1, username, password, true);
                }
        );
        this.addMessageHandler(
                2, // "STUDENTREG"
                (words)->{
                    // words[1] = username, words[2] = password
                    String username = words[1];
                    String password = words[2];

                    return createUser(2, username, password, false);
                }
        );
        this.addMessageHandler(
                3, // "LOGIN"
                (words)->{
                    // words[1] = username, words[2] = password
                    String username = words[1];
                    String password = words[2];
                    try {
                        // userLogin handles whether the user is already logged-in,
                        // setUser handles whether this client already logged-in with a different user.
                        database.userLogin(this.clientId, username, password);
                    }
                    catch (Exception e) {
                        return error(3);
                    }
                    return ack(3);
                }
        );
        this.addMessageHandler(
                4, // "LOGOUT"
                (words)->{
                    try {
                        // Logout the user associated to this clientId
                        this.database.logoutUser(this.clientId);
                    }
                    catch (Exception e) {
                        error(4);
                    }
                    return ack(4);
                }
        );
        this.addMessageHandler(
                5, // "COURSEREG"
                (words)->{
                    String courseNum = words[1];
                    try {
                        Course course = database.Courses().getCourse(parseInt(courseNum));
                        database.Courses().register(course, database.Clients().get(this.clientId).getUser());
                    }
                    catch (Exception e) {
                        return error(5);
                    }
                    return ack(5);
                }
        );
        this.addMessageHandler(
                6, // "KDAMCHECK"
                (words)->{
                    String courseNum = words[1];
                    try {
                        Course course = database.Courses().getCourse(parseInt(courseNum));

                        return ack(6, course.getKdam().toString());
                    }
                    catch (Exception e) {
                        return error(6);
                    }
                }
        );
        this.addMessageHandler(
                7, // "COURSESTAT" // Admin only!
                (words)->{
                    String courseNum = words[1];
                    try {
                        if (!this.isAdmin())
                            throw new IllegalArgumentException("Admin privileges required.");

                        Course course = database.Courses().getCourse(parseInt(courseNum));
                        String courseName = course.getName();
                        int freeSeats = course.getAvailableSeats();
                        int maxSeats = course.getMaxStudents();

                        List<User> students = course.getStudents();
                        List<String> studentNames = new ArrayList<>();
                        for (User student : students)
                            studentNames.add(student.getUsername());

                        Collections.sort(studentNames); // Sort them alphabetically

                        return ack(
                                7,
                                courseNum + "\0"
                                        + courseName + "\0"
                                        + freeSeats + "\0"
                                        + maxSeats + "\0"
                                        + studentNames.toString()
                        );
                    }
                    catch (Exception e) {
                        return error(7);
                    }
                }
        );
        this.addMessageHandler(
                8, // "STUDENTSTAT" // Admin Only!
                (words)->{
                    String username = words[1];
                    try {
                        if (!this.isAdmin())
                            throw new IllegalArgumentException("Admin privileges required.");

                        User student = database.getUser(username);
                        if (student.isAdmin())
                            throw new IllegalArgumentException("Provided user is admin.");

                        List<Course> courses = student.getCourses();
                        List<Integer> courseNames = new ArrayList<>();
                        for (Course course : courses)
                            courseNames.add(course.getCourseNumber());

                        return ack(8,student.getUsername() + "\0" + courseNames.toString());
                    }
                    catch (Exception e) {
                        return error(8);
                    }
                }
        );
        this.addMessageHandler(
                9, // "ISREGISTERED
                (words)->{
                    String courseNumber = words[1];
                    try {
                        Course course = database.Courses().getCourse(parseInt(courseNumber));
                        if (database.Courses().isStudentRegistered(
                                database.Clients().get(this.clientId).getUser(),
                                course))
                            return ack(9, "REGISTERED");
                        else
                            return ack(9, "NOT REGISTERED");
                    }
                    catch (Exception e) {
                        return error(9);
                    }
                }
        );
        this.addMessageHandler(
                10, // "UNREGISTER"
                (words)->{
                    String courseNumber = words[1];
                    Course course = database.Courses().getCourse(parseInt(courseNumber));
                    try {
                        User user = database.Clients().get(this.clientId).getUser();
                        if (database.Courses().unregister(course, user))
                            return ack(10);
                        else
                            throw new Exception("Could not unregister the provided user from the course.");
                    }
                    catch (Exception e) {
                        return error(10);
                    }
                }
        );
        this.addMessageHandler(
                11, // "MYCOURSES"
                (words)->{
                    List<Course> courses = database.Clients().get(this.clientId).getUser().getCourses();
                    List<Integer> courseNumbers = new ArrayList<>();
                    for (Course course : courses)
                        courseNumbers.add(course.getCourseNumber());

                    return ack(11, courseNumbers.toString());
                }
        );
    }

    private void shortToByte(char[] c, short opcode) {
        // TODO
    }

    private String respond(int opcode, int msg_opcode, String response) {
        char[] ch_opcode = new char[2];
        char[] ch_msg_opcode = new char[2];
        shortToByte(ch_opcode, (short)13);
        shortToByte(ch_msg_opcode, (short)msg_opcode);
        if (response.length() > 0)
            return Arrays.toString(ch_opcode) + "\0" + Arrays.toString(ch_msg_opcode) + "\0" + response;
        return Arrays.toString(ch_opcode) + "\0" + Arrays.toString(ch_msg_opcode);
    }

    private String ack(int msg_opcode) {
        return ack(msg_opcode, "");
    }

    private String ack(int msg_opcode, String response) { // ACK OPCODE = 12
        return respond(12, msg_opcode, response);
    }

    private String error(int msg_opcode) { // ERROR OPCODE = 13
        return respond(13, msg_opcode, "");
    }

    @Override
    public String process(String msg) {
        String opcode = msg.substring(0, 5); // Get the 4 digit opcode
        String[] words = (msg.substring(5)).split("\0");

        Function<String[], String> func = this.getHandler(parseInt(opcode)); // Try getting the command by its opcode

        if (Objects.isNull(func))
            return error(parseInt(opcode));

        return func.apply(words);
    }

    @Override
    public boolean shouldTerminate() {
        // TODO: Implement this
        return false;
    }
}
