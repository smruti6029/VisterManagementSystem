package com.vms2.dto;


import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.vms2.constant.MeetingContext;
import com.vms2.constant.MeetingStatus;
import com.vms2.entity.City;
import com.vms2.entity.Room;
import com.vms2.entity.State;
import com.vms2.entity.User;
import com.vms2.entity.Visitor;

public class VisitormeetingDTO {

	
	Integer id;
	
	private String name;

	@Size(min = 10, max = 13, message = "Phone number length must be between 10 and 13")
	private String phoneNumber;

	@NotEmpty(message = "Email cannot be empty")
	@Email(message = "Invalid email address")
	private String email;

	private String gender;

	private Integer age;
	
	@NotNull(message = "State is required")
	private State state;

	@NotNull(message = "City is required")
	private City city;

	private String address;
	
	private MeetingStatus meetingStatus;

	private String imageUrl;

	@NotNull(message = "addharNumber is requried")
	private String aadhaarNumber;


	private Boolean isActive;
	
	@NotNull(message ="choose a valid user for meeting" )
	private User user;

	private Room room;
	
	private String companyName;
	

	private String remarks;

	
	private Date meetingStartDateTime;

	
	private Date meetingEndDateTIme;

	
//	private Date checkInDateTime;
//
//
//	private Date checkOutDateTime;
	
	private MeetingContext meetingContext;

	
	
	
	
	public MeetingStatus getMeetingStatus() {
		return meetingStatus;
	}

	public void setMeetingStatus(MeetingStatus meetingStatus) {
		this.meetingStatus = meetingStatus;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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



	public MeetingContext getMeetingContext() {
		return meetingContext;
	}

	public void setMeetingContext(MeetingContext meetingContext) {
		this.meetingContext = meetingContext;
	}

	public static VisitormeetingDTO toVisitorDto(Visitor visitor) {
		VisitormeetingDTO visitorDto = new VisitormeetingDTO();
		visitorDto.setId(visitor.getId());
		visitorDto.setName(visitor.getName());
		visitorDto.setPhoneNumber(visitor.getPhoneNumber());
		visitorDto.setEmail(visitor.getEmail().toLowerCase());
		visitorDto.setAddress(visitor.getAddress());
		visitorDto.setGender(visitor.getGender());
		visitorDto.setAge(visitor.getAge());
		visitorDto.setState(visitor.getState());
		visitorDto.setCity(visitor.getCity());
		visitorDto.setAadhaarNumber(visitor.getAadhaarNumber());
		visitorDto.setCompanyName(visitor.getCompanyName());
		return visitorDto;
	}

	public static Visitor toVisitor(VisitormeetingDTO visitorDto) {
		Visitor visitor = new Visitor();
		visitor.setId(visitorDto.getId());
		visitor.setName(visitorDto.getName());
		visitor.setPhoneNumber(visitorDto.getPhoneNumber());
		visitor.setEmail(visitorDto.getEmail().toLowerCase());
		visitor.setAddress(visitorDto.getAddress());
		visitor.setGender(visitorDto.getGender());
		visitor.setAge(visitorDto.getAge());
		visitor.setState(visitorDto.getState());
		visitor.setCity(visitorDto.getCity());
		visitor.setAadhaarNumber(visitorDto.getAadhaarNumber());
		visitor.setCompanyName(visitorDto.getCompanyName());
		visitor.setId(visitorDto.getId() != null ? visitorDto.getId() : null);
		
		return visitor;
	}

}
