package bgu.spl.net.impl.bgrs;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.ResponseMessage;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Message> {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    private byte[] opcode_bytes = new byte[2];
    private short opcode = 0;

    private int zeros_counter = 0;

    private byte[] firstWord = new byte[1 << 10];
    private int first_len = 0;
    private byte[] secondWord = new byte[1 << 10];
    private int second_len = 0;

    private short byteToShort(byte[] byteArr) {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    private Message<String> decodeTwoStringMessage(byte nextByte) {
        // TODO: TEST
        System.out.println("decodeTwoStringMessage");
        // TODO: TEST

        if (nextByte == '\0') {
            this.zeros_counter--;

            // TODO: TEST
            System.out.println("this.zeros_counter = " + this.zeros_counter);
            // TODO: TEST
        }
        if (this.zeros_counter == 2) { // First word
            if (this.first_len >= firstWord.length) {
                firstWord = Arrays.copyOf(firstWord, this.first_len * 2);
            }
            this.firstWord[this.first_len++] = nextByte;
        }
        else if (this.zeros_counter == 1) { // Second word
            if (this.second_len >= secondWord.length) {
                secondWord = Arrays.copyOf(secondWord, this.second_len * 2);
            }
            this.secondWord[this.second_len++] = nextByte;
        }
        else if (this.zeros_counter <= 0) {
            // TODO: TEST
            System.out.println("this.zeros_counter <= 0");
            // TODO: TEST

            this.first_len = 0;
            this.second_len = 0;
            return new TwoStringsMessage(
                    this.opcode,
                    new String(this.firstWord, 0, this.first_len, StandardCharsets.UTF_8),
                    new String(this.secondWord, 0, this.second_len, StandardCharsets.UTF_8)
            );
        }
        return null;
    }

    private Message<String> decodeOneStringMessage(byte nextByte) {
        // TODO: TEST
        System.out.println("decodeOneStringMessage");
        // TODO: TEST

        if (nextByte == '\0') {
            this.first_len = 0;
            return new OneStringMessage(
                    this.opcode,
                    new String(this.firstWord, 0, this.first_len, StandardCharsets.UTF_8)
            );
        }
        if (this.first_len >= firstWord.length) {
            firstWord = Arrays.copyOf(firstWord, this.first_len * 2);
        }
        this.firstWord[this.first_len++] = nextByte;
        return null;
    }

    private Message<Integer> decodeOneIntegerMessage(byte nextByte) {
        // TODO: TEST
        System.out.println("decodeOneIntegerMessage");
        // TODO: TEST

        if (len == 2) {
            this.len = 0;
            return new OneIntegerMessage(this.opcode, (int) byteToShort(this.bytes));
        }
        bytes[len++] = nextByte;
        return null;
    }

    private Message decodeNoParameterMessage(byte nextByte) {
        // TODO: TEST
        System.out.println("decodeNoParameterMessage");
        // TODO: TEST

        if (len == 2) {
            this.len = 0;
            return new OneIntegerMessage(this.opcode, (int) byteToShort(this.bytes));
        }
        bytes[len++] = nextByte;
        return null;
    }

    @Override
    public Message decodeNextByte(byte nextByte) {
        // TODO: TEST
        System.out.println("decodeNextByte(" + nextByte + ")");
        // TODO: TEST

        if (len == 0) {
            this.len++;
        }
        else if (this.len == 1) { // First two bytes represent the opcode
            this.len++;

            opcode_bytes[1] = nextByte;
            this.opcode = byteToShort(opcode_bytes); // Set opcode

            // TODO: TEST
            System.out.println("opcode = "+opcode);
            // TODO: TEST

            if (opcode == 1 || opcode == 2)
                this.zeros_counter = 2; // Two '\0's for a two-string message
            else if (opcode == 8) // TODO: Not necessary. It just finishes after the first '\0'
                this.zeros_counter = 1;
        }
        else {
            if (opcode == 1 /*ADMINREG*/ || opcode == 2 /*STUDENTREG*/ || opcode == 3 /*LOGIN*/) { // Two string messages
                return decodeTwoStringMessage(nextByte);
            }
            else if (opcode == 5 /*COURSEREG*/ || opcode == 6 /*KDAMCHECK*/ || opcode == 7 /*COURSESTAT*/ || opcode == 9 /*ISREGISTERED*/ || opcode == 10 /*UNREGISTER*/) {
                return decodeOneIntegerMessage(nextByte);
            }
            else if (opcode == 8 /*STUDENTSTAT*/) {
                return decodeOneStringMessage(nextByte);
            }
            else if (opcode == 4 /*LOGOUT*/ || opcode == 11 /*MYCOURSES*/) {
                return decodeNoParameterMessage(nextByte);
            }
            else {
                throw new IllegalArgumentException("Unknown opcode provided.");
            }
        }
        return null;
    }

    private byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    @Override
    public byte[] encode(Message message) {
        // Expect a ResponseMessage instance:
        ResponseMessage msg = (ResponseMessage)message;

        String text;
        try {
            text = (String) msg.getWords().get(0);
        }
        catch (NullPointerException e) {
            text = "";
        }

        byte[] response = new byte[5 + text.length()];

        byte[] opcode = shortToBytes((short)msg.getOpcode());
        byte[] msg_opcode = shortToBytes((short)msg.getMessageOpcode());

        response[0] = opcode[0];
        response[1] = opcode[1];
        response[2] = msg_opcode[0];
        response[3] = msg_opcode[1];

        byte[] textBytes = text.getBytes();

        for (int i = 0; i < textBytes.length; i++) {
            response[i + 4] = textBytes[i];
        }
        response[3 + textBytes.length] = '\0'; // Mark the end of the string

        return response;
    }
}


















