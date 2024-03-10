package com.luccarieffel.interpreter;


import com.luccarieffel.interpreter.command.Command;
import com.luccarieffel.interpreter.token.Token;
import com.luccarieffel.interpreter.token.TokenType;

import java.util.Objects;

public class Lexer {
    private final String input;
    private int position = 0;
    private Token lastToken;

    public Lexer(String input) {
        this.input = input;
    }

    public Token nextToken() {
        if (position >= input.length())
            return new Token(TokenType.EOF, "");

        char currentChar = input.charAt(position);
        while (Character.isWhitespace(currentChar)) {
            position++;
            currentChar = input.charAt(position);
        }

        if (input.charAt(position) == ';') {
            lastToken = null;
            position++;
            return new Token(TokenType.EOS, ";");
        }

        lastToken = parseToken();
        return lastToken;
    }

    public Token getLastToken() {
        return lastToken;
    }

    /**
     * checks which type the token is
     * @return Token with type ID or Token with type COMMAND
     */
    private Token parseToken() {
        if (input.charAt(position) == '"') // if it starts with double quotes
            return parseString();

        StringBuilder stringBuilder = new StringBuilder();

        while (position < input.length() && !Character.isWhitespace( input.charAt(position) ) && input.charAt(position) != ';') {
            stringBuilder.append(input.charAt(position));
            position++;
        }

        String tokenValue = stringBuilder.toString();

        if (isCommand(tokenValue) && (lastToken == null || lastToken.type() != TokenType.COMMAND))
            return new Token(TokenType.COMMAND, tokenValue);

        else if (isVariable(tokenValue))
            return new Token(TokenType.VARIABLE, tokenValue);

        else
            return new Token(TokenType.STRING, tokenValue);
    }

    private boolean isVariable(String identifier) {
        return identifier.startsWith("$");
    }

    private boolean isCommand(String commandName) {
        for (Command command : Command.getCommands())

            if (Objects.equals(command.name(), commandName))
                return true;

        return false;
    }

    private Token parseString() {
        StringBuilder builder = new StringBuilder();

        position++; // to skip the first double quotes
        while (position < input.length() && input.charAt(position) != '"') {

            // handle slashed double quotes
            if (input.charAt(position) == '\\')
                // skips the slash and then builder.append(...) will append the quotes or whatever the next character is and ignore the while condition
                position++;

            builder.append(input.charAt(position));
            position++;
        }

        position++; // to skip the last double quotes

        return new Token(TokenType.STRING, builder.toString());
    }
}
