package org.doraemon.treasure.grammer.parser.impl;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

import org.doraemon.treasure.grammer.parser.exceptions.SyntaxErrorException;
import org.doraemon.treasure.grammer.utils.ANTLRUtils;

public class SyntaxErrorListener extends BaseErrorListener {

    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
            int charPositionInLine, String msg, RecognitionException e) {
        String position = "line " + line + ", pos " + charPositionInLine;

        String tokenName = "";
        String hint = "";
        if (offendingSymbol != null && offendingSymbol instanceof Token && recognizer != null
                && recognizer instanceof Parser) {
            Token token = (Token) offendingSymbol;
            tokenName = token.getText();
            String fullText = ((Parser) recognizer).getTokenStream().getTokenSource()
                    .getInputStream().toString();
            hint = ANTLRUtils.underlineError(fullText, tokenName, line, charPositionInLine);
        }
        throw new SyntaxErrorException(position + " near " + tokenName + " : " + msg + "\n" + hint, e);
    }

}
