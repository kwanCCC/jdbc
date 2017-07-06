package org.doraemon.treasure.jdbc.exception;

public class UrlParserException extends AbstractSQLException {

    /**
     * 
     */
    private static final long serialVersionUID = -6084856066597286067L;

    public UrlParserException(String message, Object... args) {
        super(message, args);
    }

}
