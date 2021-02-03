package cli_course_reg.impl.BGRSServer;

import cli_course_reg.api.Message;
import cli_course_reg.srv.Database;
import cli_course_reg.srv.Reactor;
import cli_course_reg.srv.Server;

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

        try (Server<Message<?>> server = new Reactor<>(
                threads,
                port,
                BGRSProtocol::new,
                MessageEncoderDecoderImpl::new
        )) {
            server.serve(); // Start the server reactor...
        }
        catch (IOException e) {
        }
    }
}
