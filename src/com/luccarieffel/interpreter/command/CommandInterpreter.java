package com.luccarieffel.interpreter.command;

import com.luccarieffel.interpreter.Lexer;
import com.luccarieffel.interpreter.Parser;

import java.util.*;

public class CommandInterpreter {
    private static final Dictionary<String, String> variables = new Hashtable<>();

    public static void main(String[] args) {
        BaseCommands.init(variables);

        new Command("quit", 0, 0, null, "quits the program.");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            if (Objects.equals(input, "quit")) {
                break;
            }

            new Parser(new Lexer(input), variables).parse();
        }
    }
}
