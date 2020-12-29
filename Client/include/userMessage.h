#include "connectionHandler.h"

#ifndef SPL_03_USERMASSAGE_H
#define SPL_03_USERMASSAGE_H


using namespace std;

class userMessage {

    public:

        userMessage(ConnectionHandler* handler,bool* terminate, bool* logOut);

        void run();


    //void shortToBytes(short num, char *bytesAr);


    private:
        ConnectionHandler* handler;
        bool* terminate;
        bool* logOut;
        std::map<std::string, void (*)(char a[],std::vector<string>,ConnectionHandler* h, bool* logOut)> my_map;







};
















#endif //SPL_03_USERMASSAGE_H
