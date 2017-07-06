package org.doraemon.treasure.jdbc.exception;

public class UrlEmptyException extends AbstractSQLException {
    
    /**
     * 
     */
    private static final long serialVersionUID = -5874727195518405195L;

    
    public UrlEmptyException(String message, Object... args) {
        super(message, args);
    }

}
