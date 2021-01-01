package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.Message;

import java.util.ArrayList;
import java.util.List;

public class OneStringMessage implements Message<String> {
    private String word;
    private int opcode;

    public OneStringMessage(int opcode, String word) {
        this.word = word;
        this.opcode = opcode;
    }

    @Override
    public String getResponse() {
        return this.word;
    }

    public List<String> getWords() {
        List<String> words_list = new ArrayList<>(1);
        words_list.add(this.word);
        return words_list;
    }

    public void setWords(String word) {
        this.word = word;
    }

    @Override
    public int getOpcode() {
        return this.opcode;
    }
}
