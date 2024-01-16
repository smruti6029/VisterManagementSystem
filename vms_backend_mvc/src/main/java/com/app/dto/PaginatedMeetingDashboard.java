package com.app.dto;

import java.util.List;

public class PaginatedMeetingDashboard {

	private int pageSize;
	private Long totalElements;
	private Long totalMeeting;
	private Long totalPending;
	private Long totalInProcess;
	private Long totalCompleted;
	private Long totalApproved;
	private Long totalAvailableRoom;
	private Long totalRooms;
	private Long totalBusyRooms;
	private double AvgHoursPerWeek;
	private double TotalMeetingPerWeek;
	

	private int totalPages;
	private List<MeetingHourDto> meetingHours;
	
	
	
	
	

	public double getAvgHoursPerWeek() {
		return AvgHoursPerWeek;
	}


	public void setAvgHoursPerWeek(double avgHoursPerWeek) {
		AvgHoursPerWeek = avgHoursPerWeek;
	}


	public double getTotalMeetingPerWeek() {
		return TotalMeetingPerWeek;
	}


	public void setTotalMeetingPerWeek(double totalMeetingPerWeek) {
		TotalMeetingPerWeek = totalMeetingPerWeek;
	}

	
	
	

	public List<MeetingHourDto> getMeetingHours() {
		return meetingHours;
	}
	


	public void setMeetingHours(List<MeetingHourDto> meetingHours) {
		this.meetingHours = meetingHours;
	}



	public Long getTotalMeeting() {
		return totalMeeting;
	}

	public void setTotalMeeting(Long totalMeeting) {
		this.totalMeeting = totalMeeting;
	}

	public Long getTotalAvailableRoom() {
		return totalAvailableRoom;
	}

	public void setTotalAvailableRoom(Long totalAvailableRoom) {
		this.totalAvailableRoom = totalAvailableRoom;
	}

	public Long getTotalRooms() {
		return totalRooms;
	}

	public void setTotalRooms(Long totalRooms) {
		this.totalRooms = totalRooms;
	}

	public Long getTotalBusyRooms() {
		return totalBusyRooms;
	}

	public void setTotalBusyRooms(Long totalBusyRooms) {
		this.totalBusyRooms = totalBusyRooms;
	}

	public Long getTotalApproved() {
		return totalApproved;
	}

	public void setTotalApproved(Long totalApproved) {
		this.totalApproved = totalApproved;
	}

	public Long getTotalInProcess() {
		return totalInProcess;
	}

	public void setTotalInProcess(Long totalInProcess) {
		this.totalInProcess = totalInProcess;
	}

	public Long getTotalCompleted() {
		return totalCompleted;
	}

	public void setTotalCompleted(Long totalCompleted) {
		this.totalCompleted = totalCompleted;
	}

	public Long getTotalPending() {
		return totalPending;
	}

	public void setTotalPending(Long totalPending) {
		this.totalPending = totalPending;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}



	public PaginatedMeetingDashboard(int pageSize, Long totalElements, int totalPages, List<MeetingDto> meetings) {
		super();
		this.pageSize = pageSize;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
	
	}

	public PaginatedMeetingDashboard() {
		
	}







}
