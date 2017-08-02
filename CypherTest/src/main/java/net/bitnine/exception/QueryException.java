package net.bitnine.exception;

import java.sql.SQLException;

public class QueryException extends SQLException {

    private static final long serialVersionUID = 1L;
    private String status;
    private String message;
    
    public QueryException() { }

    public QueryException(String state, SQLException ex) {
        this.status = state;
        this.message = ex.getMessage();
    }

    public QueryException(String message) {
        super (message);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
