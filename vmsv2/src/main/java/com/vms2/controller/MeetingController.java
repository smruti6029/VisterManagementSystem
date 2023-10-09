package com.vms2.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vms2.authorize.Authorization;
import com.vms2.dto.MeetingDTO;
import com.vms2.dto.VisitormeetingDTO;
import com.vms2.response.Response;
import com.vms2.service.MeetingService;

@RestController
@RequestMapping("/api/meeting/")
public class MeetingController {

	@Autowired
	private MeetingService meetingService;

	@Autowired
	private Authorization authorization;

	@GetMapping("/")
	public ResponseEntity<?> getMeetingWithStatus(@RequestParam("id") Integer id) {
		Response<?> meetings = meetingService.getmeetingWithstatusByuser(id);
		if (meetings.getStatus() == HttpStatus.OK.value()) {

			return ResponseEntity.ok(meetings);
		}
		return new ResponseEntity<>(meetings, HttpStatus.NO_CONTENT);
	}

	@GetMapping("/getall")
	public ResponseEntity<?> getallMeetingWithStatus() {
		Response<?> meetings = meetingService.getallmeetingWithstatus();
		if (meetings.getStatus() == HttpStatus.OK.value()) {
			return ResponseEntity.ok(meetings);
		}
		return new ResponseEntity<>(meetings, HttpStatus.NO_CONTENT);

	}

	@GetMapping("/getbyid")
	public ResponseEntity<?> getMeeetingByid(@RequestParam int id) {

		return null;
	}

	@PostMapping("/update/visitor")
	public ResponseEntity<?> updateVisitor(@RequestBody MeetingDTO meetingDTO, HttpServletRequest request) {

		String header = request.getHeader("Authorization");
		if (header == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize", null, HttpStatus.UNAUTHORIZED.value())),
					HttpStatus.UNAUTHORIZED);
		}

		Response<?> authorizetoAdduser = authorization.authorizetoAdduser(header.substring(7));
		if (authorizetoAdduser.getStatus() != HttpStatus.OK.value()) {
			return ResponseEntity.badRequest().body(authorizetoAdduser);
		}

		Response<?> updatemeetingStatus = meetingService.updatemeetingStatus(meetingDTO);
		if (updatemeetingStatus.getStatus() == HttpStatus.OK.value()) {
			return ResponseEntity.ok(updatemeetingStatus);
		}
		return ResponseEntity.badRequest().body(updatemeetingStatus);

	}

}
