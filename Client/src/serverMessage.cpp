#include "serverMessage.h"
#include "connectionHandler.h"
#include "userMessage.h"
#include <boost//algorithm//string.hpp>

using namespace std;

 void serverMessage::error(char a[], ConnectionHandler* h, bool* terminate, bool* l) {
    string outPut = "ERROR";
    h->getBytes(a,2);
    short messageNum = bytesToShort(a);
    if (messageNum == 4)
        *l = false;
    outPut += " " + to_string(messageNum);

    cout << outPut << endl;
}

void serverMessage::ack(char a[], ConnectionHandler* h, bool* terminate, bool* l) {
    string outPut = "ACK";

    char messageOpCode[2];
    h->getBytes(messageOpCode, 2);

    short messageNum = bytesToShort(messageOpCode);
    outPut += " " + to_string(messageNum);
    if (messageNum == 4) {
        *terminate = true;
    }
    if (messageNum == 6) {
        outPut += '\n';
        h->getLine(outPut);
    }
    if (messageNum == 7) {
        outPut += '\n';
        h->getLine(outPut);
        outPut += '\n';
        h->getLine(outPut);
        outPut += '\n';
        h->getLine(outPut);
    }

    if (messageNum == 8) {
        outPut += '\n';
        h->getLine(outPut);
        outPut += '\n';
        h->getLine(outPut);
    }

    if (messageNum == 9) {
        outPut += '\n';
        h->getLine(outPut);
    }
    if (messageNum == 11) {
        outPut += '\n';
        h->getLine(outPut);
    }
    cout << outPut << endl;
}

serverMessage::serverMessage(ConnectionHandler *h, bool* t, bool* l) :
    handler(h),
    terminate(t),
    logOut(l),
    my_map(std::map<int, void (*)(char a[], ConnectionHandler* h, bool* terminate, bool* l)>()) {
    my_map[12] = serverMessage::ack;
    my_map[13] = serverMessage::error;
}

void serverMessage::run() {
    while (!(*terminate)) {
        char * opCode = new char[2];
        handler->getBytes(opCode, 2);
        short opCodeNum = serverMessage::bytesToShort(opCode);
        (my_map.at(opCodeNum))(opCode, handler, terminate,logOut);
        delete[] opCode;
    }
}

short serverMessage::bytesToShort(char *bytesAr) {
    short result = (short)((bytesAr[0] & 0xff)<<8);
    result += (short)(bytesAr[1] & 0xff);
    return result;
}

serverMessage::~serverMessage() {
    delete handler;
    delete terminate;
    delete logOut;
}

serverMessage::serverMessage(const serverMessage &other) :
    handler(other.handler),
    terminate(other.terminate),
    logOut(other.logOut),
    my_map(other.my_map) {/*Copy-Constructor*/}
