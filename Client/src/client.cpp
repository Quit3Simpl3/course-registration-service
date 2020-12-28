#include <stdlib.h>
#include "../include/connectionHandler.h"
#include "../include/userMessage.h"
#include "../include/serverMessage.h"
/*#include "../src/connectionHandler.cpp"
#include "../src/userMessage.cpp"
#include "../src/serverMessage.cpp"*/

#include <thread>

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }

    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    bool* terminate= new bool;
    *terminate = false;
    userMessage userIn(&connectionHandler,terminate);
    serverMessage serverIn(&connectionHandler,terminate);

    thread user(&userMessage::run, &userIn);
    thread server(&serverMessage::run, &serverIn);

    //sync because we dont want to lose data from server
    server.join();
    user.join();


    delete terminate;
    return 0;
}
