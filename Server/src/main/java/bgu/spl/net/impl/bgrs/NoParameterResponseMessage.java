package bgu.spl.net.impl.bgrs;

import bgu.spl.net.api.Message;

import java.util.List;

public class NoParameterResponseMessage implements Message<String> {
    private int opcode;
    private int messageOpcode;

    public NoParameterResponseMessage(int opcode, int messageOpcode) {
        this.opcode = opcode;
        this.messageOpcode = messageOpcode;
    }

    @Override
    public List<String> getWords() {
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
