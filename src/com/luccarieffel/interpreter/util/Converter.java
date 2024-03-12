package com.luccarieffel.interpreter.util;

public class Converter {
    /**
     * Converts a string into a double if possible.<br>
     * <strong>This function should be used for commands that requires double(s), integer(s) and/or boolean(s).</strong>
     * @param string the one to check if it is a double
     * @return string as double or null if could not make it
     */
    public static Double convertToDouble(String string) {
        // MAYBE create a double that accepts big numbers?
        if (string.matches("^-?[0-9]*[.]?[0-9]+$"))
            return Double.parseDouble(string);

        return null;
    }
}
