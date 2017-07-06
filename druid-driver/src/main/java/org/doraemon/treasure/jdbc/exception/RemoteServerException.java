package org.doraemon.treasure.jdbc.exception;


public class RemoteServerException extends DruidDriverSQLException {

    public RemoteServerException() {}

    public RemoteServerException(String message) {
        super(message);
    }

    public RemoteServerException(Throwable ex) {
        super(ex);
    }

    public RemoteServerException(String message, Throwable ex) {
        super(message, ex);
    }

    public RemoteServerException(String message, Object... args) {
        super(String.format(message, args));
    }
}
