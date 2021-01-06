#include "connectionHandler.h"

using namespace std;

class userMessage {
    public:
        userMessage(ConnectionHandler* handler, bool* terminate, bool* logOut);
        virtual ~userMessage(); // Destructor
        userMessage(const userMessage& other); // Move-constructor
        userMessage &operator=(const userMessage &other);
        void run();
        void close();

    private:
        ConnectionHandler& handler;
        bool& terminate;
        bool& logOut;
        char* msg;
        std::map<std::string, void (userMessage::*)(char a[], std::vector<string>, ConnectionHandler& h, bool& logOut)> my_map;

        void adminReg(char a[],std::vector<string>, ConnectionHandler& h, bool& l);
        void studentReg(char a[],std::vector<string>, ConnectionHandler& h, bool& l);
        void login(char a[],std::vector<string>,ConnectionHandler& h, bool& l);
        void logout(char a[],std::vector<string>,ConnectionHandler& h, bool& l);
        void courseReg(char a[],std::vector<string>,ConnectionHandler& h, bool& l);
        void kdamCheck(char a[],std::vector<string>,ConnectionHandler& h, bool& l);
        void courseStat(char a[],std::vector<string>,ConnectionHandler& h, bool& l);
        void studentStat(char a[],std::vector<string>,ConnectionHandler& h, bool& l);
        void isRegistered(char a[],std::vector<string>,ConnectionHandler& h, bool& l);
        void unRegister(char a[],std::vector<string>,ConnectionHandler& h, bool& l);
        void myCourses(char a[],std::vector<string>,ConnectionHandler& h, bool& l);
};