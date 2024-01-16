package com.app.dto;

import java.util.Date;
import java.util.List;

import com.app.entity.Room;

public class PaginatedMeetingResponse {

	private int pageSize;
	private Long totalElements;
	private Long totalMeeting;

	private int totalPages;
	private List<MeetingDto> meetings;

	


	public Long getTotalMeeting() {
		return totalMeeting;
	}

	public void setTotalMeeting(Long totalMeeting) {
		this.totalMeeting = totalMeeting;
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

	public List<MeetingDto> getMeetings() {
		return meetings;
	}

	public void setMeetings(List<MeetingDto> meetings) {
		this.meetings = meetings;
	}

	public PaginatedMeetingResponse(int pageSize, Long totalElements, int totalPages, List<MeetingDto> meetings) {
		super();
		this.pageSize = pageSize;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.meetings = meetings;
	}

	public PaginatedMeetingResponse() {
		
	}







}
