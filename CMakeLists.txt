cmake_minimum_required(VERSION 3.5.0)
set(CMAKE_CXX_STANDARD 11)

project(HayBCMD VERSION 0.1.0 LANGUAGES CXX)

option(BUILD_EXAMPLE "Whether build the example together or not." FALSE)

add_library(HayBCMD HayBCMD.cpp)
target_include_directories(HayBCMD PRIVATE ${PROJECT_SOURCE_DIR}) # just the HayBCMD.h

if(BUILD_EXAMPLE)
add_subdirectory(${PROJECT_SOURCE_DIR}/example)
endif()

set(CPACK_PROJECT_NAME ${PROJECT_NAME})
set(CPACK_PROJECT_VERSION ${PROJECT_VERSION})
include(CPack)