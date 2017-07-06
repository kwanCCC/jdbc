package org.doraemon.treasure.jdbc.exception;


public class MetricStoreServerException extends RemoteServerException {
    public MetricStoreServerException() {}

    public MetricStoreServerException(String message) {
        super(message);
    }

    public MetricStoreServerException(Throwable ex) {
        super(ex);
    }

    public MetricStoreServerException(String message, Throwable ex) {
        super(message, ex);
    }

    public MetricStoreServerException(String message, Object... args) {
        super(String.format(message, args));
    }
}
