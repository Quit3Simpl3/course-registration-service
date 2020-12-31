package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.Message;
import bgu.spl.net.srv.*;

import java.io.IOException;

import static java.lang.Integer.parseInt;

public class TPCMain {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("You must supply one argument: port");
            System.exit(1);
        }

        Database database = Database.getInstance();
        int port = parseInt(args[0]);

        try (Server<Message> server = new BaseServer<Message>(
                    port,
                    BGRSProtocol::new,
                    MessageEncoderDecoderImpl::new
                ) {
                    @Override
                    protected void execute(BlockingConnectionHandler<Message> handler) {
                        new Thread(handler).start();
                    }
                }) {
            server.serve();
        }
        catch (IOException e) {

        }

        /*Server.threadPerClient(
                port,
                BGRSProtocol::new,
                MessageEncoderDecoderImpl::new
        ).serve();*/
    }
}
