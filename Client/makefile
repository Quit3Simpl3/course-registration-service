CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -pthread

all: BGRSclient
	g++ -o bin/BGRSclient bin/connectionHandler.o bin/client.o bin/serverMessage.o bin/userMessage.o $(LDFLAGS) 

BGRSclient: bin/serverMessage.o bin/userMessage.o bin/connectionHandler.o bin/client.o

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/client.o: src/client.cpp
	g++ $(CFLAGS) -o bin/client.o src/client.cpp

bin/serverMessage.o: src/serverMessage.cpp
	g++ $(CFLAGS) -o bin/serverMessage.o src/serverMessage.cpp

bin/userMessage.o: src/userMessage.cpp
	g++ $(CFLAGS) -o bin/userMessage.o src/userMessage.cpp

.PHONY: clean
clean:
	rm -f bin/*
