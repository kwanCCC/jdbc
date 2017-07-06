package org.doraemon.treasure.jdbc.exception;

import java.sql.SQLException;

public class AbstractSQLException extends SQLException {
    /**
     * 
     */
    private static final long serialVersionUID = 6732951253357377781L;

    public AbstractSQLException() {

    }

    public AbstractSQLException(String message) {
        super(message);
    }

    public AbstractSQLException(String message, Object... args) {
        super(String.format(message, args));
    }
}
