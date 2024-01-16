package com.app.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.VisitorMeetingDto;
import com.app.response.Response;
import com.app.service.VisitorMeetingService;

@RestController
@RequestMapping("/api/visitor-meeting")
public class VisitorMeetingController {

	@Autowired
	private VisitorMeetingService visitorMeetingService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

//	@Autowired
//	private VisitorService visitorService;
//
//	@Autowired
//	private MeetingService meetingService;

	@PostMapping("/save")
	public ResponseEntity<?> save(@RequestBody @Valid VisitorMeetingDto visitorMeetingDto) throws Exception {

		Response<?> response = this.visitorMeetingService.save(visitorMeetingDto);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));

	}

}
