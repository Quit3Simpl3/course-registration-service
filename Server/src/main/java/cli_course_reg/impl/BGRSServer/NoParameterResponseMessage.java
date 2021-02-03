package cli_course_reg.impl.BGRSServer;

import cli_course_reg.api.ResponseMessage;

import java.util.List;

public class NoParameterResponseMessage implements ResponseMessage {
    private final int opcode;
    private final int messageOpcode;

    public NoParameterResponseMessage(int opcode, int messageOpcode) {
        this.opcode = opcode;
        this.messageOpcode = messageOpcode;
    }

    @Override
    public List<String> getWords() {
        return null;
    }

    @Override
    public String getResponse() {
        return null;
    }

    public int getMessageOpcode() {
        return this.messageOpcode;
    }

    @Override
    public int getOpcode() {
        return this.opcode;
    }
}
