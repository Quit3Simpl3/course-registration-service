package bgu.spl.net.impl.bgrs;

import bgu.spl.net.api.Message;

import java.util.ArrayList;
import java.util.List;

public class TwoStringsMessage implements Message<String> {
    private String firstWord, secondWord;
    private int opcode;

    public TwoStringsMessage(int opcode, String firstWord, String secondWord) {
        this.firstWord = firstWord;
        this.secondWord = secondWord;
        this.opcode = opcode;
    }

    public void setWords(String firstWord, String secondWord) {
        this.firstWord = firstWord;
        this.secondWord = secondWord;
    }

    @Override
    public List<String> getWords() {
        List<String> wordList = new ArrayList<>(2);
        wordList.add(this.firstWord);
        wordList.add(this.secondWord);
        return wordList;
    }

    @Override
    public int getOpcode() {
        return this.opcode;
    }
}
