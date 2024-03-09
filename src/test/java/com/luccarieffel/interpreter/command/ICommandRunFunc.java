package com.luccarieffel.interpreter.command;

import java.util.List;

@FunctionalInterface
public interface ICommandRunFunc {
    void run(String name, int minArgs, int maxArgs, String usage, List<String> args);
}
