cmake_minimum_required(VERSION 3.10)

# set the project name
project(spl-03)

#Set your directories.  The dot representes the root application folder.
#Thus my the path to my domain folder:
set(HEADER_FILES ./include)
set(SOURCE ./src)
set(Boost_INCLUDE_DIR C:/boost_1_67_0/boost_1_67_0/boost)
set(Boost_LIBRARY_DIR c:/boost_1_67_0/boost_1_67_0/lib/)

#Include the directories and now your cpp files will recognize your headers
find_package(Boost COMPONENTS system filesystem REQUIRED)
include_directories(${Boost_INCLUDE_DIR})

include_directories(${HEADER_FILES})
include_directories(${SOURCE})




set(
        SOURCE_FILES
        ${HEADER_FILES}/connectionHandler.h
        ${SOURCE}/echoClient.cpp
        ${SOURCE}/connectionHandler.cpp
)

# add the executable
add_executable(spl-03 ${SOURCE_FILES})
