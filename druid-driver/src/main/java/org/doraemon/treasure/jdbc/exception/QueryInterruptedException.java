package org.doraemon.treasure.jdbc.exception;

public class QueryInterruptedException extends DruidDriverSQLException {

    public QueryInterruptedException() {}

    public QueryInterruptedException(String message) {
        super(message);
    }

    public QueryInterruptedException(Throwable ex) {
        super(ex);
    }

    public QueryInterruptedException(String message, Throwable ex) {
        super(message, ex);
    }

    public QueryInterruptedException(String message, Object... args) {
        super(String.format(message, args));
    }
}
