package com.app.serviceImpl;

import org.springframework.stereotype.Service;

import com.app.dto.MeetingDto;
import com.app.service.NotificationText;

@Service
public class NotificationTextImpl implements NotificationText {

	@Override
	public String approvedNotificationToReceptionist(MeetingDto meetingDto) {
		// TODO Auto-generated method stub
		String employeeName = meetingDto.getUser().getFirstName();
		String visitorName = meetingDto.getVisitor().getName();
		StringBuilder text = new StringBuilder();
		if (meetingDto.getRoom() != null) {
			System.out.println("In room recep notification " + meetingDto.getRoom().getRoomName());
			text.append(employeeName + " has accepted the appointment request of " + visitorName
					+ " and meeting is in room, " + meetingDto.getRoom().getRoomName());
		} else {
			text.append(employeeName + " has accepted the appointment request of " + visitorName);
			
		}

		return text.toString();
	}

	@Override
	public String roomAllotmentNotification(MeetingDto meetingDto) {
		String visitorName = meetingDto.getVisitor().getName();
		String text = "You have accepted appointment request of " + visitorName + " and room alloted is, "
				+ meetingDto.getRoom().getRoomName();
		System.out.println("In room allot notification " + meetingDto.getRoom().getRoomName());
		return text;
	}

	@Override
	public String pending(MeetingDto meetingDto) {
		String visitorName = meetingDto.getVisitor().getName();
		String text = "You have a new appointment request with " + visitorName;
		return text;
	}

	@Override
	public String cancelledNotificationToReceptionist(MeetingDto meetingDto) {
		String employeeName = meetingDto.getUser().getFirstName();
		String visitorName = meetingDto.getVisitor().getName();
		String text = employeeName + " has cancelled the appointment request of " + visitorName;
		return text;
	}

	@Override
	public String cancelledByVisitorNotification(MeetingDto meetingDto) {
		String text = "Visitor has cancelled the meeting. Meeting ID - " + meetingDto.getId();
		return text;
	}

}
