package org.doraemon.treasure.grammer.parser.impl;


import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;

public class ThrowingExceptionErrorStrategy extends DefaultErrorStrategy {

    /**
     * Instead of recovering from exception e, rethrow it wrapped in a generic RuntimeException so
     * it is not caught by the rule function catches. Exception e is the "cause" of the
     * RuntimeException.
     */
    @Override
    public void recover(Parser recognizer, RecognitionException e) {
        throw e;
    }

    /**
     * Make sure we don't attempt to recover inline; if the parser successfully recovers, it won't
     * throw an exception.
     */
    @Override
    public Token recoverInline(Parser recognizer) throws RecognitionException {
        throw new InputMismatchException(recognizer);
    }

    /** Make sure we don't attempt to recover from problems in subrules. */
    @Override
    public void sync(Parser recognizer) {}
}