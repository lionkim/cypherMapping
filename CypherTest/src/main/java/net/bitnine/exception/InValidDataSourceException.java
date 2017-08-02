package net.bitnine.exception;

import net.bitnine.util.messages.ErrorCodes;

public class InValidDataSourceException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String errorCode;
	private String message;
    
    public InValidDataSourceException() {
        this.message = ErrorCodes.INVALID_DATASOURCE_EXCEPTION;
    }
    
    public InValidDataSourceException (String message) {
        super (ErrorCodes.INVALID_DATASOURCE_EXCEPTION);
        this.message = message;
    }

    public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
