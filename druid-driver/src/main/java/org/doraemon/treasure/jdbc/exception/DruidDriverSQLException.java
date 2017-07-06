package org.doraemon.treasure.jdbc.exception;


import java.sql.SQLException;

public class DruidDriverSQLException extends SQLException {

    private static final long serialVersionUID = -3676866533440295330L;

    public DruidDriverSQLException() {}

    public DruidDriverSQLException(String message) {
        super(message);
    }

    public DruidDriverSQLException(Throwable ex) {
        super(ex);
    }

    public DruidDriverSQLException(String message, Throwable ex) {
        super(message, ex);
    }

    public DruidDriverSQLException(String message, Object... args) {
        super(String.format(message, args));
    }
}
