package com.app.exception;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

//import com.app.util.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException)
			throws IOException, ServletException {
//		  ErrorResponse errorResponse = new ErrorResponse("Access denied: You do not have permission to access this resource.", HttpStatus.FORBIDDEN.value());
//	        response.setStatus(HttpStatus.FORBIDDEN.value());
//	        response.setContentType("application/json");
//	        ObjectMapper mapper = new ObjectMapper();
//	        response.getWriter().write(mapper.writeValueAsString(errorResponse));
		
	}
}