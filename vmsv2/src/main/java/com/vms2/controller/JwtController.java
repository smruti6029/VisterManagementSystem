package com.vms2.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vms2.response.JwtRequest;
import com.vms2.response.Response;
import com.vms2.service.UserDataService;

@RestController
public class JwtController {

	@Autowired
	private UserDataService dataService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> generateTokan(@RequestBody JwtRequest jwtRequest) throws Exception {
		System.out.println(jwtRequest);
		Response<?> token = null;
		try {
			token = dataService.generateToken(jwtRequest);
		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<>(token,
					HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(token, HttpStatus.OK);

	}

}
