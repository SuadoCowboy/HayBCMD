/*
 * MIT License
 *
 * Copyright (c) 2024 Lucca Rieffel Silva, also as Suado Cowboy
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the “Software”),
 * to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.suadocowboy.haybcmd;

import io.github.suadocowboy.haybcmd.command.BaseCommands;
import io.github.suadocowboy.haybcmd.command.Command;
import io.github.suadocowboy.haybcmd.compatibility.Output;
import io.github.suadocowboy.haybcmd.parser.Parser;

import java.util.*;

public class CommandPrompt {
    private static final Dictionary<String, String> variables = new Hashtable<>();

    /**
     * Runs in a simple UI
     */
    private static void runUI() {
        new ConsoleUI(variables).setVisible(true);
    }

    /**
     * Runs in command prompt
     */
    private static void runCMD(Scanner scanner) {
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
    private static void init() {
        BaseCommands.init(variables);
        new Command("quit", 0, 0, null, "quits the program.");
    }

    public static void main(String[] args) {
        init();

        if (args.length == 0) {
            runUI();
            return;
        }

        if (!Objects.equals(args[0], "-c") && !Objects.equals(args[0], "--console")) {
            System.out.println("either put the first argument as \"-c\", \"--console\" for cmd input/output, or empty for a user interface");
            return;
        }

        runCMD(new Scanner(System.in));
    }
}
