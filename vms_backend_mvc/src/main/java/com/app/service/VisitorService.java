package com.app.service;

import java.util.List;

import javax.validation.Valid;

import com.app.dto.MeetingDto;
import com.app.dto.VisitorDto;
import com.app.dto.VisitorMeetingDetailsDto;
import com.app.dto.VisitorMeetingDto;
import com.app.entity.Meeting;
import com.app.entity.Visitor;
import com.app.response.Response;

public interface VisitorService {

	List<Visitor> getAllVisitors();

	Visitor getVisitorById(Integer id);

	Response<?> addVisitor(VisitorDto visitorDto);

	VisitorDto updateVisitor(String phoneNumber, VisitorDto visitorDto);

	boolean isPhoneAlreadyInUse(String phone);

	MeetingDto serachVisitor(String phoneNumber);

	List<VisitorMeetingDetailsDto> getAllVisitorsByUserId(Integer userId);

	List<Meeting> getAllMeeting();

	Visitor serachVisitorByphone(String phoneNumber);

	Response<?> addVisitorByuser(VisitorMeetingDto visitorDto);

	Response<?> checkIn(MeetingDto meetingDto);

	Response<?> updateCheckoutStatus(String phone) throws Exception;
	
	Response<?> updateCheckoutStatusByReceptionist(String phone) throws Exception;

	Visitor getVisitorByEmail(String email);

	Response<?> updateVisitorMeeting(String phoneNumber, @Valid VisitorMeetingDto visitorMeetingDto) throws Exception;

	Response<?> updateCheckoutStatus2(String phone, Integer companyId) throws Exception;

	Response<?> searchCompanyByName(String companyName);


}
