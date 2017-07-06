package org.doraemon.treasure.jdbc.misc;


import lombok.Getter;
import lombok.Setter;

public class HttpUtilException extends RuntimeException {
    private static final long serialVersionUID = 1624484672851389688L;

    @Getter
    private int statusCode;

    @Getter
    private String responseBody;

    public HttpUtilException(int statusCode, String responseBody) {
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public HttpUtilException(int statusCode, String responseBody, String message) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public HttpUtilException(int statusCode, String responseBody, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public HttpUtilException(int statusCode, String responseBody, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }
}
