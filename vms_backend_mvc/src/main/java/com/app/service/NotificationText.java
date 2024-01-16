package com.app.service;

import com.app.dto.MeetingDto;

public interface NotificationText {

	public String approvedNotificationToReceptionist(MeetingDto meetingDto);

	public String roomAllotmentNotification(MeetingDto meetingDto);

	public String pending(MeetingDto meetingDto);

	public String cancelledNotificationToReceptionist(MeetingDto meetingDto);

	public String cancelledByVisitorNotification(MeetingDto meetingDto);
}
