package com.app.service;

import com.app.response.Response;

public interface TrialService {
	
	public Response<?> getRoomTrialDataByMeetingId(Integer meetingId);
}
