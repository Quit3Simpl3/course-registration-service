cmake_minimum_required(VERSION 3.10)

# set the project name
project(spl-03)

set(SOURCE_FILES
        chat_server.cpp
        asio.hpp)



# add the executable
add_executable(spl-03 echoClient.cpp)
