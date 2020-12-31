#include "../include/serverMessage.h"
#include "../include/connectionHandler.h"
#include "../include/userMessage.h"
#include <boost//algorithm//string.hpp>

using namespace std;

void ACK(char a[],ConnectionHandler* h,bool* terminate,bool* l);
void ERROR(char a[],ConnectionHandler* h,bool* terminate,bool* l);
short bytesToShort(char* bytesAr);

serverMessage::serverMessage(ConnectionHandler *h, bool* t, bool* l) : handler(h), terminate(t), logOut(l){
    my_map = std::map<int, void (*)(char a[], ConnectionHandler* h, bool* terminate, bool* l)>();
    my_map[12] = ACK;
    my_map[13] = ERROR;
}

void serverMessage::run() {
    while (!(*terminate)) {
        char * opCode = new char[2];
        handler->getBytes(opCode, 2);
        short opCodeNum = bytesToShort(opCode);

        cout << "Received from server: " << opCodeNum << endl; // TODO

        (my_map.at(opCodeNum))(opCode, handler, terminate,logOut);
        delete[] opCode;
    }
}
void ERROR(char a[], ConnectionHandler* h, bool* terminate,bool* l){
    string outPut = "ERROR";
    h->getBytes(a,2);
    short messageNum = bytesToShort(a);
    if (messageNum == 4)
        *l = false;
    outPut += " " + to_string(messageNum);

    cout << "Received error from server" << endl; // TODO

    cout << outPut << endl;
}
void ACK(char a[], ConnectionHandler* h, bool* terminate, bool* l) {
    string outPut = "ACK";

    char messageOpCode[2];
    h->getBytes(messageOpCode, 2);

    cout << "Received ack from server " << messageOpCode << endl; // TODO

    short messageNum = bytesToShort(messageOpCode);
    outPut += " " + to_string(messageNum);
    if (messageNum == 4) {
        *terminate = true;
    }
    if (messageNum == 6) {
        outPut += '\n';
        outPut += "Kdam Courses: ";
         h->getLine(outPut);
    }
    if (messageNum == 7) {
        outPut += '\n';
        outPut += "Course: ";
        h->getLine(outPut);
        outPut += '\n';
        outPut += "Seats Available: ";
        h->getLine(outPut);
        outPut += '\n';
        outPut += "Students Registered: ";
        h->getLine(outPut);
    }

    if (messageNum == 8) {
        outPut += '\n';
        outPut += "Student: ";
        h->getLine(outPut);
        outPut += '\n';
        outPut += "Courses: ";
        h->getLine(outPut);
    }

    if (messageNum == 9) {
        outPut += '\n';
        h->getLine(outPut);
    }
    if (messageNum == 11) {
        outPut += '\n';
        outPut += "My Courses: ";
        h->getLine(outPut);
    }



    cout << outPut << endl;
}

short bytesToShort(char* bytesAr) {
    short result = (short)((bytesAr[0] & 0xff)<<8);
    result += (short)(bytesAr[1] & 0xff);
    return result;
}
