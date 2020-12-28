package bgu.spl.net.impl.bgrs;

import bgu.spl.net.api.Message;
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
        Database database = Database.getInstance();
        int port = parseInt(args[0]);
        int threads = parseInt(args[1]);
        try (Server<Message> server = new Reactor<Message>(
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
