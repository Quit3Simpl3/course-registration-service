#include "serverMessage.h"
#include "connectionHandler.h"
#include "userMessage.h"
#include <boost//algorithm//string.hpp>

#include <exception>

using namespace std;

userMessage::userMessage(ConnectionHandler* h, bool* t, bool* l) :
    handler(*h),
    terminate(*t),
    logOut(*l),
    msg(new char[2]),
    my_map(std::map<std::string, void(userMessage::*) (char a[], std::vector<string>, ConnectionHandler& h, bool& l)>()) {
    my_map["ADMINREG"] = &userMessage::adminReg;
    my_map["STUDENTREG"] = &userMessage::studentReg;
    my_map["LOGIN"] = &userMessage::login;
    my_map["LOGOUT"] = &userMessage::logout;
    my_map["COURSEREG"] = &userMessage::courseReg;
    my_map["KDAMCHECK"] = &userMessage::kdamCheck;
    my_map["COURSESTAT"] = &userMessage::courseStat;
    my_map["STUDENTSTAT"] = &userMessage::studentStat;
    my_map["ISREGISTERED"] = &userMessage::isRegistered;
    my_map["UNREGISTER"] = &userMessage::unRegister;
    my_map["MYCOURSES"] = &userMessage::myCourses;
}

short stringToShort(std::vector<string>& v) {
    if (v.size() < 2) {
        throw exception();
    }
    return (short)stoi((string)v[1]);
}

void userMessage::run() {
    while (!(terminate)) {
        if (!(logOut)) {
            vector<string> inPutLine = vector<string>(3, "");
            char *opCode = new char[2];
            const short bufsize = 1024;
            char buf[bufsize];
            cin.getline(buf, bufsize);
            string line(buf);
            boost::split(inPutLine, line, boost::is_any_of(" "));
            try {
                void (userMessage::*func)(char a[], std::vector<string>, ConnectionHandler& h, bool& l);
                func = userMessage::my_map.at(inPutLine[0]);
                (this->*func)(opCode, inPutLine, handler, logOut);
            }
            catch (const exception &e) {
                // DO NOTHING
            }
            delete[] opCode;
        }
    }
}

void sendShort(char a[], char shortMsg[], ConnectionHandler& h) {
    char *charToSend = new char[4];
    charToSend[0] = a[0];
    charToSend[1] = a[1];
    charToSend[2] = shortMsg[0];
    charToSend[3] = shortMsg[1];
    h.sendBytes(charToSend, 4);
    delete[] charToSend;
}

void stringToBytes(char a[], string line, int size, ConnectionHandler& h) {
    int line_size = size + 2;
    char *lineToSend = new char[line_size];
    lineToSend[0] = a[0];
    lineToSend[1] = a[1];
    for (int j = 2; j < line_size; j++) {
        lineToSend[j] = line[j - 2];
    }

    h.sendBytes(lineToSend, line_size);
    delete[] lineToSend;
}

void shortToBytes(char* bytesAr, short num) {
    bytesAr[0] = ((num >> 8) & 0xFF);
    bytesAr[1] = (num & 0xFF);
}

string getStringMessage(std::vector<string>& v) {
    if (v.size() < 3 || v[1].length() < 1 || v[2].length() < 1) {
        throw exception();
    }
    return v[1] + '\0' + v[2] + '\0';
}

void userMessage::adminReg(char a[], std::vector<string> v, ConnectionHandler& h, bool& l) {
    shortToBytes(a, 1);
    string send = getStringMessage(v);

    int len = send.length();
    stringToBytes(a,send,len,h);
}

void userMessage::studentReg(char a[], std::vector<string> v, ConnectionHandler& h, bool& l) {
    shortToBytes(a, 2);
    string send = getStringMessage(v);

    int len = send.length();
    stringToBytes(a, send, len, h);
}

void userMessage::login(char a[], std::vector<string> v, ConnectionHandler& h,bool& l) {
    shortToBytes(a, 3);
    string send = getStringMessage(v);

    int len = send.length();
    stringToBytes(a,send,len,h);
}

void userMessage::logout(char a[], std::vector<string> v, ConnectionHandler& h, bool& l) {
    l = true;
    shortToBytes(a, 4);
    h.sendBytes(a, 2);
}

void userMessage::courseReg(char a[], std::vector<string> v, ConnectionHandler& h, bool& l) {
    shortToBytes(a, 5);
    short send_short = stringToShort(v);

    this->msg = new char[2];
    shortToBytes(this->msg, send_short);
    sendShort(a, this->msg, h);
}

void userMessage::kdamCheck(char a[], std::vector<string> v, ConnectionHandler& h, bool& l) {
    shortToBytes(a, 6);
    short send_short = stringToShort(v);

    this->msg = new char[2];
    shortToBytes(msg, send_short);
    sendShort(a, msg, h);
}

void userMessage::courseStat(char a[], std::vector<string> v, ConnectionHandler& h, bool& l) {
    shortToBytes(a, 7);
    short send_short = stringToShort(v);

    this->msg = new char[2];
    shortToBytes(msg, send_short);
    sendShort(a, msg, h);
}

void userMessage::studentStat(char a[], std::vector<string> v, ConnectionHandler& h, bool& l) {
    shortToBytes(a, 8);
    if (v.size() < 2) {
        throw exception();
    }
    string send = v[1] + '\0';
    int len = send.length();
    stringToBytes(a,send,len,h);
}

void userMessage::isRegistered(char a[], std::vector<string> v, ConnectionHandler& h, bool& l) {
    shortToBytes(a, 9);
    short send_short = stringToShort(v);

    char *msg = new char[2];
    shortToBytes(msg, send_short);
    sendShort(a, msg, h);
}

void userMessage::unRegister(char a[], std::vector<string> v, ConnectionHandler& h, bool& l) {
    shortToBytes(a, 10);
    short send_short = stringToShort(v);

    this->msg = new char[2];
    shortToBytes(this->msg, send_short);
    sendShort(a, this->msg, h);
}
void userMessage::myCourses(char a[], std::vector<string> v, ConnectionHandler& h, bool& l) {
    shortToBytes(a, 11);
    h.sendBytes(a, 2);
}

void userMessage::close() {
    delete[] this->msg;
}

userMessage::~userMessage() {/*Destructor*/
    close();
}

userMessage::userMessage(const userMessage &other) :
    handler(other.handler),
    terminate(other.terminate),
    logOut(other.logOut),
    msg(other.msg),
    my_map(other.my_map) {/*Copy-Constructor*/
}

userMessage &userMessage::operator=(const userMessage &other) {
    terminate = other.terminate;
    logOut = other.logOut;
    msg = other.msg;
    my_map = other.my_map;
    return *this;
}