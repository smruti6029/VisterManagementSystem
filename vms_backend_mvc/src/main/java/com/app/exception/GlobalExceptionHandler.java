package com.app.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalExceptionHandler {
	
//	@ExceptionHandler(UsernameNotFoundException.class)
//	public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex){
//		
//		 ErrorResponse errorResponse = new ErrorResponse(" Username not found. !! ", 404);
//
//	        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
//		
//		
//	}
//	
//	
//	@ExceptionHandler(BadCredentialsException.class)
//	public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex){
//		
//		ErrorResponse errorResponse = new ErrorResponse("Invalid password.", 401);
//		
//		
//		  return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//		
//		
//	}
//	

}
