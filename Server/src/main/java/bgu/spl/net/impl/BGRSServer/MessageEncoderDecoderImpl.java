package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Message<?>> {
    private byte[] bytes = new byte[1 << 10]; // start with 1k
    private int len = 0;

    private short opcode = 0;

    private int zeros_counter = 0;

    private byte[] firstWord = new byte[1 << 10];
    private int first_len = 0;
    private byte[] secondWord = new byte[1 << 10];
    private int second_len = 0;

    private final ArrayList<Function<Byte, Message<?>>> decodeMessageByOpcode;


    public MessageEncoderDecoderImpl() {
        // Setup message decoder functions:
        this.decodeMessageByOpcode = new ArrayList<>(11);
        for (int i = 0; i < 11; i++)
            this.decodeMessageByOpcode.add(null);

        for (int i = 0; i <= 2; i++)
            this.decodeMessageByOpcode.set(i, this::decodeTwoStringMessage);

        for (int i = 4; i <= 9; i++)
            this.decodeMessageByOpcode.set(i, this::decodeOneIntegerMessage);

        this.decodeMessageByOpcode.set(3, this::decodeNoParameterMessage);
        this.decodeMessageByOpcode.set(10, this::decodeNoParameterMessage);
        this.decodeMessageByOpcode.set(7, this::decodeOneStringMessage);
    }

    private Function<Byte, Message<?>> getMessageDecoder(int opcode) {
        try {
            return this.decodeMessageByOpcode.get(opcode - 1);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Unknown opcode provided.");
        }
    }

    private short byteToShort(byte[] byteArr) {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    private void resetEncoderDecoder() {
        this.first_len = 0;
        this.second_len = 0;
        this.len = 0;
    }

    private Message<String> decodeTwoStringMessage(byte nextByte) {
        if (nextByte == '\0') {
            this.zeros_counter--;
            if (this.zeros_counter <= 0) {
                Message<String> message = new TwoStringsMessage(
                        this.opcode,
                        new String(this.firstWord, 0, this.first_len, StandardCharsets.UTF_8),
                        new String(this.secondWord, 0, this.second_len, StandardCharsets.UTF_8)
                );
                resetEncoderDecoder();
                return message;
            }
        }
        else if (this.zeros_counter == 2) // First word
            this.first_len = addAndGetByteArray(this.firstWord, nextByte, this.first_len);

        else if (this.zeros_counter == 1) // Second word
            this.second_len = addAndGetByteArray(this.secondWord, nextByte, this.second_len);

        return null;
    }

    /**
     * Adds the provided element to the provided byte array and returns the next element's location.
     * Updates the array's length as needed.
     * @param byteArray - The byte-array to update.
     * @param element - The element to add to the array.
     * @param location - Where to put the element.
     * @return - The location of the next element.
     */
    private int addAndGetByteArray(byte[] byteArray, byte element, int location) {
        if (location >= byteArray.length) // Handle the array's length
            byteArray = Arrays.copyOf(byteArray, location * 2);

        byteArray[location++] = element;
        return location;
    }

    private Message<String> decodeOneStringMessage(byte nextByte) {
        if (nextByte == '\0') {
            Message<String> message = new OneStringMessage(
                    this.opcode,
                    new String(this.firstWord, 0, this.first_len, StandardCharsets.UTF_8)
            );
            resetEncoderDecoder();
            return message;
        }
        if (this.first_len >= firstWord.length) {
            firstWord = Arrays.copyOf(firstWord, this.first_len * 2);
        }
        this.firstWord[this.first_len++] = nextByte;
        return null;
    }

    private Message<Integer> decodeOneIntegerMessage(byte nextByte) {
        bytes[first_len++] = nextByte;

        if (first_len == 2) {
            resetEncoderDecoder();
            return new OneIntegerMessage(this.opcode, (int) byteToShort(this.bytes));
        }
        return null;
    }

    private Message<Integer> decodeNoParameterMessage(byte nextByte) {
        if (len == 2) {
            resetEncoderDecoder();
            return new OneIntegerMessage(this.opcode, (int) byteToShort(this.bytes));
        }
        bytes[len++] = nextByte;
        return null;
    }

    @Override
    public Message<?> decodeNextByte(byte nextByte) {
        if (len == 0) { // First byte is 0 - ignored.
            this.len++;
        }
        else if (this.len == 1) { // First two bytes represent the opcode
            this.len++;

            opcode = byteToShort(new byte[] {0, nextByte}); // Set opcode

            if (opcode == 4 /*LOGOUT*/ || opcode == 11 /*MYCOURSES*/) // Handle opcode-only messages
                 return decodeNoParameterMessage(nextByte);

            this.zeros_counter = 2; // Two '\0's for a two-string message
        }
        // Start handling opcode+string/int messages starting ONLY at the THIRD run of the decoding function:
        else if (this.len >= 2) {
            return getMessageDecoder(opcode).apply(nextByte);
        }
        return null;
    }

    private byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    @Override
    public byte[] encode(Message<?> message) {
        String reply_str = "";
        if (!Objects.isNull(message.getResponse()))
            reply_str = message.getResponse().toString();

        byte[] opcode = shortToBytes((short)message.getOpcode()); // Must cast to short for the function to work
        byte[] msg_opcode = shortToBytes((short)message.getMessageOpcode());
        byte[] words_bytes = reply_str.getBytes();
        byte[] response = new byte[4 + words_bytes.length]; // 2 - opcode, 2 - msg_opcode, then reply_str

        for (int i = 0; i < words_bytes.length; i++) // Insert the reply string into the response bytes array
            response[i + 4] = words_bytes[i]; // Start from index 4

        // Insert the opcode and the messageOpcode:
        response[0] = opcode[0];
        response[1] = opcode[1];
        response[2] = msg_opcode[0];
        response[3] = msg_opcode[1];

        return response;
    }
}