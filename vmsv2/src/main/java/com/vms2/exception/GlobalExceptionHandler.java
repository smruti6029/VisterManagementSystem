package com.vms2.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;




@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex){
		
		 ErrorResponse errorResponse = new ErrorResponse(" Username not found. !! ", 404);

	        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
		
		
	}
	
	
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex){
		
		ErrorResponse errorResponse = new ErrorResponse("Invalid password.", 401);
		
		
		  return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
		
		
	}
	

}
