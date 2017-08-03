package net.bitnine.exception.handler;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import net.bitnine.exception.InValidDataSourceException;
import net.bitnine.exception.InvalidTokenException;
import net.bitnine.exception.QueryException;
import net.bitnine.exception.test.BaseException;
import net.bitnine.exception.test.TestException;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {


    @ExceptionHandler(value = { QueryException.class })
    @ResponseBody
    protected ErrorMessage handleQueryException(SQLException ex, QueryException queryException, WebRequest request) {
        ErrorMessage em = new ErrorMessage();
        em.setStatus ("failure");
        em.setErrorCode (queryException.getStatus());
        em.setMessage(ex.getMessage());
        return em;
    }
    



    @ExceptionHandler(value = { InValidDataSourceException.class })
    @ResponseBody
    protected ErrorMessage handleInValidDataSourceException(SQLException ex, InValidDataSourceException inValidDataSourceException, WebRequest request) {
        ErrorMessage em = new ErrorMessage();
        em.setStatus ("failure");
        em.setErrorCode (inValidDataSourceException.getErrorCode());
        em.setMessage(inValidDataSourceException.getMessage());
        return em;
    }
   
    
    @ExceptionHandler(value = { InvalidTokenException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED )
    @ResponseBody
    protected ErrorMessage handleInvalidTokenException(RuntimeException ex, WebRequest request) {
        ErrorMessage em = new ErrorMessage();
        em.setStatus ("failure");
        em.setErrorCode (HttpStatus.UNAUTHORIZED.toString());
        em.setMessage(ex.getMessage());
        return em;
    }
    
    

    
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ExceptionHandler (value = BaseException.class)
    public String handleBaseException (BaseException e) {
        return e.getMessage();        
    }
    
    @ExceptionHandler (value = Exception.class)
    public String handleException (Exception e) {
        return e.getMessage();
    }


    @ExceptionHandler(value = { TestException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    protected ErrorMessage handleConflict(RuntimeException ex, WebRequest request) {
        ErrorMessage em = new ErrorMessage();
        em.setStatus ("failure");
        em.setErrorCode (HttpStatus.INTERNAL_SERVER_ERROR.toString());
        em.setMessage(ex.getMessage());
        return em;
    }
}


class ErrorMessage {
	private String status;
    private String errorCode;
    private String message;
    
	public ErrorMessage() {
		this.status = "failure";		// 기본값으로 failure
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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