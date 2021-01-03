package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.ResponseMessage;
import bgu.spl.net.srv.Course;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.User;

import java.util.*;
import java.util.function.Function;

import static java.lang.Integer.parseInt;

public class BGRSProtocol implements MessagingProtocol<Message> {
    // Private fields:
    final HashMap<Integer, Function<List, Message<String>>> messageHandlers; // messageHandlers<opcode, function>
    Database database;
    String clientId;
    boolean shouldTerminate;

    private void addMessageHandler(int opcode, Function<List, Message<String>> function) {
        this.messageHandlers.put(opcode, function);
    }

    private Function<List, Message<String>> getHandler(int opcode) {
        // TODO: TEST
        System.out.println("getHandler("+opcode+")");
        // TODO: TEST

        return this.messageHandlers.get(opcode);
    }

    private Message<String> createUser(int msg_opcode, String username, String password, boolean isAdmin) {
        try {
            if (!Objects.isNull(database.Clients().get(this.clientId).getUser()))
                throw new IllegalStateException("Client cannot register a new user while logged-in.");

            database.createUser(username, password, isAdmin);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return error(msg_opcode);
        }
        return ack(msg_opcode);
    }

    private boolean isAdmin() {
        try {
            return this.database.Clients().get(this.clientId).getUser().isAdmin();
        }
        catch (NullPointerException e) {
            System.out.println("Client is not logged-in.");
            return false;
        }
    }

    private void logout() {
        // TODO: TEST
        System.out.println("logout(): client.getUser() = " + database.Clients().get(this.clientId).getUser());
        // TODO: TEST

        this.database.logoutUser(this.clientId);
    }

    public BGRSProtocol() {
        // Create Database instance:
        this.database = Database.getInstance();
        // Assign a new client object and ID for this client:
        this.clientId = this.database.Clients().assign().getId();
        // Create message handlers and add them to the hashmap:
        this.messageHandlers = new HashMap<>();
        this.addMessageHandler(
                1, // "ADMINREG"
                (words)-> createUser(1, words.get(0).toString(), words.get(1).toString(), true)
        );
        this.addMessageHandler(
                2, // "STUDENTREG"
                (words)-> createUser(2, words.get(0).toString(), words.get(1).toString(),false)
        );
        this.addMessageHandler(
                3, // "LOGIN"
                (words)->{
                    try {
                        // userLogin handles whether the user is already logged-in,
                        // setUser handles whether this client already logged-in with a different user.
                        database.userLogin(this.clientId, words.get(0).toString(), words.get(1).toString());

                        // TODO: TEST
                        System.out.println("LOGIN: client.getUser() = " + database.Clients().get(this.clientId).getUser());
                        // TODO: TEST

                        return ack(3);
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                        return error(3);
                    }
                }
        );
        this.addMessageHandler(
                4, // "LOGOUT"
                (words)->{
                    try {
                        // Logout the user associated to this clientId
                        logout();
                        this.shouldTerminate = true; // Close the client's socket.
                        return ack(4);
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                        return error(4);
                    }
                }
        );
        this.addMessageHandler(
                5, // "COURSEREG"
                (words)->{
                    try {
                        int courseNum = parseInt(words.get(0).toString());
                        System.out.println("COURSEREG: " + courseNum);
                        Course course = database.Courses().getCourse(courseNum);
                        if (Objects.isNull(course))
                            throw new IllegalArgumentException("Course " + courseNum + " does not exist.");

                        // TODO
                        System.out.println("course = " + course.getCourseNumber() + ", " + course.getName());
                        // TODO

                        database.Courses().register(course, database.Clients().get(this.clientId).getUser());
                        return ack(5);
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                        return error(5);
                    }
                }
        );
        this.addMessageHandler(
                6, // "KDAMCHECK"
                (words)->{
                    int courseNum = parseInt(words.get(0).toString());
                    try {
                        Course course = database.Courses().getCourse(courseNum);
                        return ack(6, course.getKdam().toString().replace(" ", "") + "\0");
                    }
                    catch (Exception e) {
                        return error(6);
                    }
                }
        );
        this.addMessageHandler(
                7, // "COURSESTAT" // Admin only!
                (words)->{
                    // TODO: TEST
                    System.out.println("COURSESTAT");
                    // TODO: TEST

                    int courseNum = parseInt(words.get(0).toString());
                    try {
                        if (!this.isAdmin())
                            throw new IllegalArgumentException("Admin privileges required.");

                        Course course = database.Courses().getCourse(courseNum);
                        String courseName = course.getName();
                        int freeSeats = course.getAvailableSeats();
                        int maxSeats = course.getMaxStudents();

                        List<User> students = course.getStudents();
                        List<String> studentNames = new ArrayList<>();
                        if (!Objects.isNull(students)) {
                            for (User student : students)
                                studentNames.add(student.getUsername());
                        }
                        Collections.sort(studentNames); // Sort them alphabetically // TODO: make sure we print it correctly (spaces???)

                        return ack(
                                7,
                                "Course: (" + courseNum + ") " + courseName + "\0"
                                        + "Seats Available: " + freeSeats + "/" + maxSeats + "\0"
                                        + "Students Registered: " + studentNames.toString().replace(" ", "") + "\0"
                        );
                    }
                    catch (Exception e) {
                        System.out.println("COURSESTAT ERROR: " + e.getMessage());
                        return error(7);
                    }
                }
        );
        this.addMessageHandler(
                8, // "STUDENTSTAT" // Admin Only!
                (words)->{
                    // TODO: TEST
                    System.out.println("COURSESTAT");
                    // TODO: TEST

                    try {
                        String username = (String)words.get(0);
                        if (!this.isAdmin())
                            throw new IllegalArgumentException("Admin privileges required.");

                        User student = database.getUser(username);
                        if (student.isAdmin())
                            throw new IllegalArgumentException("Provided user is admin."); // TODO: is this needed?

                        List<Course> courses = student.getCourses();
                        List<Integer> courseNames = new ArrayList<>();
                        if (!Objects.isNull(courses)) {
                            for (Course course : courses)
                                courseNames.add(course.getCourseNumber());
                        }

                        return ack(8,
                                "Student: " + student.getUsername() + "\0"
                                        + "Courses: " + courseNames.toString().replace(" ", "") + "\0"
                        );
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                        return error(8);
                    }
                }
        );
        this.addMessageHandler(
                9, // "ISREGISTERED
                (words)->{
                    int courseNum = (int)words.get(0);
                    try {
                        Course course = database.Courses().getCourse(courseNum);
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
                    int courseNum = (int)words.get(0);
                    Course course = database.Courses().getCourse(courseNum);
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

                    return ack(11, courseNumbers.toString().replace(" ", "") + "\0");
                }
        );
    }

    private ResponseMessage respond(int opcode, int msg_opcode, String response) {
        ResponseMessage message = null;
        if (response.length() > 0)
            message = new OneStringResponseMessage(opcode, msg_opcode, response);
        else
            message = new NoParameterResponseMessage(opcode, msg_opcode);

        return message;
    }

    private ResponseMessage ack(int msg_opcode) {
        return ack(msg_opcode, "");
    }

    private ResponseMessage ack(int msg_opcode, String response) { // ACK OPCODE = 12
        return respond(12, msg_opcode, response);
    }

    private ResponseMessage error(int msg_opcode) { // ERROR OPCODE = 13
        return respond(13, msg_opcode, "");
    }

    public Message process(Message msg) {
        // TODO
        System.out.println("PROTOCOL: opcode received: " + msg.getOpcode());
        System.out.println("PROTOCOL: words received: " + msg.getWords().toString());
        // TODO

        int opcode = msg.getOpcode();
        List words = msg.getWords();

        Function<List, Message<String>> func = this.getHandler(opcode); // Try getting the command by its opcode

        if (Objects.isNull(func))
            return error(opcode);

        return func.apply(words);
    }

    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }
}
