package cli_course_reg.impl.BGRSServer;

public class Main {
    public static void main(String[] args) {
        if (args.length == 1) // Handle TPC server
            TPCMain.main(args);

        else if (args.length == 2) // Handle Reactor server
            ReactorMain.main(args);
    }
}
