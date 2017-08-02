package net.bitnine.exception;

public class TestException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String message;
    
    public TestException(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
