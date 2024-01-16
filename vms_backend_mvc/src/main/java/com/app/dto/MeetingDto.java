package com.app.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.app.emun.MeetingContext;
import com.app.emun.MeetingStatus;
import com.app.entity.Company;
import com.app.entity.Meeting;
import com.app.entity.Room;
import com.app.entity.User;
import com.app.entity.VisitorCompany;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

public class MeetingDto {

	private Integer id;

	@NotNull(message = "User is required")
	private UserDto user;

//	@NotNull(message = "Visitor is required")
	private VisitorDto visitor;

//	@NotBlank(message = "Phone is required")
	private String phone;

//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date checkInDateTime;

//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date checkOutDateTime;

	@NotNull(message = "Context is required")
	private MeetingContext context;

	@NotBlank(message = "remarks is required")
	private String remarks;

//	@NotNull(message = "room is required")
	private RoomDto room;

//	@NotNull(message = "Meeting Start date is required")
//	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date meetingStartDateTime;

//	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date meetingEndDateTime;

//	@NotNull(message = "Duration is required")
	private String duration;

	private Boolean roomChanged;

	private MeetingStatus status;

	private Date createdBy;

	private VisitorCompanyDto visitorCompanyDto;

	private String updatedBy;

	private CompanyDTO company;

	public VisitorCompanyDto getVisitorCompanyDto() {
		return visitorCompanyDto;
	}

	public void setVisitorCompanyDto(VisitorCompanyDto visitorCompanyDto) {
		this.visitorCompanyDto = visitorCompanyDto;
	}

	public Integer getId() {
		return id;
	}

	public Date getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Date createdBy) {
		this.createdBy = createdBy;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public VisitorDto getVisitor() {
		return visitor;
	}

	public void setVisitor(VisitorDto visitor) {
		this.visitor = visitor;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public RoomDto getRoom() {
		return room;
	}

	public void setRoom(RoomDto room) {
		this.room = room;
	}

	public Date getMeetingStartDateTime() {
		return meetingStartDateTime;
	}

	public void setMeetingStartDateTime(Date meetingStartDateTime) {
		this.meetingStartDateTime = meetingStartDateTime;
	}

//	public Date getMeetingEndDateTime() {
//	    if (duration != null && meetingStartDateTime != null) {
//	        long durationInMilliseconds = duration.getTime(); // Extract milliseconds from the Date
//	        Date endDateTime = new Date(meetingStartDateTime.getTime() + durationInMilliseconds);
//	        return endDateTime;
//	    }
//	    return meetingEndDateTime;
//	}
//
//	public void setMeetingEndDateTime(Date meetingEndDateTime) {
//	    this.meetingEndDateTime = meetingEndDateTime;
//
//	    if (meetingStartDateTime != null && meetingEndDateTime != null) {
//	        long durationInMinutes = (meetingEndDateTime.getTime() - meetingStartDateTime.getTime()) / (60 * 1000);
//	        setDuration(new Date(durationInMinutes)); // Convert duration to Date
//	    }
//	}

	public CompanyDTO getCompany() {
		return company;
	}

	public void setCompany(CompanyDTO company) {
		this.company = company;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getMeetingEndDateTime() {
		if (duration != null && meetingStartDateTime != null) {
			long durationInMilliseconds = durationToMillis(duration); // Convert duration to milliseconds
			Date endDateTime = new Date(meetingStartDateTime.getTime() + durationInMilliseconds);
			return endDateTime;
		}
		return meetingEndDateTime;
	}

	public void setMeetingEndDateTime(Date meetingEndDateTime) {
		this.meetingEndDateTime = meetingEndDateTime;

		if (meetingStartDateTime != null && meetingEndDateTime != null) {
			long durationInMinutes = (meetingEndDateTime.getTime() - meetingStartDateTime.getTime()) / (60 * 1000);
			setDuration(minutesToHHMMSS(durationInMinutes)); // Convert duration to hh:mm:ss format
		}
	}

	private long durationToMillis(String duration) {

		String[] parts = duration.split(":");
		int hours = Integer.parseInt(parts[0]);
		int minutes = Integer.parseInt(parts[1]);
		int seconds = Integer.parseInt(parts[2]);

		return (hours * 60 * 60 + minutes * 60 + seconds) * 1000L;
	}

	private String minutesToHHMMSS(long durationInMinutes) {
		long hours = durationInMinutes / 60;
		long minutes = durationInMinutes % 60;
		long seconds = 0;

		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public MeetingStatus getStatus() {
		return status;
	}

	public void setStatus(MeetingStatus status) {
		this.status = status;
	}

	public Boolean getRoomChanged() {
		return roomChanged;
	}

	public void setRoomChanged(Boolean roomChanged) {
		this.roomChanged = roomChanged;
	}

	public MeetingDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static MeetingDto convertToDTO(Meeting meeting) throws Exception {
		MeetingDto meetingDto = new MeetingDto();
		if (meeting == null) {
			return null;
		}
		meetingDto.setId(meeting.getId());
		meetingDto.setUser(UserDto.convertUserToDTO(meeting.getEmployee()));
		meetingDto.setVisitor(VisitorDto.convertToDTO(meeting.getVisitor()));
		meetingDto.setContext(meeting.getContext());
		meetingDto.setRemarks(meeting.getRemarks());
		meetingDto.setRoom(RoomDto.toRoomDto(meeting.getRoom()));
		meetingDto.setStatus(meeting.getStatus());
		meetingDto.setUpdatedBy(meeting.getUpdatedBy());
		meetingDto.setRoomChanged(meeting.getRoomChanged());
		meetingDto.setCompany(CompanyDTO.toCompanyDto(meeting.getCompany()));
		try {
			VisitorCompany company = meeting.getVisitor().getCompany();
			VisitorCompanyDto obj = new VisitorCompanyDto();
			meetingDto.setVisitorCompanyDto(obj.toDto(company));
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			meetingDto.setMeetingStartDateTime(convertDateToMilliseconds(meeting.getMeetingStartDateTime()));
			meetingDto.setMeetingEndDateTime(convertDateToMilliseconds(meeting.getMeetingEndDateTime()));
			meetingDto.setCheckInDateTime(convertDateToMilliseconds(meeting.getCheckInDateTime()));
			meetingDto.setCreatedBy(convertDateToMilliseconds(meeting.getCreatedAt()));
			meetingDto.setCheckOutDateTime(
					meeting.getCheckOutDateTime() != null ? convertDateToMilliseconds(meeting.getCheckOutDateTime())
							: null);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return meetingDto;
	}

	public static Meeting convertToEntity(MeetingDto meetingDto) {
		if (meetingDto == null) {
			return null;
		}

		Meeting meeting = new Meeting();
		meeting.setId(meetingDto.getId());
		meeting.setEmployee(User.fromDTO(meetingDto.getUser()));
		meeting.setVisitor(VisitorDto.toVisitor(meetingDto.getVisitor()));
		meeting.setContext(meetingDto.getContext());
		meeting.setRemarks(meetingDto.getRemarks());
		meeting.setRoom(RoomDto.toRoom(meetingDto.getRoom()));
		meeting.setStatus(meetingDto.getStatus());
		meeting.setUpdatedBy(meeting.getUpdatedBy());
		meeting.setRoomChanged(meetingDto.getRoomChanged());
		
		

		try {
			meeting.setMeetingStartDateTime(convertDateToMilliseconds(meetingDto.getMeetingStartDateTime()));
			meeting.setMeetingEndDateTime(convertDateToMilliseconds(meetingDto.getMeetingEndDateTime()));
			meeting.setCheckInDateTime(convertDateToMilliseconds(meetingDto.getCheckInDateTime()));
			meeting.setCreatedAt(convertDateToMilliseconds(meetingDto.getCreatedBy()));
			meeting.setCheckOutDateTime(meetingDto.getCheckOutDateTime() != null
					? convertDateToMilliseconds(meetingDto.getCheckOutDateTime())
					: null);
		} catch (Exception e) {
			// Handle the exception, or log it as needed
			e.printStackTrace();
			// You can also throw a custom exception or handle it in another way.
		}

		return meeting;
	}

	public static Date convertDateToMilliseconds(Date dateString) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = dateString;
			long milliseconds = date.getTime() + 5L * 60L * 60L * 1000L + 30L * 60L * 1000L;
			return new Date(milliseconds);
		} catch (Exception e) {
			return dateString;
		}
	}

}
