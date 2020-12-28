#include "connectionHandler.h"


#ifndef SPL_03_SERVERMESSAGE_H
#define SPL_03_SERVERMESSAGE_H

using namespace std;

class serverMessage {

    public:

         serverMessage(ConnectionHandler* handler,bool* terminate);

         void run();

    private:
    ConnectionHandler* handler;
    bool* terminate;
    std::map<int, void (*)(char a[],ConnectionHandler* h, bool* terminate)> my_map;

};




#endif //SPL_03_SERVERMESSAGE_H
