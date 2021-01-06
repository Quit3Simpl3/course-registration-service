#include <stdlib.h>
#include "connectionHandler.h"
#include "userMessage.h"
#include "serverMessage.h"

#include <thread>

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

    bool terminate = false;
    bool logOut = false;

    userMessage userIn = userMessage(&connectionHandler, &terminate, &logOut);
    serverMessage serverIn = serverMessage(&connectionHandler, &terminate, &logOut);

    thread user(&userMessage::run, &userIn);
    thread server(&serverMessage::run, &serverIn);

    // Sync because we don't want to lose data from server
    server.join();
    user.join();

    connectionHandler.close();

    return 0;
}
