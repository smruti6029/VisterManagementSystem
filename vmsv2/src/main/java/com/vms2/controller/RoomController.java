package com.vms2.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vms2.dto.IdIsactiveDTO;
import com.vms2.dto.RoomDTO;
import com.vms2.response.Response;
import com.vms2.service.RoomService;

@RestController
@RequestMapping("/api/room/")
public class RoomController {

	@Autowired
	private RoomService roomService;

	@PostMapping("/add")
	public ResponseEntity<?> addRomm(@RequestBody RoomDTO roomDTO) {
		Response<?> addRoom = roomService.addRoom(roomDTO);
		if (addRoom.getStatus() == HttpStatus.OK.value()) {
			return new ResponseEntity<>(addRoom, HttpStatus.OK);
		}

		return ResponseEntity.badRequest().body(addRoom);
	}


//	public Room getByStatus(IdIsactiveDTO idIsactiveDTO);

//	public Room delete(IdIsactiveDTO idIsactiveDTO);
//	
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<?> getRoombyId(@PathVariable Integer id) {
		Response<?> room = roomService.geteroomByid(id);
		
		if(room!=null)
		{
		if (room.getStatus()== HttpStatus.OK.value()) {
			return new ResponseEntity<>(room, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>(new Response<>("No Room Found", null, HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);
		}
		}
		return new ResponseEntity<>(new Response<>("No Room Found", null, HttpStatus.BAD_REQUEST.value()),
				HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getall")
	public ResponseEntity<?> getAllroom() {

		Response<?> allRooms = roomService.getAllRooms();
		
		if (allRooms.getStatus()== HttpStatus.OK.value()) {
			return new ResponseEntity<>(allRooms, HttpStatus.OK);
		}
		return new ResponseEntity<>(allRooms,HttpStatus.BAD_REQUEST);
	}
		
	@PostMapping("/delete")
	public ResponseEntity<?> deleteRoom(@RequestBody IdIsactiveDTO idIsactiveDTO)
	{
		Response<?> deleteRoom = roomService.deleteRoom(idIsactiveDTO);
		if (deleteRoom.getStatus()== HttpStatus.OK.value()) {
			return new ResponseEntity<>(deleteRoom, HttpStatus.OK);
		}
		return new ResponseEntity<>(deleteRoom,HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("/updateStatus")
	public ResponseEntity<?> updateRoom(@RequestBody IdIsactiveDTO idIsactiveDTO)
	{
		Response<?> upadateStatus = roomService.upadateStatus(idIsactiveDTO);
		return ResponseEntity.ok(upadateStatus);
	}
	
	
	
	
		
	

}
