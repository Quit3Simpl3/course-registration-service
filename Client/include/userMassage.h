#include "connectionHandler.h"

#ifndef SPL_03_USERMASSAGE_H
#define SPL_03_USERMASSAGE_H


using namespace std;

class userMassage {

    public:

        userMassage(ConnectionHandler* handler);

        void run();

    private:
        ConnectionHandler* handler;
        bool* terminate;

};
















#endif //SPL_03_USERMASSAGE_H
