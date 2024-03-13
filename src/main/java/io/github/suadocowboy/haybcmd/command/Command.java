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

package io.github.suadocowboy.haybcmd.command;

import io.github.suadocowboy.haybcmd.compatibility.Output;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Command(String name, int minArgs, int maxArgs, ICommandRunFunc runFunc, String usage) {
    private static final List<Command> commands = new ArrayList<>();

    private static void addCommand(Command command) {
        for (Command c : commands)
            if (Objects.equals(c.name, command.name))
                throw new RuntimeException("Command with name \"" + command.name + "\" already exists");

        commands.add(command);
    }

    public static Command getCommand(String name, boolean printError) {
        for (Command command : commands)
            if (Objects.equals(command.name, name))
                return command;

        if (printError)
            Output.println("unknown command \"" + name + "\"");
        return null;

    }

    public static List<Command> getCommands() {
        return commands;
    }

    public static void printUsage(Command command) {
        Output.println(command.name() + " " + command.usage());
    }

    public Command(String name, int minArgs, int maxArgs, ICommandRunFunc runFunc, String usage) {
        this.name = name;
        this.maxArgs = maxArgs;
        this.minArgs = minArgs;
        this.runFunc = runFunc;
        this.usage = usage;

        addCommand(this);
    }

    public void run(List<String> args) {
        runFunc.run(name, minArgs, maxArgs, usage, args);
    }
}
