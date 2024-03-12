package com.luccarieffel.haybcmd.token;

public enum TokenType {
    VARIABLE,
    /**
     * A String is just a text that can be stored in a variable or used in a command. It can be created using double quotes to include whitespaces or without double quotes to not include whitespaces
     */
    STRING,
    COMMAND,
    /**
     * EOF - End Of File, it's used to check whether the end of a file had been reached.
     */
    EOF,
    /**
     * EOS - End Of Statement, it's used to check whether the end of a statement had been reached.
     */
    EOS
}
