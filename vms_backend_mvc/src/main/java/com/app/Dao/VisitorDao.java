package com.app.Dao;

import java.util.List;

import com.app.dto.VisitorMeetingDetailsDto;
import com.app.dto.VisitorMeetingDto;
import com.app.entity.Meeting;
import com.app.entity.Visitor;

public interface VisitorDao {
	
	
	    List<Visitor> getAllVisitors();

	    Visitor getVisitorById(Integer id);

	    Visitor addVisitor(VisitorMeetingDto visitorDto);

	     Visitor updateVisitor(String phoneNumber ,  VisitorMeetingDto visitorDto);

		Visitor addVisitor(Visitor visitor);

		Visitor findByPhone(String phone);

		Visitor serachVisitor(String phoneNumber);

	     Visitor updateVisitor(Visitor serachVisitor );

		List<VisitorMeetingDetailsDto> getAllVisitorsByUserId(Integer userId);

		List<Meeting> getAllMeeting();

		Meeting getTodayMeeting(Integer id);

		Visitor getVisitorByEmail(String email);

//		Visitor serachVisitor2(String phone, Integer companyId);



}
