package bgu.spl.net.api;

public interface ResponseMessage extends Message<String> {
    public int getMessageOpcode();
}
