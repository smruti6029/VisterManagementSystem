package com.app.service;

import javax.servlet.http.HttpServletRequest;

import com.app.dto.MeetingDto;
import com.app.dto.NotificationDTO;
import com.app.response.Response;

public interface NotificationService {

	public Response<?> save(NotificationDTO notificationDTO) throws Exception;

	public Response<?> getAll();
	
	public Response<?> getByMeetingId(MeetingDto meetingDto);

	public Response<?> getByUser(HttpServletRequest request, Integer companyId, Integer buildingId);

	public Response<?> updateNotification(NotificationDTO notificationDTO);

	public Response<?> markAsSeen(HttpServletRequest request, Integer companyId, Integer buildingId) throws Exception;
}
