package com.vms2.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vms2.constant.MeetingContext;
import com.vms2.constant.MeetingStatus;
import com.vms2.entity.Meeting;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeetingDTO {

	private Integer id;

	private MeetingStatus status;

	private UserDto user;

	private VisitorDTO visitor;

	private MeetingContext context;

	private String remarks;

	private Date meetingStartDateTime;

	private Date meetingEndDateTIme;

	private Date checkInDateTime;

	private Date checkOutDateTime;

	private RoomDTO room;

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

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto userDto) {
		this.user = userDto;
	}

	public VisitorDTO getVisitor() {
		return visitor;
	}

	public void setVisitor(VisitorDTO visitorDTO) {
		this.visitor = visitorDTO;
	}

	public MeetingContext getContext() {
		return context;
	}

	public void setContext(MeetingContext context) {
		this.context = context;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getMeetingStartDateTime() {
		return meetingStartDateTime;
	}

	public void setMeetingStartDateTime(Date meetingStartDateTime) {
		this.meetingStartDateTime = meetingStartDateTime;
	}

	public Date getMeetingEndDateTIme() {
		return meetingEndDateTIme;
	}

	public void setMeetingEndDateTIme(Date meetingEndDateTIme) {
		this.meetingEndDateTIme = meetingEndDateTIme;
	}

	public Date getCheckInDateTime() {
		return checkInDateTime;
	}

	public void setCheckInDateTime(Date checkInDateTime) {
		this.checkInDateTime = checkInDateTime;
	}

	public Date getCheckOutDateTime() {
		return checkOutDateTime;
	}

	public void setCheckOutDateTime(Date checkOutDateTime) {
		this.checkOutDateTime = checkOutDateTime;
	}

	public RoomDTO getRoom() {
		return room;
	}

	public void setRoom(RoomDTO room) {
		this.room = room;
	}

	public static MeetingDTO convertToDTO(Meeting meeting) {
		MeetingDTO meetingDTO = new MeetingDTO();
		meetingDTO.setId(meeting.getId());
		meetingDTO.setStatus(meeting.getStatus()); // Convert MeetingStatus to String
		meetingDTO.setUser(UserDto.convertUserToDTO(meeting.getEmployee())); // Assuming you have a userToDTO method
		meetingDTO.setVisitor(VisitorDTO.convertToDTO(meeting.getVisitor())); // Assuming you have a visitorToDTO method
		meetingDTO.setContext(meeting.getContext());
		meetingDTO.setRemarks(meeting.getRemarks());
		meetingDTO.setMeetingStartDateTime(meeting.getMeetingStartDateTime());
		meetingDTO.setMeetingEndDateTIme(meeting.getMeetingEndDateTIme());
		meetingDTO.setCheckInDateTime(meeting.getCheckInDateTime());
		meetingDTO.setCheckOutDateTime(meeting.getCheckOutDateTime());
		try {
			meetingDTO.setRoom(RoomDTO.fromRoom(meeting.getRoom()));
		} catch (Exception e) {
			// TODO: handle exception
		}

		return meetingDTO;
	}

//	 public static Meeting toMeeting(VisitormeetingDTO meetingDTO) {
//		    Meeting meeting = new Meeting();
//		    meeting.setId(meetingDTO.getId());
//		    meeting.setStatus(meetingDTO.getMeetingStatus()); // Assuming MeetingStatus is an enum
//		    meeting.setEmployee(meetingDTO.getUser()); // Convert UserDto to User entity
//		    meeting.setVisitor(VisitorDTO.convertToDTO(meetingDTO.get)); // Convert VisitorDTO to Visitor entity
//		    meeting.setContext(meetingDTO.getContext());
//		    meeting.setRemarks(meetingDTO.getRemarks());
//		    meeting.setMeetingStartDateTime(meetingDTO.getMeetingStartDateTime());
//		    meeting.setMeetingEndDateTIme(meetingDTO.getMeetingEndDateTIme());
//		    meeting.setCheckInDateTime(meetingDTO.getCheckInDateTime());
//		    meeting.setCheckOutDateTime(meetingDTO.getCheckOutDateTime());
//		    meeting.setRoom(RoomDtoToRoom(meetingDTO.getRoom())); // Convert RoomDTO to Room entity
//		    
//		    return meeting;
//		}

}
