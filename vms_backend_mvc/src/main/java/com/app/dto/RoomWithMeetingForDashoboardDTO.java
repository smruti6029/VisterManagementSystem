package com.app.dto;

import java.util.Date;
import java.util.List;

import com.app.entity.Meeting;
import com.app.entity.Room;

public class RoomWithMeetingForDashoboardDTO{

	Integer roomId;

	String roomName;

	Boolean status;

	Integer capacity;

	List<MeetingDto> meetings;

	
	public Integer getRoomId() {
		return roomId;
	}


	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}


	public String getRoomName() {
		return roomName;
	}


	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}


	public Boolean getStatus() {
		return status;
	}


	public void setStatus(Boolean status) {
		this.status = status;
	}


	public Integer getCapacity() {
		return capacity;
	}


	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}


	public List<MeetingDto> getMeetings() {
		return meetings;
	}


	public void setMeetings(List<MeetingDto> meetings) {
		this.meetings = meetings;
	}


	public  static RoomWithMeetingForDashoboardDTO convertTODashboad(List<MeetingDto> meetings, Room room) {
		
		RoomWithMeetingForDashoboardDTO obj = new RoomWithMeetingForDashoboardDTO();
		if (meetings!=null) {
			obj.setMeetings(meetings);
		}

		obj.setRoomId(room.getId());
		obj.setRoomName(room.getRoomName());

		obj.setCapacity(room.getCapacity());
		obj.setStatus(room.getIsAvailable());
		return obj;

	}

}
