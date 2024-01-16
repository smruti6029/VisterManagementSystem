package com.app.controller;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import com.app.authorize.Authorization;
import com.app.dto.CustomResponseDTO;
import com.app.dto.IsActiveDto;
import com.app.dto.RoomDto;
import com.app.entity.User;
import com.app.response.Response;
import com.app.service.RoomService;

@RestController
@RequestMapping("/api/room")
public class RoomController {

	@Autowired
	private RoomService roomService;

	@Autowired
	private Authorization authorization;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

//	@PostMapping("/save")
//	public ResponseEntity<?> save(@RequestBody @Valid RoomDto roomDto) {
//
//		logger.info("I am The King");
//
//		roomDto.getRoomName().trim();
//		Response<?> response = this.roomService.save(roomDto);
//		if (response.getStatus() == HttpStatus.OK.value()) {
//			System.out.println("Enter in this method");
//			return new ResponseEntity<>(response, HttpStatus.OK);
//		}
//		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
//	}

	@PostMapping("/save")
	public ResponseEntity<?> save(@RequestBody @Valid RoomDto roomDto) {
		if (roomDto.getRoomName() != null && !roomDto.getRoomName().equals("")) {
			roomDto.getRoomName().trim();
		}
		if (roomDto.getCompany() == null || roomDto.getCompany().getId() == null) {

			return new ResponseEntity<>(
					(new Response<>("Company ID is Requried", null, HttpStatus.BAD_REQUEST.value())),
					HttpStatus.BAD_REQUEST);

		}
		Response<?> response = this.roomService.save(roomDto);
		if (response.getStatus() == HttpStatus.OK.value()) {
			System.out.println("Enter in this method");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

//	@GetMapping("/all")
//	public ResponseEntity<?> getAll(@RequestParam Integer id,
//			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") Date meetingStartDate,
//			@RequestParam Integer duration) {
//
//		Response<?> response = this.roomService.getAll(id, meetingStartDate, duration);
//	
//		if (response.getStatus() == HttpStatus.OK.value()) {
//			return new ResponseEntity<>(response, HttpStatus.OK);
//		}
//		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
//	}

	@GetMapping("/all")
	public ResponseEntity<?> getAll(@RequestParam Integer id) {

		// You can set the default time zone to UTC for the entire application
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		Response<?> response = this.roomService.getAll(id);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PostMapping("/update")
	public ResponseEntity<?> update(@RequestBody @Valid RoomDto roomDto) {
		roomDto.getRoomName().trim();
		Response<?> response = this.roomService.update(RoomDto.toRoom(roomDto));
		if (response.getStatus() == HttpStatus.OK.value()) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/allbuilding")
	public ResponseEntity<?> getAll(@RequestParam(required = false) Integer id,
			@RequestParam(required = false) Integer buildingId, HttpServletRequest request) {

		String header = request.getHeader("Authorization");
		if (header != null) {
			User checkRole = authorization.checkRole(header.substring(7));
			if (checkRole != null) {

				Response<?> room = roomService.getAll2(checkRole.getCompany().getId(), buildingId);
				if (room.getStatus() == HttpStatus.OK.value()) {
					return ResponseEntity.ok(room);
				}

				return new ResponseEntity<>(room, HttpStatus.NO_CONTENT);
			}
		}

		// You can set the default time zone to UTC for the entire application
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		Response<?> response = this.roomService.getAll2(id, buildingId);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/getroomfordashboard")
	public ResponseEntity<?> getRoomWithDetalisForDashboard(@RequestParam(required = false) Integer companyId,
			@RequestParam Integer buildingId) {

		Response<?> response = roomService.getRoomWithMeetingsfordashboard2(companyId, buildingId);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

//	@GetMapping("/getroomfordashboard")
//	public ResponseEntity<?> getRoomWithDetalisForDashboard(@RequestParam Integer companyId) {
//
//		Response<?> response = roomService.getRoomWithMeetingsfordashboard(companyId);
//
//		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
//	}

	@PostMapping("/delete")
	public ResponseEntity<?> deleteRoomByID(@RequestParam Integer roomId) {

		Response<?> response = roomService.deleteRoomByID(roomId);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));

	}

	@PostMapping("/isActive")
	public ResponseEntity<?> isActiveRoom(@RequestBody IsActiveDto activeDto) {

		boolean activate = activeDto.getIsActive();
		String status = null;
		if (activate == true) {
			status = "Activated";
		} else {
			status = "Deactivated";
		}

		Response<?> response = roomService.delete(activeDto);

		if (response.getStatus() == 200) {
			return new ResponseEntity<>(new CustomResponseDTO("Room " + status, HttpStatus.OK.value()),
					HttpStatus.valueOf(response.getStatus()));
		}
		return new ResponseEntity<>(new CustomResponseDTO(response.getMessage(), response.getStatus()),
				HttpStatus.valueOf(response.getStatus()));
	}

}
