package com.luccarieffel.interpreter;

import com.luccarieffel.interpreter.command.BaseCommands;
import com.luccarieffel.interpreter.command.Command;
import com.luccarieffel.interpreter.compatibility.Output;
import com.luccarieffel.interpreter.parser.Parser;

import java.util.*;

public class Interpreter {
    private static final Dictionary<String, String> variables = new Hashtable<>();

    public static void main(String[] args) {
        BaseCommands.init(variables);
        Output.init(System.out::print, System.out::println);

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
