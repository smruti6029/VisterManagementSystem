package com.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.response.JwtRequest;
import com.app.response.JwtResponse;
import com.app.service.UserDataService;


@RestController
public class JwtController {
	
	@Autowired
	private UserDataService dataService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@RequestMapping(value = "/token", method = RequestMethod.POST)
	public ResponseEntity<?> generateTokan(@RequestBody JwtRequest jwtRequest) throws Exception
	{
		String username=null;
		
		
		try {
		 username = jwtRequest.getUsername().replaceAll("\\s+","").substring(0, 10);
		}catch (Exception e) {
			return new ResponseEntity<String>(" please provied valid Phone Number !!  ",HttpStatus.BAD_REQUEST);
		}
		
		jwtRequest.setUsername(username);
		
		System.out.println(jwtRequest);
		JwtResponse token=null;
		try
		{
		 token = dataService.generateToken(jwtRequest);
		}
		catch (Exception e) {
			
			e.printStackTrace();
			return new ResponseEntity<String>(" Incorrect password or User !!  ",HttpStatus.BAD_REQUEST);
		}
		
		
		return new ResponseEntity<JwtResponse>(token,HttpStatus.OK);	
		
		
	}
	

}
