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
import io.github.suadocowboy.haybcmd.compatibility.Output;
import io.github.suadocowboy.haybcmd.token.TokenType;

import java.util.Dictionary;
import java.util.List;

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
                advanceUntil(List.of(TokenType.EOS));
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
