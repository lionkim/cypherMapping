package net.bitnine.exception;

import net.bitnine.util.messages.ErrorCodes;

public class InvalidTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String message;
    
    public InvalidTokenException() {
        this.message = ErrorCodes.INVALID_TOKEN_EXCEPTION;
    }
    
    public InvalidTokenException(String message) {
        super (ErrorCodes.INVALID_TOKEN_EXCEPTION);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
