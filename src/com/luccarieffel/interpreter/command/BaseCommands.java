package com.luccarieffel.interpreter.command;

import com.luccarieffel.interpreter.compatibility.Output;

import java.util.*;

public class BaseCommands {
    private static Dictionary<String, String> variables;

    public static void init(Dictionary<String, String> variables) {
        BaseCommands.variables = variables;

        new Command("help", 0, 1, BaseCommands::help, "<command?> - shows a list of commands usages or the usage of a specific command");
        new Command("echo", 1, 1, BaseCommands::echo, "<message> - echoes a message to the console");
        new Command("alias", 1, 2, BaseCommands::alias, "<varName> <commands> - creates/deletes variables");
        new Command("variables", 0, 0, BaseCommands::variables, "- list of variables");
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
            Output.println("Variable name can not have whitespace.");
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

    // TODO: incrementvar command(maybe create it's own class)
}
