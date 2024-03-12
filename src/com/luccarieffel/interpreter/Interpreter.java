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
        new Command("quit", 0, 0, null, "quits the program.");

        if (args.length == 0) {
            new ConsoleUI(variables).setVisible(true);
            return;
        }

        if (!Objects.equals(args[0], "--console") && !Objects.equals(args[0], "-c"))
            return;

        Output.init(System.out::print, System.out::println);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();

            if (Objects.equals(input, "quit"))
                break;

            new Parser(new Lexer(input), variables).parse();
        }
    }
}
