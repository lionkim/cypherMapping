package net.bitnine.exception;

import java.sql.SQLException;

public class QueryException extends SQLException {
    public QueryException() { }
    public QueryException(String message) {
        super (message);
    }
    public QueryException(String message, String string) {
        super (message, string);
    }
}
