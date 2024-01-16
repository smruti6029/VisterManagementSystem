package com.app.dto;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.app.emun.MeetingStatus;

public class PaginationRequest {

	private Integer page;

	private Integer size;

	private UserDto user;
	
	private RoomDto room;
	
	private Date date;
	
	private Date fromDate;
	
	private Date toDate;
		
	private String phoneNumber;

	private MeetingStatus status;
	
	private Integer companyId;
	
	private Integer buildingId;
	
	private String companyName;
	
	

	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		if (size == null) {
            return Integer.MAX_VALUE;
        }
        return size;
	}
	

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public RoomDto getRoom() {
		return room;
	}

	public void setRoom(RoomDto room) {
		this.room = room;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public MeetingStatus getStatus() {
		return status;
	}

	public void setStatus(MeetingStatus status) {
		this.status = status;
	}

	
	
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public PaginationRequest(Integer page, Integer size, Date date, MeetingStatus status) {
		super();
		this.page = page;
		this.size = size;
		this.date = date;
		this.status = status;
	}

	public PaginationRequest() {
		super();
	}

}
