package org.doraemon.treasure.jdbc.exception;


public class MetricStoreBadRequestException extends MetricStoreServerException {
    public MetricStoreBadRequestException() {}

    public MetricStoreBadRequestException(String message) {
        super(message);
    }

    public MetricStoreBadRequestException(Throwable ex) {
        super(ex);
    }

    public MetricStoreBadRequestException(String message, Throwable ex) {
        super(message, ex);
    }

    public MetricStoreBadRequestException(String message, Object... args) {
        super(String.format(message, args));
    }
}
