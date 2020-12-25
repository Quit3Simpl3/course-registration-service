package bgu.spl.net.impl.bgrs;

import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.Reactor;
import bgu.spl.net.srv.Server;

import java.io.IOException;

import static java.lang.Integer.parseInt;

public class Main {
    public static void main(String[] args) {
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
