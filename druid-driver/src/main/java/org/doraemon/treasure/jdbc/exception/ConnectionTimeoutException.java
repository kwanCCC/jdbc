package org.doraemon.treasure.jdbc.exception;


public class ConnectionTimeoutException extends DruidDriverSQLException {

    private static final long serialVersionUID = 6279557409667841166L;

    public ConnectionTimeoutException() {}

    public ConnectionTimeoutException(String message) {
        super(message);
    }

    public ConnectionTimeoutException(Throwable ex) {
        super(ex);
    }

    public ConnectionTimeoutException(String message, Throwable ex) {
        super(message, ex);
    }

    public ConnectionTimeoutException(String message, Object... args) {
        super(String.format(message, args));
    }

}
