package com.app.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.NotificationDTO;
import com.app.response.Response;
import com.app.service.NotificationService;

@RestController
@RequestMapping("/api/notification/")
public class NotificationController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private NotificationService notificationService;

//	@GetMapping("/pending-request")
//	public ResponseEntity<?> getByUser(@RequestParam Integer companyId, HttpServletRequest request) {
//		
//		if (request.getHeader("Authorization") == null) {
//			return new ResponseEntity<>(new Response<>("Not Authenticated", null, HttpStatus.UNAUTHORIZED.value()),
//					HttpStatus.UNAUTHORIZED);
//		}
//		Response<?> response = this.notificationService.getByUser(request, companyId);
//
//		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
//	}

	@GetMapping("/pending-request")
	public ResponseEntity<?> getByUser(@RequestParam(name = "companyId", required = false) Integer companyId,
			@RequestParam(name = "buildingId", required = false) Integer buildingId, HttpServletRequest request) {

		if (request.getHeader("Authorization") == null) {
			return new ResponseEntity<>(new Response<>("Not Authenticated", null, HttpStatus.UNAUTHORIZED.value()),
					HttpStatus.UNAUTHORIZED);
		}
		Response<?> response = this.notificationService.getByUser(request, companyId, buildingId);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PostMapping("/mark-seen")
	public ResponseEntity<?> markAsSeen(@RequestParam(name = "companyId", required = false) Integer companyId,
			@RequestParam(name = "buildingId", required = false) Integer buildingId, HttpServletRequest request)
			throws Exception {
		if (request.getHeader("Authorization") == null) {

			return new ResponseEntity<>(new Response<>("Not Authenticated", null, HttpStatus.UNAUTHORIZED.value()),
					HttpStatus.UNAUTHORIZED);
		}
		Response<?> response = this.notificationService.markAsSeen(request, companyId, buildingId);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}
}
