package com.app.service;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.MeetingDto;
import com.app.dto.PaginationRequest;
import com.app.dto.UpdateMeetingDto;
import com.app.dto.VisitorMeetingDto;
import com.app.entity.Meeting;
import com.app.entity.Visitor;
import com.app.response.Response;

public interface MeetingService {

	List<Meeting> getMeetingsByVisitor(Visitor visitor);

	Response<?> addMeeting(MeetingDto meetingDto) throws Exception;

	Response<?> getmeetingWithstatusByuser(Integer id);

	Response<?> getallmeetingWithstatus();

	Response<?> updatemeetingStatus(UpdateMeetingDto meetingDTO, HttpServletRequest request) throws Exception;

	Response<?> addMeetingByuser(VisitorMeetingDto visitorDto);

	Response<?> updateCheckoutStatus(Visitor visitor) throws Exception;
	
	Response<?> updateCheckoutStatusByReceptionist(Visitor visitor) throws Exception;

	Response<?> updateMeeting(MeetingDto meetingDto);

	Response<?> isEmployeeBusy(@RequestParam Integer id,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") Date meetingStartDate,
			@RequestParam Integer duration);

	Response<?> getMeetingsByPagination(PaginationRequest request);

	Response<?> getMeetingByid(int id);

	ByteArrayInputStream generateExcel(List<MeetingDto> meetingsByPagination);


	List<MeetingDto> getMeetingByPaginationExcel(PaginationRequest request);

	Response<?> getMeetingOfVisitor(String phoneNumber, Integer companyId);

	Response<?> getMeetingsToCheckOut(String phoneNumber) throws Exception;
	
	Response<?> getMeetingsToCheckOut2(Integer companyId, String phoneNumber) throws Exception;

	Response<?> getMeetingByidForQr(int id);

	Response<?> getMeetingByPaginationDashBoardReceptionist(PaginationRequest request);

	Response<?> getmeetingForDashboardByuser(PaginationRequest meetingPaginationRequest);

	Response<?> getmeetingforUserDashboard(PaginationRequest meetingPaginationRequest);

//	Response<?> getMeetingByPaginationUserId(MeetingPaginationRequest request);

    public Void automaticallyCancelledMeeting(Integer noOfDays);

	void deleteOldFiles();

}
