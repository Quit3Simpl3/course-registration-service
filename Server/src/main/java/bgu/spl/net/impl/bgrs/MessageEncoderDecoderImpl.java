package bgu.spl.net.impl.bgrs;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<String> {
    //TODO: IMPLEMENT THIS
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private byte[] opcode = new byte[2];

    /*private byte[] byteToShort(short opcode) {

    }*/

    @Override
    public String decodeNextByte(byte nextByte) {
        // TODO
        System.out.println("Decoding: " + nextByte);
        // TODO

        if (len == 0 || len == 1) {
            if (opcode[1] == 0)
                opcode[1] = nextByte;
        }

        if (nextByte == '\n') {
            String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
            result = opcode[1] + result;
            // TODO
            System.out.println("bytes = " + Arrays.toString(bytes));
            System.out.println("opcode = " + Arrays.toString(opcode));
            System.out.println("result = " + result);
            // TODO

            len = 0;
            return result;
        }

        if (nextByte == '\0')
            nextByte = ' ';

        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;

        return null;
    }

    @Override
    public byte[] encode(String message) {
        return (message + "\0").getBytes(); //uses utf8 by default
    }
}
