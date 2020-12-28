package bgu.spl.net.api;

import java.util.List;

public interface Message<T> {
    /**
     * Returns the message's words.
     * @return
     */
    public List<T> getWords();

    /**
     * Returns the message's opcode.
     * @return
     */
    public int getOpcode();
}
