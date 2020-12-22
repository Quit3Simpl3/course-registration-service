#include "connectionHandler.h"

#ifndef SPL_03_SERVERMASSAGE_H
#define SPL_03_SERVERMASSAGE_H

using namespace std;

class serverMassage {

public:

    serverMassage(ConnectionHandler* handler);

    void run();

private:
    bool* terminate;

};



#endif //SPL_03_SERVERMASSAGE_H
