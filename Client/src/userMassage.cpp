#include "../include/serverMassage.h"
#include "../include/connectionHandler.h"
#include "../include/userMassage.h"
#include <boost//algorithm//string.hpp>


using namespace std;

void adminReg(char a[],std::vector<string>,ConnectionHandler* h);
void studentReg(char a[],std::vector<string>,ConnectionHandler* h);
void login(char a[],std::vector<string>,ConnectionHandler* h);
void logout(char a[],std::vector<string>,ConnectionHandler* h);
void courseReg(char a[],std::vector<string>,ConnectionHandler* h);
void kdamCheck(char a[],std::vector<string>,ConnectionHandler* h);
void courseStat(char a[],std::vector<string>,ConnectionHandler* h);
void studentStat(char a[],std::vector<string>,ConnectionHandler* h);
void isRegistered(char a[],std::vector<string>,ConnectionHandler* h);
void unRegister(char a[],std::vector<string>,ConnectionHandler* h);
void myCourses(char a[],std::vector<string>,ConnectionHandler* h);


userMassage::userMassage(ConnectionHandler *h) : handler(h){


    my_map = std::map<std::string, void(*) (char a[],std::vector<string>,ConnectionHandler* h)>();

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


    vector<string> inPutLine = vector<string>(); //holding the line
    char* opCode = new char[2]; //create because we need char[2] for connectionhandler
}


void userMassage::run() {

    *terminate = false;

    while (!(*terminate)) {


        const short bufsize = 1024;
        char buf[bufsize];
        cin.getline(buf, bufsize);
        string line(buf);
        int len = line.length();

        boost::split(inPutLine, line, boost::is_any_of("\0"));

        (my_map.at(inPutLine[0]))(opCode,inPutLine,handler);

    }
}
void shortToBytes( char* bytesAr, short num) {
    bytesAr[0] = ((num >> 8) & 0xFF);
    bytesAr[1] = (num & 0xFF);
}
void adminReg(char a[],std::vector<string> v,ConnectionHandler* h) {
    shortToBytes(a,1);
    h->sendBytes(a,2);
    h->sendLine(v[1]);
    h->sendLine(v[2]);

};


void studentReg(char a[],std::vector<string> v,ConnectionHandler* h) {
    shortToBytes(a, 2);
    h->sendBytes(a, 2);
    h->sendLine(v[1]);
    h->sendLine(v[2]);
}
void login(char a[],std::vector<string> v,ConnectionHandler* h) {
    shortToBytes(a, 3);
    h->sendBytes(a, 2);
    h->sendLine(v[1]);
    h->sendLine(v[2]);
}

void logout(char a[],std::vector<string> v,ConnectionHandler* h) {
    shortToBytes(a, 4);
    h->sendBytes(a, 2);
}

void courseReg(char a[],std::vector<string> v,ConnectionHandler* h) {
    shortToBytes(a, 5);
    h->sendBytes(a, 2);
    h->sendLine(v[1]);
}

void kdamCheck(char a[],std::vector<string> v,ConnectionHandler* h) {
    shortToBytes(a, 6);
    h->sendBytes(a, 2);
    h->sendLine(v[1]);
}

void courseStat(char a[],std::vector<string> v,ConnectionHandler* h) {
    shortToBytes(a, 7);
    h->sendBytes(a, 2);
    h->sendLine(v[1]);

}

void studentStat(char a[],std::vector<string> v,ConnectionHandler* h) {
    shortToBytes(a, 8);
    h->sendBytes(a, 2);
    h->sendLine(v[1]);
}

void isRegistered(char a[],std::vector<string> v,ConnectionHandler* h) {
    shortToBytes(a, 9);
    h->sendBytes(a, 2);
    h->sendLine(v[1]);
}

void unRegister(char a[],std::vector<string> v,ConnectionHandler* h) {
    shortToBytes(a, 10);
    h->sendBytes(a, 2);
    h->sendLine(v[1]);
}
void myCourses(char a[],std::vector<string> v,ConnectionHandler* h) {
    shortToBytes(a, 11);
    h->sendBytes(a, 2);
}