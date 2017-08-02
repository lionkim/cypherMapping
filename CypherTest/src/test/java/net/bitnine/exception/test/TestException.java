package net.bitnine.exception.test;

public class TestException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String message;
    
    public TestException(String message) {
        super(message);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
