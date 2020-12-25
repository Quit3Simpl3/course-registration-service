package bgu.spl.net.impl.bgrs;

import bgu.spl.net.api.MessageEncoderDecoder;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<String> {
    //TODO: IMPLEMENT THIS

    @Override
    public String decodeNextByte(byte nextByte) {
        return null;
    }

    @Override
    public byte[] encode(String message) {
        return new byte[0];
    }
}
