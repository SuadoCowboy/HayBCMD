package com.luccarieffel.interpreter.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record Command(String name, int minArgs, int maxArgs, ICommandRunFunc runFunc, String usage) {
    private static final List<Command> commands = new ArrayList<>();

    private static void addCommand(Command command) {
        for (Command c : commands)
            if (Objects.equals(c.name, command.name))
                throw new RuntimeException("Command with name \"" + command.name + "\" already exists");

        commands.add(command);
    }

    public static Optional<Command> getCommand(String name) {
        for (Command command : commands)
            if (Objects.equals(command.name, name))
                return Optional.of(command);

        return Optional.empty();

    }

    public static List<Command> getCommands() {
        return commands;
    }

    public static void printUsage(Command command) {
        System.out.println(command.name() + " " + command.usage());
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
