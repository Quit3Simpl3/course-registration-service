package bgu.spl.net.srv;

public class MessageHandler implements Runnable {
    // Private fields:
    int opcode;

    public MessageHandler(int opcode) {
        this.opcode = opcode;
    }

    public int getOpcode() {
        return this.opcode;
    }

    @Override
    public void run() {

    }
}
