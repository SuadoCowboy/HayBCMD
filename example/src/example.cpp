#include <iostream>
#include <unordered_map>
#include <vector>

#include "HayBCMD.h"

static std::string outputLevelToString(const HayBCMD::OutputLevel &level) {
    switch (level) {
    case HayBCMD::OutputLevel::DEFAULT:
        return "DEFAULT";
    
    case HayBCMD::OutputLevel::ECHO:
        return "ECHO";
    
    case HayBCMD::OutputLevel::WARNING:
        return "WARNING";
    
    case HayBCMD::OutputLevel::ERROR:
        return "ERROR";
    };

    return "UNKNOWN";
}

static void print(HayBCMD::OutputLevel level, const std::string &string) {
    std::cout << outputLevelToString(level) << ": " << string;
}

int main()
{
    HayBCMD::Output::setPrintFunction(print);

    std::unordered_map<std::string, std::string> variables{};
    HayBCMD::BaseCommands::init(&variables);

    bool running = true;
    HayBCMD::Command("quit", 0, 0, [&](HayBCMD::Command*, const std::vector<std::string>&) {
        running = false;
    }, "- quits");

    std::string testCvar = "Happy Face :)";
    HayBCMD::CVARStorage::cvar("testCvar", &testCvar, "Happy face :)");

    while (running) {
        std::string input;
        std::getline(std::cin, input);

        HayBCMD::Output::printf(HayBCMD::ECHO, "{}\n", testCvar);

        HayBCMD::Lexer lexer = input;
        HayBCMD::Parser(&lexer, variables).parse();
        
        HayBCMD::handleLoopAliasesRunning(variables);
    }
}