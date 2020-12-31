package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.ResponseMessage;

import java.util.ArrayList;
import java.util.List;

public class OneStringResponseMessage implements ResponseMessage {
    private int opcode;
    private int messageOpcode;
    private String response;

    public OneStringResponseMessage(int opcode, int messageOpcode, String response) {
        this.opcode = opcode;
        this.messageOpcode = messageOpcode;
        this.response = response;
    }

    @Override
    public List<String> getWords() {
        List<String> wordList = new ArrayList<>(1);
        wordList.add(this.response);
        return wordList;
    }

    public int getMessageOpcode() {
        return this.messageOpcode;
    }

    @Override
    public int getOpcode() {
        return this.opcode;
    }
}
