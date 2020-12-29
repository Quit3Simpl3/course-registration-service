#include "../include/serverMessage.h"
#include "../include/connectionHandler.h"
#include "../include/userMessage.h"
#include <boost//algorithm//string.hpp>


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
void stringToBytes(char a[], string line, int size, ConnectionHandler* h) {
    // char* lineToSend = new char[size+3];
    int line_size = size + 2;
    char* lineToSend = new char[line_size];

    lineToSend[0] = a[0];
    lineToSend[1] = a[1];

    for (int j = 2; j<line_size; j++) {
        lineToSend[j] = line[j-2];
    }

    h->sendBytes(lineToSend,line_size);

    // TODO
    cout << "sent msg: START:";
    for (int i = 0; i < line_size; i++) {
        cout << lineToSend[i] << ",";
    }
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
};


void studentReg(char a[],std::vector<string> v,ConnectionHandler* h,bool* l) {
    shortToBytes(a, 2);
    string send = v[1]+'\0'+v[2]+'\0';
    int len = send.length();
    stringToBytes(a,send,len,h);
};
void login(char a[],std::vector<string> v,ConnectionHandler* h,bool* l) {
    shortToBytes(a, 3);
    string send = v[1]+'\0'+v[2]+'\0';
    int len = send.length();
    stringToBytes(a,send,len,h);
};

void logout(char a[],std::vector<string> v,ConnectionHandler* h,bool* l) {
    *l = true;
    shortToBytes(a, 4);
    h->sendBytes(a, 2);
}

void courseReg(char a[],std::vector<string> v,ConnectionHandler* h,bool* l) {
    shortToBytes(a, 5);
    string send = v[1];
    int len = send.length();
    stringToBytes(a,send,len,h);
}

void kdamCheck(char a[],std::vector<string> v,ConnectionHandler* h,bool* l) {
    shortToBytes(a, 6);
    string send = v[1];
    int len = send.length();
    stringToBytes(a,send,len,h);
}

void courseStat(char a[],std::vector<string> v,ConnectionHandler* h,bool* l) {
    shortToBytes(a, 7);
    string send = v[1];
    int len = send.length();
    stringToBytes(a,send,len,h);

}

void studentStat(char a[],std::vector<string> v,ConnectionHandler* h,bool* l) {
    shortToBytes(a, 8);
    string send = v[1]+'\0';
    int len = send.length();
    stringToBytes(a,send,len,h);
}

void isRegistered(char a[],std::vector<string> v,ConnectionHandler* h,bool* l) {
    shortToBytes(a, 9);
    string send = v[1];
    int len = send.length();
    stringToBytes(a,send,len,h);
}

void unRegister(char a[],std::vector<string> v,ConnectionHandler* h,bool* l) {
    shortToBytes(a, 10);
    string send = v[1];
    int len = send.length();
    stringToBytes(a,send,len,h);
}
void myCourses(char a[],std::vector<string> v,ConnectionHandler* h,bool* l) {
    shortToBytes(a, 11);
    h->sendBytes(a, 2);
}

