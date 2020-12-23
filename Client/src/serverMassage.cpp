#include "../include/serverMassage.h"
#include "../include/connectionHandler.h"
#include "../include/userMassage.h"
#include <boost//algorithm//string.hpp>

using namespace std;

void ACK(std::vector<string> s) {}
void ERROR(std::vector<string> s) {}

serverMassage::serverMassage(ConnectionHandler *h) : handler(h){
    my_map = std::map<std::string, void (*)(std::vector<string>)>();
    my_map["ACK"] = ACK;
    my_map["ERROR"] = ERROR;
}

void serverMassage::run() {

    *terminate = false;

    while (!(*terminate)) {
        int massageNum = 0;
        string serverInPut;
        if (!handler->getLine(serverInPut)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }

        vector<string> serverMassages; //holding the line
        vector<string> massage;
        const short bufsize = 1024;
        char buf[bufsize];
        cin.getline(buf, bufsize);
        string line(buf);
        int len = line.length();
        if (!handler->sendLine(line)) {
            std::cout << "ERROR" << std::endl;
            //need to decided what to do with ERROR
        } else
            boost::split(serverMassages, line, boost::is_any_of("\n"));
            while(serverMassages[massageNum+1] == "\n" ) {
                boost::split(massage, serverMassages[massageNum], boost::is_any_of(" "));
                my_map.at(massage[0]);
                serverMassages[massageNum+1] = ".";
                massageNum = massageNum+2;
            }
    }


}

/*
 *
 *
FUNCTION MAP:
void foo(std::vector<string> s) {int a=0;};
std::map<std::string, void (*)(std::vector<string>)> my_map;
my_map["hey"] = foo;
 *
 *
 */