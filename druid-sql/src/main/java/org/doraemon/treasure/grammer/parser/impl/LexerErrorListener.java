package org.doraemon.treasure.grammer.parser.impl;


import org.doraemon.treasure.grammer.parser.exceptions.LexicalErrorException;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import org.doraemon.treasure.grammer.utils.ANTLRUtils;

public class LexerErrorListener extends BaseErrorListener{

    // offendingSymbol is null when lexer error
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                            int charPositionInLine, String msg, RecognitionException e) {

        String position = "line " + line + ", pos " + charPositionInLine;

        String charText = "";
        String hint = "";
        if(recognizer != null && recognizer instanceof Lexer) {
            Lexer lexer = (Lexer) recognizer;
            String fullText = lexer.getInputStream().toString();
            charText = fullText.charAt(lexer.getCharIndex()) + "";
            hint = ANTLRUtils.underlineError(fullText, charText, line, charPositionInLine);
        }
        throw new LexicalErrorException(position + " near " + charText + " : " + msg + hint , e);
    }
}
