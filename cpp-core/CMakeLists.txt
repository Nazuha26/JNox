cmake_minimum_required(VERSION 3.28)
project(WinNativeLib)

set(CMAKE_CXX_STANDARD 20)

find_package(JNI REQUIRED)

add_library(WinNativeLib SHARED WinNativeLib.cpp)

target_include_directories(WinNativeLib PRIVATE ${JNI_INCLUDE_DIRS})

target_link_libraries(WinNativeLib PRIVATE
        ${JNI_LIBRARIES}
        dwmapi.lib
        user32.lib
)


add_custom_command(TARGET WinNativeLib POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E copy_if_different
        "$<TARGET_FILE:WinNativeLib>"
        "${CMAKE_CURRENT_SOURCE_DIR}/../ui/src/main/resources/$<TARGET_FILE_NAME:WinNativeLib>"
        COMMENT "Copying WinNativeLib.dll to the ui module resources..."
)