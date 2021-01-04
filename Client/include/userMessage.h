#include "connectionHandler.h"

using namespace std;

class userMessage {
    public:
        userMessage(ConnectionHandler* handler,bool* terminate, bool* logOut);
        ~userMessage(); // Destructor
        userMessage(const userMessage& other); // Copy-constructor
        void run();

    private:
        ConnectionHandler* handler;
        bool* terminate;
        bool* logOut;
        std::map<std::string, void (*)(char a[],std::vector<string>,ConnectionHandler* h, bool* logOut)> my_map;

        static void adminReg(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
        static void studentReg(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
        static void login(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
        static void logout(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
        static void courseReg(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
        static void kdamCheck(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
        static void courseStat(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
        static void studentStat(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
        static void isRegistered(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
        static void unRegister(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
        static void myCourses(char a[],std::vector<string>,ConnectionHandler* h,bool* l);
};