package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.ResponseMessage;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

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

    private Function<Byte, Message>[] decodeMessageByOpcode;


    public MessageEncoderDecoderImpl() {
        this.decodeMessageByOpcode = new Function[11];

        this.decodeMessageByOpcode[0] = this::decodeTwoStringMessage;
        this.decodeMessageByOpcode[1] = this::decodeTwoStringMessage;
        this.decodeMessageByOpcode[2] = this::decodeTwoStringMessage;

        this.decodeMessageByOpcode[3] = this::decodeNoParameterMessage;
        this.decodeMessageByOpcode[10] = this::decodeNoParameterMessage;

        this.decodeMessageByOpcode[4] = this::decodeOneIntegerMessage;
        this.decodeMessageByOpcode[5] = this::decodeOneIntegerMessage;
        this.decodeMessageByOpcode[6] = this::decodeOneIntegerMessage;
        this.decodeMessageByOpcode[8] = this::decodeOneIntegerMessage;
        this.decodeMessageByOpcode[9] = this::decodeOneIntegerMessage;

        this.decodeMessageByOpcode[7] = this::decodeOneStringMessage;
    }

    private Function<Byte, Message> getMessageDecoder(int opcode) {
        try {
            return this.decodeMessageByOpcode[opcode - 1];
        }
        catch (NoSuchElementException e) {
            // TODO
            System.out.print(e.getMessage());
            // TODO
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
        // TODO: TEST
        System.out.println("decodeTwoStringMessage");
        // TODO: TEST

        if (nextByte == '\0') {
            this.zeros_counter--;

            if (this.zeros_counter <= 0) {
                // TODO: TEST
                System.out.println("this.zeros_counter <= 0");
                // TODO: TEST
                Message message = new TwoStringsMessage(
                        this.opcode,
                        new String(this.firstWord, 0, this.first_len, StandardCharsets.UTF_8),
                        new String(this.secondWord, 0, this.second_len, StandardCharsets.UTF_8)
                );
                resetEncoderDecoder();
                return message;
            }
        }
        else if (this.zeros_counter == 2) { // First word
            /*firstWord = getSizeableArray(firstWord, this.first_len); // Handle the byte-array's length
            this.firstWord[this.first_len++] = nextByte;*/
            this.first_len = addAndGetByteArray(this.firstWord, nextByte, this.first_len); // TODO: MAKE SURE THIS UPDATES THE ARRAY!!!
        }
        else if (this.zeros_counter == 1) { // Second word
            /*secondWord = getSizeableArray(secondWord, this.second_len); // Handle the byte-array's length
            this.secondWord[this.second_len++] = nextByte;*/
            this.second_len = addAndGetByteArray(this.secondWord, nextByte, this.second_len); // TODO: MAKE SURE THIS UPDATES THE ARRAY!!!
        }
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

    /*private byte[] getSizeableArray(byte[] byteArray, int length) {
        if (length >= byteArray.length)
            return Arrays.copyOf(byteArray, length * 2);
        return byteArray;
    }*/

    private Message<String> decodeOneStringMessage(byte nextByte) {
        // TODO: TEST
        System.out.println("decodeOneStringMessage");
        // TODO: TEST

        if (nextByte == '\0') {
            Message message = new OneStringMessage(
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
        // TODO: TEST
        System.out.println("decodeOneIntegerMessage");
        System.out.println("first_len="+first_len);
        // TODO: TEST

        bytes[first_len++] = nextByte;

        if (first_len == 2) {
            resetEncoderDecoder();
            return new OneIntegerMessage(this.opcode, (int) byteToShort(this.bytes));
        }
        return null;
    }

    private Message decodeNoParameterMessage(byte nextByte) {
        // TODO: TEST
        System.out.println("decodeNoParameterMessage");
        // TODO: TEST

        if (len == 2) {
            // TODO: TEST
            System.out.println("return: OneIntegerMessage to the protocol");
            // TODO: TEST

            resetEncoderDecoder();
            Message msg = new OneIntegerMessage(this.opcode, (int) byteToShort(this.bytes));

            // TODO: TEST
            System.out.println("msg.opcode = " + msg.getOpcode());
            // TODO: TEST

            return msg;
        }
        bytes[len++] = nextByte;
        return null;
    }

    @Override
    public Message decodeNextByte(byte nextByte) {
        // TODO: TEST
        System.out.println("decodeNextByte(" + nextByte + ")");
        // TODO: TEST

        if (len == 0) { // First byte is 0 - ignored.
            this.len++;
        }
        else if (this.len == 1) { // First two bytes represent the opcode
            this.len++;

            opcode_bytes[1] = nextByte;
            this.opcode = byteToShort(opcode_bytes); // Set opcode

            // TODO: TEST
            System.out.println("opcode = " + opcode);
            // TODO: TEST

            if (opcode == 4 /*LOGOUT*/ || opcode == 11 /*MYCOURSES*/) { // Handle opcode-only messages
                 return decodeNoParameterMessage(nextByte);
            }

            this.zeros_counter = 2; // Two '\0's for a two-string message
        }
        // Start handling opcode+string/int messages starting ONLY at the THIRD run of the decoding function:
        else if (this.len >= 2) {
            return getMessageDecoder(opcode).apply(nextByte);
//            if (opcode == 1 /*ADMINREG*/ || opcode == 2 /*STUDENTREG*/ || opcode == 3 /*LOGIN*/) { // Two string messages
//                return decodeTwoStringMessage(nextByte);
//            }
//            else if (opcode == 5 /*COURSEREG*/ || opcode == 6 /*KDAMCHECK*/ || opcode == 7 /*COURSESTAT*/ || opcode == 9 /*ISREGISTERED*/ || opcode == 10 /*UNREGISTER*/) {
//                return decodeOneIntegerMessage(nextByte);
//            }
//            else if (opcode == 8 /*STUDENTSTAT*/) {
//                return decodeOneStringMessage(nextByte);
//            }
//            else {
//                throw new IllegalArgumentException("Unknown opcode provided.");
//            }
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
        List words = message.getWords();
        String reply_str = "";
        int messageLength = 0;

        if (!Objects.isNull(words)) {
            for (Object word : words) {
                if (word instanceof String) {
                    messageLength += ((String) word).length(); // If the word is a string, add its length to the total length
                    reply_str += (String) word;

                    // TODO
                    System.out.println("word = " + word);
                    // TODO
                }
                else {
                    messageLength++; // Otherwise, add one to the length.
                }
            }
        }

        byte[] opcode = new byte[2];
        byte[] msg_opcode = new byte[2];
        opcode = shortToBytes((short)message.getOpcode()); // Must cast to short for the function to work
        if (message instanceof ResponseMessage)
            msg_opcode = shortToBytes((short)((ResponseMessage) message).getMessageOpcode()); // If it's a ResponseMessage instance, get the msgOpcode
        else
            throw new IllegalArgumentException("\"message\" must be an instance of ResponseMessage.");

        byte[] response = new byte[4]; // 2 - opcode, 2 - msg_opcode

        byte[] reply_bytes = reply_str.getBytes();

        if (reply_bytes.length > 0) {
            response = new byte[5 + messageLength]; // If there is a reply string: 2 - opcode, 2 - msg_opcode, messageLength - words, 1 - '\0'
            for (int i = 0; i < reply_bytes.length; i++) { // Insert the reply string into the response bytes array
                response[i + 4] = reply_bytes[i]; // Start from index 4
            }
            //response[4 + reply_bytes.length] = '\0'; // Mark the end of the string.
        }

        // Insert the opcode and the messageOpcode:
        response[0] = opcode[0];
        response[1] = opcode[1];
        response[2] = msg_opcode[0];
        response[3] = msg_opcode[1];

        // TODO
        System.out.print("response:");
        for(byte b : response)
            System.out.print(b + ",");
        System.out.println();
        // TODO

        return response;
    }

    /* OLD VERSION OF encode()
    @Override
    public byte[] encode_(Message message) {
        // Expect a ResponseMessage instance:
        ResponseMessage msg = (ResponseMessage)message;

        String text;
        try {
            text = (String) msg.getWords().get(0);
        }
        catch (NullPointerException e) {
            text = "";
        }

        byte[] response = new byte[4 + text.length()];

        byte[] opcode = shortToBytes((short)msg.getOpcode());
        byte[] msg_opcode = shortToBytes((short)msg.getMessageOpcode());

        response[0] = opcode[0];
        response[1] = opcode[1];
        response[2] = msg_opcode[0];
        response[3] = msg_opcode[1];

        byte[] textBytes = text.getBytes();

        if (textBytes.length > 0) {
            for (int i = 0; i < textBytes.length; i++) {
                response[i + 4] = textBytes[i];
            }
            response[3 + textBytes.length] = '\0'; // Mark the end of the string
        }

        // TODO: TEST
        System.out.print("Encoded response as: ");
        for (int i = 0; i < response.length; i++)
            System.out.print(response[i] + ", ");

        System.out.println();
        // TODO: TEST

        return response;
    }

     */
}