#include "connectionHandler.h"

using namespace std;

class serverMessage {
    public:
        serverMessage(ConnectionHandler* handler, bool* terminate, bool* logOut);
        virtual ~serverMessage(); // Destructor
        void run();

    private:
        ConnectionHandler& handler;
        bool& terminate;
        bool& logOut;
        std::map<int, void (*)(char a[], ConnectionHandler& h, bool& terminate, bool& logOut)> my_map;
        static void ack(char a[], ConnectionHandler& h, bool& terminate, bool& l);
        static void error(char a[], ConnectionHandler& h, bool& terminate, bool& l);
        static short bytesToShort(char* bytesAr);
};