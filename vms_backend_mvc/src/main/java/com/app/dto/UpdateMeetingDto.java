package com.app.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.app.emun.MeetingStatus;

public class UpdateMeetingDto {

	private Integer id;
	
	private MeetingStatus status;

	private RoomDto room;

	private UserDto employee;

	private VisitorDto visitor;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MeetingStatus getStatus() {
		return status;
	}

	public void setStatus(MeetingStatus status) {
		this.status = status;
	}

	public UserDto getEmployee() {
		return employee;
	}

	public void setEmployee(UserDto employee) {
		this.employee = employee;
	}

	public VisitorDto getVisitor() {
		return visitor;
	}

	public void setVisitor(VisitorDto visitor) {
		this.visitor = visitor;
	}

	public RoomDto getRoom() {
		return room;
	}

	public void setRoom(RoomDto room) {
		this.room = room;
	}

	public UpdateMeetingDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UpdateMeetingDto(Integer id, MeetingStatus status, RoomDto room, UserDto employee, VisitorDto visitor) {
		super();
		this.id = id;
		this.status = status;
		this.room = room;
		this.employee = employee;
		this.visitor = visitor;
	}
}
