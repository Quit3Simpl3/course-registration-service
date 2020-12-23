package bgu.spl.net.srv;

import bgu.spl.net.api.MessagingProtocol;

import java.util.HashMap;
import java.util.function.Function;

public class BGRSProtocol implements MessagingProtocol<String> {
    HashMap<String, Function<String[], String>> messageHandlers; // messageHandlers<command, function>
    HashMap<Integer, String> opcodeToCommand;

    private void addMessageHandler(int opcode, String command, Function<String[], String> function) {
        this.messageHandlers.put(command, function);
        this.opcodeToCommand.put(opcode, command);
    }

    public BGRSProtocol() {
        // Create message handlers and add them to the hashmap:
        this.addMessageHandler(
                1,
                "ADMINREG",
                (lines)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                2,
                "STUDENTREG",
                (lines)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                3,
                "LOGIN",
                (lines)->{
                    // TODO
                    return "";
                }
        );
        this.addMessageHandler(
                4,
                "LOGOUT",
                (lines)->{
                    // TODO
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
        // TODO: Implement this
        return null;
    }

    @Override
    public boolean shouldTerminate() {
        // TODO: Implement this
        return false;
    }
}
