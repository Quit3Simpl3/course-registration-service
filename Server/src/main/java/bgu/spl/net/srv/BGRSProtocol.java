package bgu.spl.net.srv;

import bgu.spl.net.api.MessagingProtocol;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

import static java.lang.Integer.parseInt;

public class BGRSProtocol implements MessagingProtocol<String> {
    // Private fields:
    HashMap<String, Function<String[], String>> messageHandlers; // messageHandlers<command, function>
    HashMap<Integer, String> opcodeToCommand;
    Database database;

    private void addMessageHandler(int opcode, String command, Function<String[], String> function) {
        this.messageHandlers.put(command, function);
        this.opcodeToCommand.put(opcode, command);
    }

    private Function<String[], String> getHandler(int opcode) {
        String command = this.opcodeToCommand.get(opcode);
        if (Objects.isNull(command))
            throw new IllegalArgumentException("This command does not exist.");
        return this.getHandler(command);
    }

    private Function<String[], String> getHandler(String command) {
        Function<String[], String> func = this.messageHandlers.get(command);
        if (Objects.isNull(func))
            throw new IllegalArgumentException("This command does not exist.");
        return func;
    }

    /**
     * Used to return ACK message in the message handler functions.
     * @return
     */
    private String ack() {
        return "ACK";
    }

    public BGRSProtocol() {
        // Create Database instance:
        this.database = Database.getInstance();
        // Create message handlers and add them to the hashmap:
        this.addMessageHandler(
                1,
                "ADMINREG",
                (lines)->{
                    // lines[0] = command name, lines[1] = username, lines[2] = password
                    String username = lines[1];
                    String password = lines[2];
                    try {
                        database.createUser(username, password, true);
                    }
                    catch (IllegalArgumentException e) {
                        return e.getMessage();
                    }
                    return ack();
                }
        );
        this.addMessageHandler(
                2,
                "STUDENTREG",
                (lines)->{
                    // lines[0] = command name, lines[1] = username, lines[2] = password
                    String username = lines[1];
                    String password = lines[2];
                    try {
                        database.createStudent(username, password);
                    }
                    catch (IllegalArgumentException e) {
                        return e.getMessage();
                    }
                    return ack();
                }
        );
        this.addMessageHandler(
                3,
                "LOGIN",
                (lines)->{
                    // lines[0] = command name, lines[1] = username, lines[2] = password
                    String username = lines[1];
                    String password = lines[2];
                    try {
                        database.userLogin(username, password);
                    }
                    catch (IllegalArgumentException e) {
                        return e.getMessage();
                    }
                    return ack();
                }
        );
        this.addMessageHandler(
                4,
                "LOGOUT",
                (lines)->{
                    // TODO: how does the server know which client to logout?
                    return "";
                }
        );
        this.addMessageHandler(
                5,
                "COURSEREG",
                (lines)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                6,
                "KDAMCHECK",
                (lines)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                7,
                "COURSESTAT",
                (lines)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                8,
                "STUDENTSTAT",
                (lines)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                9,
                "ISREGISTERED",
                (lines)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                10,
                "UNREGISTER",
                (lines)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                11,
                "MYCOURSES",
                (lines)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                12,
                "ACK",
                (lines)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                13,
                "ERR",
                (lines)->{
                    // TODO
                    return "";
                }
        );
    }

    @Override
    public String process(String msg) {
        String[] lines = msg.split(" ");
        String command = lines[0]; // The first word in the message is the command to execute
        Function<String[], String> func = null;
        try {
            func = this.getHandler(parseInt(command)); // Try getting the command by its opcode
        }
        catch (IllegalArgumentException e) {
            func = this.getHandler(command); // Try getting the command by its name
        }

        if (Objects.isNull(func))
            return "ERROR: Illegal command provided.";

        return func.apply(lines);
    }

    @Override
    public boolean shouldTerminate() {
        // TODO: Implement this
        return false;
    }
}
