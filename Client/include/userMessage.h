#include "connectionHandler.h"

#ifndef SPL_03_USERMASSAGE_H
#define SPL_03_USERMASSAGE_H


using namespace std;

class userMessage {

    public:

        userMessage(ConnectionHandler* handler,bool* t);

        void run();


    //void shortToBytes(short num, char *bytesAr);


    private:
        ConnectionHandler* handler;
        bool* terminate;
        std::map<std::string, void (*)(char a[],std::vector<string>,ConnectionHandler* h)> my_map;







};
















#endif //SPL_03_USERMASSAGE_H
