cmake_minimum_required(VERSION 3.0.0)
project(meowParserJNI VERSION 0.1.0)

include(CTest)
enable_testing()

add_library(meowParserJNI SHARED meowParserJNI.cpp)
include_directories("E:/Program Files/Java/jdk1.8.0_351/include" "E:/Program Files/Java/jdk1.8.0_351/include/win32")
target_link_libraries(meowParserJNI "E:/Workspace/IdeaProjects/creative-explorer-all/sdefaa-code-snippet/jni-snippet/cpp/meowParserJNI/meowParser.lib")

set(CPACK_PROJECT_NAME ${PROJECT_NAME})
set(CPACK_PROJECT_VERSION ${PROJECT_VERSION})
include(CPack)
