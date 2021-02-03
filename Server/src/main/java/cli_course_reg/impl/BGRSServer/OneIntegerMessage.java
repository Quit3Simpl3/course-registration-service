package cli_course_reg.impl.BGRSServer;

import cli_course_reg.api.Message;

import java.util.ArrayList;
import java.util.List;

public class OneIntegerMessage implements Message<Integer> {
    private int opcode;
    Integer word;

    public OneIntegerMessage(int opcode, Integer word) {
        this.word = word;
        this.opcode = opcode;
    }

    @Override
    public List<Integer> getWords() {
        List<Integer> wordList = new ArrayList<>(1);
        wordList.add(this.word);
        return wordList;
    }

    @Override
    public Integer getResponse() {
        return null;
    }

    @Override
    public int getOpcode() {
        return this.opcode;
    }
}
