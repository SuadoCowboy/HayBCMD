package com.luccarieffel.interpreter.command;

import com.luccarieffel.interpreter.compatibility.Output;

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
            Output.println("Unknown command \"" + name + "\"");
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
