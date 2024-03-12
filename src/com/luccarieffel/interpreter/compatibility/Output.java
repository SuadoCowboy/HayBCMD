package com.luccarieffel.interpreter.compatibility;

public class Output {
    private static IPrint printMethod;
    private static IPrintln printlnMethod;

    public static void init(IPrint print, IPrintln println) {
        Output.printMethod = print;
        Output.printlnMethod = println;
    }

    public static void printUnknownCommand(String command) {
        Output.println("unknown command \"" + command + "\"");
    }

    public static void print(String string) {
        printMethod.print(string);
    }

    public static void println(String string) {
        printlnMethod.println(string);
    }
}
