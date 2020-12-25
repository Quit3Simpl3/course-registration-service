#include "connectionHandler.h"


#ifndef SPL_03_SERVERMASSAGE_H
#define SPL_03_SERVERMASSAGE_H

using namespace std;

class serverMassage {

    public:

         serverMassage(ConnectionHandler* handler);

         void run();

    private:
    ConnectionHandler* handler;
    bool* terminate;
    std::map<int, void (*)(std::vector<string>)> my_map;

};




#endif //SPL_03_SERVERMASSAGE_H
