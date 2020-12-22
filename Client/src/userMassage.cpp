#include "../include/serverMassage.h"
#include "../include/connectionHandler.h"
#include "../include/userMassage.h"


using namespace std;

userMassage::userMassage(ConnectionHandler *h) : handler(h){}

void userMassage::run() {

    *terminate = false;

    while(!(*terminate)) {
        const short bufsize = 1024;
        char buf[bufsize];
        cin.getline(buf, bufsize);
        string line(buf);
        int len=line.length();
    }
}
