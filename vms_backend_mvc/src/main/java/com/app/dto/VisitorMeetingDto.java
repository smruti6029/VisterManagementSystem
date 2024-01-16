package com.app.dto;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.app.emun.MeetingContext;

import com.app.entity.Visitor;

public class VisitorMeetingDto {

	@NotBlank(message = "Name cannot be empty")
	@NotNull(message = "Name cannot be empty")
	private String name;

	@NotBlank(message = "Phone number cannot be empty")
	@Size(min = 10, max = 10, message = "Phone number length must be 10 digits")
	private String phoneNumber;

	@NotBlank(message = "Email cannot be empty")
	@Email(message = "Invalid email address")
	private String email;

	private Integer age;

	@NotNull(message = "State is required")
	private StateDto state;

	@NotNull(message = "City is required")
	private CityDto city;

	private String address;

	private String imageUrl;

	@NotNull(message = "choose a valid user for meeting")
	private UserDto user;

	private String companyName;

//	@NotBlank(message = "Company Name cannot be empty")
	private VisitorCompanyDto visitorCompany;

	private String remarks;

	private Date meetingStartDateTime;

	private Date meetingEndDateTime;

	private MeetingContext context;

	private RoomDto room;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public StateDto getState() {
		return state;
	}

	public void setState(StateDto state) {
		this.state = state;
	}

	public CityDto getCity() {
		return city;
	}

	public void setCity(CityDto city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public VisitorCompanyDto getVisitorCompany() {
		return visitorCompany;
	}

	public void setVisitorCompany(VisitorCompanyDto visitorCompany) {
		this.visitorCompany = visitorCompany;
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

	public Date getMeetingEndDateTime() {
		return meetingEndDateTime;
	}

	public void setMeetingEndDateTime(Date meetingEndDateTime) {
		this.meetingEndDateTime = meetingEndDateTime;
	}

	public MeetingContext getContext() {
		return context;
	}

	public void setContext(MeetingContext context) {
		this.context = context;
	}

	public RoomDto getRoom() {
		return room;
	}

	public void setRoom(RoomDto room) {
		this.room = room;
	}

	public VisitorMeetingDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static VisitorMeetingDto toVisitorDto(Visitor visitor) {
		VisitorMeetingDto visitorDto = new VisitorMeetingDto();
		if (visitor == null) {
			return null;
		}
		visitorDto.setName(visitor.getName());
		visitorDto.setPhoneNumber(visitor.getPhoneNumber());
		visitorDto.setEmail(visitor.getEmail().toLowerCase());
		visitorDto.setAddress(visitor.getAddress());
		visitorDto.setAge(visitor.getAge());
		visitorDto.setState(StateDto.convertEntityToDTO(visitor.getState()));
		visitorDto.setCity(CityDto.convertEntityToDTO(visitor.getCity()));
		visitorDto.setCompanyName(visitor.getCompanyName().trim());
		visitorDto.setVisitorCompany(VisitorCompanyDto.toDto(visitor.getCompany()));
		visitorDto.setImageUrl(visitor.getImage());
		return visitorDto;
	}

	public static Visitor toVisitor(VisitorMeetingDto visitorDto) {
		Visitor visitor = new Visitor();
		visitor.setImage(visitorDto.getImageUrl());
		if (visitorDto.getName() != null && !visitorDto.getName().trim().isEmpty()) {
			visitor.setName(visitorDto.getName());
		}
		visitor.setPhoneNumber(visitorDto.getPhoneNumber());
		visitor.setEmail(visitorDto.getEmail().toLowerCase());
		visitor.setAddress(visitorDto.getAddress());
		visitor.setAge(visitorDto.getAge());
		visitor.setState(StateDto.convertDTOToEntity(visitorDto.getState()));
		visitor.setCity(CityDto.convertDtoToEntity(visitorDto.getCity()));
		

		if (visitorDto.getCompanyName() != null && !visitorDto.getVisitorCompany().getName().isEmpty()) {
			visitor.setCompany(VisitorCompanyDto.toEntity(visitorDto.getVisitorCompany()));
			;
		}

		return visitor;
	}

}
