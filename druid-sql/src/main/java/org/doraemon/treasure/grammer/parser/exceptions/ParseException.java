package org.doraemon.treasure.grammer.parser.exceptions;


public class ParseException extends DruidSQLException {

    private static final long serialVersionUID = 1134808507717143482L;

    public ParseException(String msg) {
        super(msg);
    }

    public ParseException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
