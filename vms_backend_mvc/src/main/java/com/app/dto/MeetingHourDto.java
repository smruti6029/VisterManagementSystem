package com.app.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MeetingHourDto {
	
	@JsonFormat(pattern = " EEEE - dd/MM/yyyy ") 
    private Date date;
    
    private double totalMeetingHours;
    
    private double totalMeeting;
    
    private double MeetingAverage;
    
   

	public double getTotalMeeting() {
		return totalMeeting;
	}

	public void setTotalMeeting(double totalMeeting) {
		this.totalMeeting = totalMeeting;
	}

	public double getMeetingAverage() {
		return MeetingAverage;
	}

	public void setMeetingAverage(double meetingAverage) {
		MeetingAverage = meetingAverage;
	}

	public void setTotalMeetingHours(double totalMeetingHours) {
		this.totalMeetingHours = totalMeetingHours;
	}

	public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotalMeetingHours() {
        return totalMeetingHours;
    }

    public void setTotalMeetingHours1(double totalMeetingHours) {
        this.totalMeetingHours = totalMeetingHours;
    }

	public MeetingHourDto() {
	
	}

	public MeetingHourDto(Date date, double totalMeetingHours2, double totalMeeting, double meetingAverage) {
		super();
		this.date = date;
		this.totalMeetingHours = totalMeetingHours2;
		this.totalMeeting = totalMeeting;
		MeetingAverage = meetingAverage;
	}
    
    
    
    
}
