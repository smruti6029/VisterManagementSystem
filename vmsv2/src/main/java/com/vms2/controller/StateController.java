package com.vms2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vms2.dto.StateDTO;
import com.vms2.response.Response;
import com.vms2.serviceImp.StateService;

@RestController
@RequestMapping("/api/state/")
public class StateController {
	
	@Autowired
	private StateService stateService;
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllstates()
	{
		List<StateDTO> allstate = stateService.getAllstate();	
		return new ResponseEntity<>(new Response<>("Success",allstate,HttpStatus.OK.value()),HttpStatus.OK);
		
	}

}
