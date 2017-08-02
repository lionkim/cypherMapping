package net.bitnine.exception;

public class QueryException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String message;
    
    public QueryException() { }
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
}
