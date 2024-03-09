package com.luccarieffel.interpreter;

import com.luccarieffel.interpreter.command.Command;
import com.luccarieffel.interpreter.token.Token;
import com.luccarieffel.interpreter.token.TokenType;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Optional;

public class Parser {
    private final Lexer lexer;
    private Token currentToken;
    private final Dictionary<String, String> variables;

    public Parser(Lexer lexer, Dictionary<String, String> variables) {
        this.lexer = lexer;
        this.variables = variables;
        advance();
    }

    private void advance() {
        currentToken = lexer.nextToken();
    }

    public void parse() {
        while (currentToken.type() != TokenType.EOF) {

            if (variables.get(currentToken.value()) != null) {
                // TODO: fix stackoverflow issue with alias loops
                new Parser(new Lexer(variables.get(currentToken.value())), variables).parse();
                advance(); // skip possible end of statement
            }

            else if (currentToken.type() == TokenType.COMMAND) {
                String commandString = currentToken.value();

                Command command;
                {
                    Optional<Command> commandOptional = Command.getCommand(commandString);
                    if (commandOptional.isEmpty()) {
                        System.out.println("Unknown command \"" + commandString + "\"");
                        continue;
                    }

                    command = commandOptional.get();
                }

                advance();

                List<String> arguments = new ArrayList<>();
                while (currentToken.type() != TokenType.EOF && currentToken.type() != TokenType.EOS) {

                    if (currentToken.type() == TokenType.STRING)
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

                if (command.maxArgs() == 1 && !arguments.isEmpty()) { // make it include whitespaces in that case
                    StringBuilder stringBuilder = new StringBuilder();

                    for (String argument : arguments) {
                        stringBuilder.append(argument).append(" ");
                    }

                    stringBuilder.deleteCharAt(stringBuilder.length()-1);

                    arguments.clear();
                    arguments.add(stringBuilder.toString());
                }

                if (arguments.size() > command.maxArgs() || arguments.size() < command.minArgs()) {
                    Command.printUsage(command);
                    if (!arguments.isEmpty())
                        System.out.println("arguments size must be within range [" + command.minArgs() + "," + command.maxArgs() + "], but size is " + arguments.size());
                    continue;
                }

                command.run(arguments);
            }

            else {
                System.out.println("Unknown command \"" + currentToken.value() + "\"");
                return;
            }

            advance();
        }
    }
}
