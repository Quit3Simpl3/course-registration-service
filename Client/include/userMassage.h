#include "connectionHandler.h"

#ifndef SPL_03_USERMASSAGE_H
#define SPL_03_USERMASSAGE_H


using namespace std;

class userMassage {

    public:

        userMassage(ConnectionHandler* handler);

        void run();


    //void shortToBytes(short num, char *bytesAr);


    private:
        ConnectionHandler* handler;
        bool* terminate;
        std::map<std::string, void (*)(char a[],std::vector<string>,ConnectionHandler* h)> my_map;
        vector<string> inPutLine;
        char opCode[2];





};
















#endif //SPL_03_USERMASSAGE_H
