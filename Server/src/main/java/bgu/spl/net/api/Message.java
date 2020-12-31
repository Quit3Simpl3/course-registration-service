package bgu.spl.net.api;

import java.util.List;

public interface Message<T> {
    /**
     * Returns the message's words.
     * @return The list of words in the message.
     */
    public default List<T> getWords() { return null; };

    /**
     * @return The response object.
     */
    public default T getResponse() { return null; };

    /**
     * Returns the message's opcode.
     * @return The message's opcode.
     */
    public int getOpcode();

    /**
     * Return's the message's opcode.
     * @return The original message's opcode.
     */
    public default int getMessageOpcode() {
        return this.getOpcode();
    }
}
