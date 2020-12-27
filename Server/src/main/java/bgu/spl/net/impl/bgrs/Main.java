package bgu.spl.net.impl.bgrs;

import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.Reactor;
import bgu.spl.net.srv.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Main {
    public static void main(String[] args) {
        List<String> str1 = new ArrayList<>();
        str1.add("hey11");
        str1.add("hey13");
        str1.add("hey12");

        List<String> str2 = new ArrayList<>();
        str2.add("hey21");

        List<Integer> int1 = new ArrayList<>();
        int1.add(1);
        int1.add(2);
        int1.add(3);

        List<Integer> int2 = new ArrayList<>();
        int2.add(1);
        int2.add(3);
        int2.add(2);
        int2.add(3);

        List<Integer> int3 = new ArrayList<>();

        System.out.println("str1: " + str1.toString());
        System.out.println("str2: " + str2.toString());
        System.out.println("int1: " + int1.toString());
        System.out.println("int2: " + int2.toString());
        Collections.sort(int2);
        System.out.println("int2 sorted: " + int2.toString());
        System.out.println("int3: " + int3.toString());

        Database database = Database.getInstance();
        int port = parseInt(args[0]);
        int threads = parseInt(args[1]);
        try (Server<String> server = new Reactor<>(
                threads,
                port,
                BGRSProtocol::new,
                MessageEncoderDecoderImpl::new
            )) {
            server.serve(); // Start the server reactor...
        }
        catch (IOException e) {
            // TODO
        }
    }
}
