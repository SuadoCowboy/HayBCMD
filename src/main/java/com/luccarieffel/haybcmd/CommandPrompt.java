package com.luccarieffel.haybcmd;

import com.luccarieffel.haybcmd.command.BaseCommands;
import com.luccarieffel.haybcmd.command.Command;
import com.luccarieffel.haybcmd.compatibility.Output;
import com.luccarieffel.haybcmd.parser.Parser;

import java.util.*;

public class CommandPrompt {
    private static final Dictionary<String, String> variables = new Hashtable<>();

    /**
     * Runs in a simple UI
     */
    protected static ConsoleUI runUI() {
        ConsoleUI console = new ConsoleUI(variables);
        console.setVisible(true);
        return console;
    }

    /**
     * Runs in command prompt
     */
    protected static void runCMD(Scanner scanner) {
        Output.init(System.out::print, System.out::println);

        while (true) {
            String input = scanner.nextLine();

            if (Objects.equals(input, "quit"))
                break;

            new Parser(new Lexer(input), variables).parse();
        }
    }

    /**
     * inits BaseCommands and creates a quit command with null run function
     */
    protected static void init() {
        BaseCommands.init(variables);
        new Command("quit", 0, 0, null, "quits the program.");
    }
}
