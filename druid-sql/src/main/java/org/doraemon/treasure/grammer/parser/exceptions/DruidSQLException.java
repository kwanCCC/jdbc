package org.doraemon.treasure.grammer.parser.exceptions;


public class DruidSQLException extends RuntimeException {

    private static final long serialVersionUID = -9178671316416338557L;

    public DruidSQLException(String msg) {
        super(msg);
    }

    public DruidSQLException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
