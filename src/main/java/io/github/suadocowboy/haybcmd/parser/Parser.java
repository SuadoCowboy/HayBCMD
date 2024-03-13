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

package io.github.suadocowboy.haybcmd.parser;

import io.github.suadocowboy.haybcmd.Lexer;
import io.github.suadocowboy.haybcmd.command.Command;
import io.github.suadocowboy.haybcmd.compatibility.Output;
import io.github.suadocowboy.haybcmd.token.Token;
import io.github.suadocowboy.haybcmd.token.TokenType;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class Parser {
    protected Lexer lexer;
    protected Token currentToken;
    protected final Dictionary<String, String> variables;
    public boolean isAliasThreaded = true;

    public Parser(Lexer lexer, Dictionary<String, String> variables) {
        this.lexer = lexer;
        this.variables = variables;

        advance();
    }

    public Parser(Lexer lexer, Dictionary<String, String> variables, boolean isAliasThreaded) {
        this.isAliasThreaded = isAliasThreaded;
        this.variables = variables;
        this.lexer = lexer;

        advance();
    }

    protected void advance() {
        currentToken = lexer.nextToken();
    }

    /**
     * Advances until the currentToken.type is one of the tokenTypes
     * @param tokenTypes A list of token types to check if currentToken.type is equal to
     */
    protected void advanceUntil(List<TokenType> tokenTypes) {
        advance(); // always skip the first one

        // checks if EOF is reached because if not, it would run forever
        while (!tokenTypes.contains((currentToken.type())) && currentToken.type() != TokenType.EOF)
            advance();
    }

    /**
     * Gets string and variable tokens to a list of arguments.<br>
     * Might return an empty String list.
     * @return list of arguments
     */
    private List<String> getArguments() {
        List<String> arguments = new ArrayList<>();

        while (currentToken.type() != TokenType.EOF && currentToken.type() != TokenType.EOS) {

            // yes... it's also appending command type.
            if (currentToken.type() == TokenType.STRING || currentToken.type() == TokenType.COMMAND)
                arguments.add(currentToken.value());

            else if (currentToken.type() == TokenType.VARIABLE) {
                String variable = variables.get(currentToken.value().substring(1));

                if (variable != null)
                    arguments.add(variable);

                else // in case user is confused why his (unexistent)variable is not passing his value
                    arguments.add(currentToken.value());
            }

            advance();
        }

        return arguments;
    }

    protected String getVariableAsCurrentTokenValue() {
        return variables.get(currentToken.value());
    }

    protected void handleCommandToken() {
        String commandString = currentToken.value();

        Command command = Command.getCommand(commandString, true);
        if (command == null)
            return;

        advance(); // skips the command token

        List<String> arguments = getArguments();

        // make it include whitespaces in that case
        if (command.maxArgs() == 1 && !arguments.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();

            for (String argument : arguments) {
                stringBuilder.append(argument).append(" ");
            }

            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

            arguments.clear();
            arguments.add(stringBuilder.toString());
        }

        // checks if arguments size is within the allowed
        if (arguments.size() > command.maxArgs() || arguments.size() < command.minArgs()) {
            Command.printUsage(command);
            if (!arguments.isEmpty())
                Output.println("arguments size must be within range [" + command.minArgs() + "," + command.maxArgs() + "], but size is " + arguments.size());
            return;
        }

        command.run(arguments);
    }

    public void parse() {
        while (currentToken.type() != TokenType.EOF) {

            if (getVariableAsCurrentTokenValue() != null) {
                if (isAliasThreaded) {
                    Lexer aliasLexer = new Lexer(getVariableAsCurrentTokenValue());
                    new Thread(() -> new AliasParser(aliasLexer, variables)).start();
                }
                else
                    new AliasParser(new Lexer(getVariableAsCurrentTokenValue()), variables);

                advance(); // skip possible end of statement
            }

            else if (currentToken.type() == TokenType.COMMAND)
                handleCommandToken();

            else {
                Output.printUnknownCommand(currentToken.value());
                advanceUntil(List.of(TokenType.EOS));
            }

            advance();
        }
    }
}
