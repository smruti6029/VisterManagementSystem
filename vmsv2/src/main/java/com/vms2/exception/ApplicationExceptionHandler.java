package com.vms2.exception;



import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ApplicationExceptionHandler {

	
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String,String> handleInvalidArgument(MethodArgumentNotValidException ex){
		Map<String,String> errorMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error ->{
			
		errorMap.put(error.getField(), error.getDefaultMessage());	
			
		});
		return errorMap;
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(UsernameNotFoundException.class)
	public Map<String , String> handleBusinessException(UsernameNotFoundException ex){
		
		Map<String,String> errorMap = new HashMap<>();
		
		errorMap.put("error Message", ex.getMessage());
		
		return errorMap;
		
	}
	
	
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Map<String, String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex){
		
		Map<String, String> errorMap = new HashMap<>();
		
		String exceptionMessage = ex.getMessage();
		
		  if (exceptionMessage.contains("Cannot deserialize value of type `java.sql.Date`")) {
			  
	            errorMap.put("error", "Date of birth format should be of (dd-MM-yyyy)");
	        } else {
	            errorMap.put("error", "Invalid request payload format");
	        }
	        
	        return errorMap;

	}
	
	
	    @ResponseStatus(HttpStatus.BAD_GATEWAY)  
	    @ExceptionHandler(ConstraintViolationException.class)
	    
	    public Map<String, String> handleConstraintViolationException(ConstraintViolationException ex) {
	        Map<String, String> errorMap = new HashMap<>();
	        errorMap.put("error", "Constraint violation: " + ex.getMessage());
	        return errorMap;
	    }
	
	
	   
	
}
