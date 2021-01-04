#include "connectionHandler.h"

using namespace std;

class serverMessage {
    public:
        serverMessage(ConnectionHandler* handler, bool* terminate, bool* logOut);
        ~serverMessage(); // Destructor
        serverMessage(const serverMessage& other); // Copy-constructor
        void run();

    private:
        ConnectionHandler* handler;
        bool* terminate;
        bool* logOut;
        std::map<int, void (*)(char a[], ConnectionHandler* h, bool* terminate, bool* logOut)> my_map;
        static void ack(char a[], ConnectionHandler* h, bool* terminate, bool* l);
        static void error(char a[], ConnectionHandler* h, bool* terminate, bool* l);
        static short bytesToShort(char* bytesAr);
};