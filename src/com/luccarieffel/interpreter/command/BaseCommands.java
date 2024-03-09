package com.luccarieffel.interpreter.command;

import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

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
            Command.getCommand(args.get(0)).ifPresentOrElse(
                    Command::printUsage,
                    () -> System.out.println("Unknown command \"" + args.get(0) + "\"")
            );
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

        System.out.println(stringBuilder);
    }

    /**
     * sets or deletes a variable
     */
    protected static void alias(String name, int minArgs, int maxArgs, String usage, List<String> args) {
        if (args.size() == 2) {
            if (Command.getCommand(args.get(0)).isPresent()) {
                System.out.println("varName is a command name, therefore this variable can not be created");
                return;
            }

            variables.put(args.get(0), args.get(1));
            return;
        }

        variables.remove(args.get(0));
    }

    protected static void variables(String name, int minArgs, int maxArgs, String usage, List<String> args) {
        StringBuilder stringBuilder = new StringBuilder();

        Iterator<String> it = variables.keys().asIterator();
        while (it.hasNext()) {
            String key = it.next();
            stringBuilder.append(key).append(" = \"").append(variables.get(key)).append("\"");
        }

        System.out.println(stringBuilder);
    }

    // TODO: incrementvar command(maybe create it's own class)
}
