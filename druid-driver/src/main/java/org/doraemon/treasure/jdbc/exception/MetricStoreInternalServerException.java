package org.doraemon.treasure.jdbc.exception;


public class MetricStoreInternalServerException extends MetricStoreServerException {

    public MetricStoreInternalServerException() {}

    public MetricStoreInternalServerException(String message) {
        super(message);
    }

    public MetricStoreInternalServerException(Throwable ex) {
        super(ex);
    }

    public MetricStoreInternalServerException(String message, Throwable ex) {
        super(message, ex);
    }

    public MetricStoreInternalServerException(String message, Object... args) {
        super(String.format(message, args));
    }
}
