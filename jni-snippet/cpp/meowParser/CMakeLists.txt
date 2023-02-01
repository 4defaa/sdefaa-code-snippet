cmake_minimum_required(VERSION 3.0.0)
project(meowParser VERSION 0.1.0)

include(CTest)
enable_testing()

add_library(meowParser meowParser.cpp)

if(MSVC)
    set(CMAKE_C_FLAGS /source-charset:utf-8)
    set(CMAKE_C_FLAGS /execution-charset:utf-8)
endif(MSVC)

set(CPACK_PROJECT_NAME ${PROJECT_NAME})
set(CPACK_PROJECT_VERSION ${PROJECT_VERSION})
include(CPack)