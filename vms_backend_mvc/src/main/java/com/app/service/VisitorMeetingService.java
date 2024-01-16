package com.app.service;

import org.springframework.validation.BindingResult;

import com.app.dto.VisitorMeetingDto;
import com.app.response.Response;

public interface VisitorMeetingService {
	
	public Response<?> save(VisitorMeetingDto visitorMeetingDto) throws Exception;
}

