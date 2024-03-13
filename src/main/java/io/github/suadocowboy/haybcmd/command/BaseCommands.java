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
import io.github.suadocowboy.haybcmd.util.Converter;

import java.util.*;

public class BaseCommands {
    private static Dictionary<String, String> variables;

    public static void init(Dictionary<String, String> variables) {
        BaseCommands.variables = variables;

        new Command("help", 0, 1, BaseCommands::help, "<command?> - shows a list of commands usages or the usage of a specific command");
        new Command("echo", 1, 1, BaseCommands::echo, "<message> - echoes a message to the console");
        new Command("alias", 1, 2, BaseCommands::alias, "<var> <commands?> - creates/deletes variables");
        new Command("variables", 0, 0, BaseCommands::variables, "- list of variables");
        new Command("variable", 1, 1, BaseCommands::variable, "- shows variable value");
        new Command("incrementvar", 4, 4, BaseCommands::incrementvar, "<var> <minValue> <maxValue> <delta> - increments the value of a variable");
    }

    protected static void help(String name, int minArgs, int maxArgs, String usage, List<String> args) {
        if (args.size() == 1)
        {
            Command command = Command.getCommand(args.get(0), true);
            if (command != null)
                Command.printUsage(command);

            return;
        }

        for (Command command : Command.getCommands()) {
            Command.printUsage(command);
        }
    }

    protected static void echo(String name, int minArgs, int maxArgs, String usage, List<String> args) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String arg : args) {
            stringBuilder.append(arg);
        }

        Output.println(stringBuilder.toString());
    }

    /**
     * sets or deletes a variable
     */
    protected static void alias(String name, int minArgs, int maxArgs, String usage, List<String> args) {
        if (args.size() == 1) {
            variables.remove(args.get(0));
            return;
        }

        if (Command.getCommand(args.get(0), false) != null) {
            Output.println("varName is a command name, therefore this variable can not be created");
            return;
        }

        // \S = any non-whitespace characters
        if (!args.get(0).matches("\\S+")) {
            Output.println("variable name can not have whitespace.");
            return;
        }

        variables.put(args.get(0), args.get(1));
    }

    protected static void variables(String name, int minArgs, int maxArgs, String usage, List<String> args) {
        StringBuilder stringBuilder = new StringBuilder();

        Iterator<String> it = variables.keys().asIterator();
        while (it.hasNext()) {
            String key = it.next();
            stringBuilder.append(key).append(" = \"").append(variables.get(key)).append("\"").append("\n");
        }

        Output.println("amount of variables: " + Arrays.stream(stringBuilder.toString().split("\n")).filter((s) -> !Objects.equals(s, "")).count());
        if (!stringBuilder.isEmpty())
            Output.println(stringBuilder.deleteCharAt(stringBuilder.length()-1).toString());
    }

    protected static void variable(String name, int minArgs, int maxArgs, String usage, List<String> args) {
        String key = args.get(0);
        String value = variables.get(key);

        if (value == null) {
            Output.println("variable \"" + key + "\" does not exist");
            return;
        }

        Output.println(key + " = \"" + value + "\"\n");
    }

    /**
     * increments an already existing variable
     */
    protected static void incrementvar(String name, int minArgs, int maxArgs, String usage, List<String> args) {
        String variable = args.get(0);
        Double minValue = Converter.convertToDouble(args.get(1));
        Double maxValue = Converter.convertToDouble(args.get(2));
        Double delta = Converter.convertToDouble(args.get(3));

        if (minValue == null || maxValue == null || delta == null) {
            Output.println("one of the variables is not a number");
            return;
        }

        if (minValue > maxValue) {
            Output.println("minValue is higher than maxValue");
        }

        String variableValue = variables.get(variable);
        if (variableValue == null) {
            Output.println("unknown variable \"" + variable + "\"");
            return;
        }

        Double variableValueAsDouble = Converter.convertToDouble(variableValue);
        if (variableValueAsDouble == null) {
            Output.println("variable value \"" + variableValue + "\" is not a number");
            return;
        }

        variableValueAsDouble += delta;
        if (variableValueAsDouble > maxValue)
            variableValueAsDouble = minValue;

        else if (variableValueAsDouble < minValue)
            variableValueAsDouble = maxValue;

        variables.put(variable, Double.toString(variableValueAsDouble));
    }
}
