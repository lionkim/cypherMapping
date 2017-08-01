package net.bitnine.exception;

public class InvalidTokenException extends Exception {

    public InvalidTokenException() { }
    public InvalidTokenException(String message) {
        super (message);
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
