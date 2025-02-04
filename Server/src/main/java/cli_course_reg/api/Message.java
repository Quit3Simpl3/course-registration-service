package cli_course_reg.api;

import java.util.List;

public interface Message<T> {
    /**
     * Returns the message's words.
     * @return The list of words in the message.
     */
    public List<T> getWords();

    /**
     * @return The response object.
     */
    public T getResponse();

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
