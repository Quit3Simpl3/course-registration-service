#include "../include/serverMassage.h"
#include "../include/connectionHandler.h"
#include "../include/userMassage.h"
#include <boost//algorithm//string.hpp>

using namespace std;

void ACK(std::vector<string> s);
void ERROR(std::vector<string> s);
short bytesToShort(char* bytesAr);

serverMassage::serverMassage(ConnectionHandler *h) : handler(h){
    my_map = std::map<int, void (*)(std::vector<string>)>();
    my_map[12] = ACK;
    my_map[13] = ERROR;
}

void serverMassage::run() {

    *terminate = false;

    while (!(*terminate)) {



    }
}



short bytesToShort(char* bytesAr) {
    short result = (short)((bytesAr[0] & 0xff)<<8);
    result += (short)(bytesAr[1] & 0xff);
    return result;
}

void ACK(std::vector<string> s) { int a = 2;}


