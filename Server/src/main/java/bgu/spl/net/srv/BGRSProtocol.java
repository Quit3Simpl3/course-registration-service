package bgu.spl.net.srv;

import bgu.spl.net.api.MessagingProtocol;

import javax.xml.crypto.Data;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

import static java.lang.Integer.parseInt;

public class BGRSProtocol implements MessagingProtocol<String> {
    // Private fields:
    final HashMap<Integer, Function<String[], String>> messageHandlers; // messageHandlers<opcode, function>
    Database database;
    Client client;

    private void addMessageHandler(int opcode, Function<String[], String> function) {
        this.messageHandlers.put(opcode, function);
    }

    private Function<String[], String> getHandler(int opcode) {
        return this.messageHandlers.get(opcode);
    }

    private String createUser(int msg_opcode, String username, String password, boolean isAdmin) {
        try {
            this.client.setUser(database.createUser(username, password, isAdmin));
        }
        catch (IllegalArgumentException e) {
            return error(msg_opcode);
        }
        return ack(1, "");
    }

    public BGRSProtocol() {
        // Create Database instance:
        this.database = Database.getInstance();

        this.client = this.database.clients.assign();
        this.messageHandlers = new HashMap<>();
        // Create message handlers and add them to the hashmap:
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
                        client.setUser(this.database.userLogin(username, password));
                    }
                    catch (IllegalArgumentException e) {
                        return error(3);
                    }
                    return ack(3, "");
                }
        );
        this.addMessageHandler(
                4, // "LOGOUT"
                (words)->{
                    try {
                        client.getUser().logout();
                    }
                    catch (Exception e) {
                        error(4);
                    }
                    return ack(4, "");
                }
        );
        this.addMessageHandler(
                5, // "COURSEREG"
                (words)->{
                    String course_number = words[1];
                    Course course = database.getCourses().getCourse(course_number);
                    database.
                }
        );
        this.addMessageHandler(
                6, // "KDAMCHECK"
                (words)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                7,
                (words)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                8,
                (words)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                9,
                (words)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                10,
                (words)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                11,
                (words)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                12,
                (words)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                13,
                (words)->{
                    // TODO
                    return "";
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

    private String ack(int msg_opcode, String response) { // ACK OPCODE = 12
        return respond(12, msg_opcode, response);
    }

    private String error(int msg_opcode) { // ERROR OPCODE = 13
        return respond(13, msg_opcode, "");
    }

    @Override
    public String process(String msg) {
        String opcode = msg.substring(0, 5); // Get the 4 digit opcode
        String[] lines = (msg.substring(5)).split("\0");

        Function<String[], String> func = this.getHandler(parseInt(opcode)); // Try getting the command by its opcode

        if (Objects.isNull(func))
            return error(parseInt(opcode));

        return func.apply(lines);
    }

    @Override
    public boolean shouldTerminate() {
        // TODO: Implement this
        return false;
    }
}
