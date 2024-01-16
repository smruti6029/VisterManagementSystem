package com.app.dto;

import java.util.Date;
import java.util.Map;

public class MeetingDashboardDto {
	private Long totalMeetings;
	private Long pending;
	private Long completed;
	private Long rescheduled;
	private Long cancelled;
	private Long totalHoursOfMeeting;
	private Map<Date, Map<String, Map<String, Long>>> meetingsContextDate;
	private Map<String, Long> totalHoursByContext;

	public Long getTotalMeetings() {
		return totalMeetings;
	}

	public void setTotalMeetings(Long totalMeetings) {
		this.totalMeetings = totalMeetings;
	}

	public Long getPending() {
		return pending;
	}

	public void setPending(Long pending) {
		this.pending = pending;
	}

	public Long getCompleted() {
		return completed;
	}

	public void setCompleted(Long completed) {
		this.completed = completed;
	}

	public Long getRescheduled() {
		return rescheduled;
	}

	public void setRescheduled(Long rescheduled) {
		this.rescheduled = rescheduled;
	}

	public Long getCancelled() {
		return cancelled;
	}

	public void setCancelled(Long cancelled) {
		this.cancelled = cancelled;
	}

	public Long getTotalHoursOfMeeting() {
		return totalHoursOfMeeting;
	}

	public void setTotalHoursOfMeeting(Long totalHoursOfMeeting) {
		this.totalHoursOfMeeting = totalHoursOfMeeting;
	}

	public Map<Date, Map<String, Map<String, Long>>> getMeetingsContextDate() {
		return meetingsContextDate;
	}

	public void setMeetingsContextDate(Map<Date, Map<String, Map<String, Long>>> meetingsContextDate) {
		this.meetingsContextDate = meetingsContextDate;
	}
	

	public Map<String, Long> getTotalHoursByContext() {
		return totalHoursByContext;
	}

	public void setTotalHoursByContext(Map<String, Long> totalHoursByContext) {
		this.totalHoursByContext = totalHoursByContext;
	}

	public MeetingDashboardDto(Long totalMeetings, Long pending, Long completed, Long rescheduled, Long cancelled,
			Long totalHoursOfMeeting, Map<Date, Map<String, Map<String, Long>>> meetingsContextDate2) {
		this.totalMeetings = totalMeetings;
		this.pending = pending;
		this.completed = completed;
		this.rescheduled = rescheduled;
		this.cancelled = cancelled;
		this.totalHoursOfMeeting = totalHoursOfMeeting;
		this.meetingsContextDate = meetingsContextDate2;
	}

}
