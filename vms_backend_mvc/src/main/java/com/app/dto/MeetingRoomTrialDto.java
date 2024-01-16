package com.app.dto;

import java.util.Date;

public class MeetingRoomTrialDto {

	private Integer id;

	private MeetingDto meeting;

	private RoomDto room;

	private String text;

	private Date createdAt;

	private UserTrialDto createdBy;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MeetingDto getMeeting() {
		return meeting;
	}

	public void setMeeting(MeetingDto meeting) {
		this.meeting = meeting;
	}

	public RoomDto getRoom() {
		return room;
	}

	public void setRoom(RoomDto room) {
		this.room = room;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public UserTrialDto getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserTrialDto createdBy) {
		this.createdBy = createdBy;
	}

	public MeetingRoomTrialDto(Integer id, MeetingDto meeting, RoomDto room, String text, Date createdAt,
			UserTrialDto createdBy) {
		super();
		this.id = id;
		this.meeting = meeting;
		this.room = room;
		this.text = text;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
	}

	public MeetingRoomTrialDto() {
		super();
	}

}
