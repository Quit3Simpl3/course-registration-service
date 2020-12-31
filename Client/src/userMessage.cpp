#include "../include/serverMessage.h"
#include "../include/connectionHandler.h"
#include "../include/userMessage.h"
#include <boost//algorithm//string.hpp>
//#include "../src/serverMessage.cpp"



using namespace std;

void adminReg(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
void studentReg(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
void login(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
void logout(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
void courseReg(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
void kdamCheck(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
void courseStat(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
void studentStat(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
void isRegistered(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
void unRegister(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
void myCourses(char a[],std::vector<string>,ConnectionHandler* h,bool* l);

userMessage::userMessage(ConnectionHandler *h,bool* t,bool* l) : handler(h), terminate(t),logOut(l) {
    my_map = std::map<std::string, void(*) (char a[],std::vector<string>, ConnectionHandler* h,bool* l)>();
    my_map["ADMINREG"] = adminReg;
    my_map["STUDENTREG"] = studentReg;
    my_map["LOGIN"] = login;
    my_map["LOGOUT"] = logout;
    my_map["COURSEREG"] = courseReg;
    my_map["KDAMCHECK"] = kdamCheck;
    my_map["COURSESTAT"] = courseStat;
    my_map["STUDENTSTAT"] = studentStat;
    my_map["ISREGISTERED"] = isRegistered;
    my_map["UNREGISTER"] = unRegister;
    my_map["MYCOURSES"] = myCourses;
}

void userMessage::run() {
    while (!(*terminate)) {
        if (!(*logOut)) {
            vector<string> inPutLine = vector<string>();
            char *opCode = new char[2];
            const short bufsize = 1024;
            char buf[bufsize];
            cin.getline(buf, bufsize);
            string line(buf);
            boost::split(inPutLine, line, boost::is_any_of(" "));
            (my_map.at(inPutLine[0]))(opCode, inPutLine, handler,logOut);
            delete[] opCode;
        }
    }
}


void sendShort(char a[], char shortMsg[], ConnectionHandler* h) {
    char *charToSend = new char[4];
    charToSend[0] = a[0];
    charToSend[1] = a[1];
    charToSend[2] = shortMsg[0];
    charToSend[3] = shortMsg[1];
    h->sendBytes(charToSend, 4);


    // TODO
    cout << "sent msg: START:";

    char *w1 = new char[2];
    w1[0] = charToSend[0];
    w1[1] = charToSend[1];
    char *w2 = new char[2];
    w2[0] = charToSend[2];
    w2[1] = charToSend[3];


    short w01 = bytesToShort(w1);
    short w02 = bytesToShort(w2);

    cout << "the opcode is:" << w01 << ", ";
    cout << "the message is:" << w02 << ". ";
    cout << "END." << endl;

    delete[] charToSend;



}

void stringToBytes(char a[], string line, int size, ConnectionHandler* h) {

    int line_size = size + 2;
    char *lineToSend = new char[line_size];

    lineToSend[0] = a[0];
    lineToSend[1] = a[1];

    for (int j = 2; j < line_size; j++) {
        lineToSend[j] = line[j - 2];
    }

    h->sendBytes(lineToSend, line_size);

    // TODO
    cout << "sent msg: START:";

    char *w1 = new char[2];
    w1[0] = lineToSend[0];
    w1[1] = lineToSend[1];
    char *w2 = new char[2];
    w2[2] = lineToSend[2];
    w2[3] = lineToSend[3];


    short w01 = bytesToShort(w1);
    short w02 = bytesToShort(w2);

    cout << "the opcode is:" << w01 << ",";
    cout << "the msg is:" << w02 << ",";
    cout << "END." << endl;

    delete[] lineToSend;


}

void shortToBytes(char* bytesAr, short num) {
    bytesAr[0] = ((num >> 8) & 0xFF);
    bytesAr[1] = (num & 0xFF);
}

void adminReg(char a[],std::vector<string> v,ConnectionHandler* h,bool* l) {
    shortToBytes(a, 1);
    string send = v[1]+'\0'+v[2]+'\0';
    int len = send.length();
    stringToBytes(a,send,len,h);
}

void studentReg(char a[],std::vector<string> v,ConnectionHandler* h,bool* l) {
    shortToBytes(a, 2);
    string send = v[1]+'\0'+v[2]+'\0';
    int len = send.length();
    stringToBytes(a,send,len,h);
}

void login(char a[],std::vector<string> v,ConnectionHandler* h,bool* l) {
    shortToBytes(a, 3);
    string send = v[1]+'\0'+v[2]+'\0';
    int len = send.length();
    stringToBytes(a,send,len,h);
}

void logout(char a[],std::vector<string> v, ConnectionHandler* h, bool* l) {
    *l = true;
    shortToBytes(a, 4);
    h->sendBytes(a, 2);
}

void courseReg(char a[], std::vector<string> v, ConnectionHandler* h, bool* l) {
    shortToBytes(a, 5);
    short send_short = stoi(v[1]);
    char *msg = new char[2];
    shortToBytes(msg, send_short);
    cout << "send_short = " << send_short << endl;
    sendShort(a, msg, h);
}

void kdamCheck(char a[],std::vector<string> v, ConnectionHandler* h, bool* l) {
    shortToBytes(a, 6);
    short send_short = stoi(v[1]);
    char *msg = new char[2];
    shortToBytes(msg, send_short);
    cout << "send_short = " << send_short << endl;
    sendShort(a, msg, h);
}

void courseStat(char a[],std::vector<string> v, ConnectionHandler* h, bool* l) {
    shortToBytes(a, 7);
    short send_short = stoi(v[1]);
    char *msg = new char[2];
    shortToBytes(msg, send_short);
    cout << "send_short = " << send_short << endl;
    sendShort(a, msg, h);
}

void studentStat(char a[],std::vector<string> v, ConnectionHandler* h, bool* l) {
    shortToBytes(a, 8);
    string send = v[1]+'\0';
    int len = send.length();
    stringToBytes(a,send,len,h);
}

void isRegistered(char a[],std::vector<string> v, ConnectionHandler* h, bool* l) {
    shortToBytes(a, 9);
    short send_short = stoi(v[1]);
    char *msg = new char[2];
    shortToBytes(msg, send_short);
    cout << "send_short = " << send_short << endl;
    sendShort(a, msg, h);
}

void unRegister(char a[],std::vector<string> v,ConnectionHandler* h,bool* l) {
    shortToBytes(a, 10);
    short send_short = stoi(v[1]);
    char *msg = new char[2];
    shortToBytes(msg, send_short);
    cout << "send_short = " << send_short << endl;
    sendShort(a, msg, h);
}
void myCourses(char a[],std::vector<string> v,ConnectionHandler* h,bool* l) {
    shortToBytes(a, 11);
    h->sendBytes(a, 2);
}
