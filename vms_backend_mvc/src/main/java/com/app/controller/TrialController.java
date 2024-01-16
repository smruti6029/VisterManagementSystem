package com.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.response.Response;
import com.app.service.TrialService;

@RestController
@RequestMapping("/api/trial")
public class TrialController {
	
	@Autowired
	private TrialService trialService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("/room/data")
	public ResponseEntity<?> getRoomTrialByMeetingId(@RequestParam(name = "meetingId", required = true) Integer meetingId) {
		Response<?> response = trialService.getRoomTrialDataByMeetingId(meetingId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}
}
