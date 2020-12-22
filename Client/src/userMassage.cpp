#include "../include/serverMassage.h"
#include "../include/connectionHandler.h"
#include "../include/userMassage.h"
#include <boost//algorithm//string.hpp>


using namespace std;

userMassage::userMassage(ConnectionHandler *h) : handler(h){}

void userMassage::run() {

    *terminate = false;

    while(!(*terminate)) {
        char inCode[2]; //create because we need char[2] for connectionhandler
        vector<string> inPutLine; //holding the line
        const short bufsize = 1024;
        char buf[bufsize];
        cin.getline(buf, bufsize);
        string line(buf);
        int len = line.length();
        //take the massage part by part into vect:
        boost::split(inPutLine, line, boost::is_any_of("\0"));
        //Opcode 1:
        if (inPutLine[0] == "ADMINREG") {}
        //Opcode 2:
        if (inPutLine[0] == "STUDENTREG") {}
        //Opcode 3:
        if (inPutLine[0] == "LOGIN") {}
        //Opcode 4:
        if (inPutLine[0] == "LOGOUT") {}
        //Opcode 5:
        if (inPutLine[0] == "COUREREG") {}
        //Opcode 6:
        if (inPutLine[0] == "KDAMCHECK") {}
        //Opcode 7:
        if (inPutLine[0] == "COURSESTAT") {}
        //Opcode 8:
        if (inPutLine[0] == "STUDENSTAT") {}
        //Opcode 9:
        if (inPutLine[0] == "ISREGISTERED") {}
        //Opcode 10:
        if (inPutLine[0] == "UNREGISTER") {}
        //Opcode 11:
        if (inPutLine[0] == "MYCOURSES") {}






    }
}
