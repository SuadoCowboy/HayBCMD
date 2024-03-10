package com.luccarieffel.interpreter.parser;

import com.luccarieffel.interpreter.Lexer;
import com.luccarieffel.interpreter.compatibility.Output;
import com.luccarieffel.interpreter.token.TokenType;

import java.util.Dictionary;

/**
 * Parses alias so that it can make loops without stackoverflow issue
 */
public class AliasParser extends Parser {
    private final int id;

    public AliasParser(Lexer lexer, Dictionary<String, String> variables) {
        super(lexer, variables, false);
        id = 0;
        parse();
    }

    private AliasParser(Lexer lexer, Dictionary<String, String> variables, int id) {
        super(lexer, variables, false);
        this.id = id;
        parse();
    }

    @Override
    public void parse() {
        Lexer tempLexer = null; // variable used to store the original lexer while the lexer from other aliases are being used

        while (currentToken.type() != TokenType.EOF) {
            if (getVariableAsCurrentTokenValue() != null) {
                if (id > 0) {
                    return;
                }

                if (tempLexer == null)
                    tempLexer = lexer;

                // fixing stackoverflow issue was easier than what I thought. My 11 year old self must be proud.
                lexer = new AliasParser(new Lexer(getVariableAsCurrentTokenValue()), variables, id+1).lexer;
                currentToken = lexer.getLastToken();

                continue;
            }

            else if (currentToken.type() == TokenType.COMMAND)
                handleCommandToken();

            else {
                Output.printUnknownCommand(currentToken.value());
                return;
            }

            advance();

            // this code is messy af
            while (currentToken.type() == TokenType.EOF && tempLexer != null) {
                lexer = tempLexer;
                tempLexer = null;
                advance();
            }
        }
    }
}
