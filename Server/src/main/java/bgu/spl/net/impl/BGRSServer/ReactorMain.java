package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.Message;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.Reactor;
import bgu.spl.net.srv.Server;

import java.io.IOException;

import static java.lang.Integer.parseInt;

public class ReactorMain {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("You must supply two arguments: port, threads");
            System.exit(1);
        }

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
