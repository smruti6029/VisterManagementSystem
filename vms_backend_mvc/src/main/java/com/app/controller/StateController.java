package com.app.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.StateDto;
import com.app.response.Response;
import com.app.service.StateService;



@RestController
@RequestMapping("/api/state/")
public class StateController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private StateService stateService;
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllstates()
	{
		List<StateDto> allstate = stateService.getAllstate();	
		return new ResponseEntity<>(new Response<>("Success",allstate,HttpStatus.OK.value()),HttpStatus.OK);
		
	}
	
	
	
	
	

}
