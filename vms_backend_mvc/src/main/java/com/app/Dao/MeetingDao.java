package com.app.Dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import com.app.dto.IsActiveDto;
import com.app.dto.MeetingDto;
import com.app.dto.MeetingHourDto;
import com.app.dto.PaginationRequest;
import com.app.entity.Meeting;
import com.app.entity.Room;
import com.app.entity.Visitor;

public interface MeetingDao {

	List<Meeting> getMeetingsByVisitor(Visitor visitor);

	public Meeting save(Meeting meeting);

	public Meeting getById(Integer integer);

	public List<Meeting> getAll(Integer CompanyId);

	public Meeting update(Meeting meeting);

	public Meeting delete(IsActiveDto idIsactiveDTO);

	public List<Meeting> getMeetingsByUserId(Integer id);

	public Meeting checkIfRoomAvailable(Room room, Date startDate, Date endDate);

	List<Meeting> getMeetingByVisitorIdAndTodayDate(Integer vid);

	Meeting getMeetingByUseridandDate(MeetingDto meetingDto);

	Meeting isEmployeeBusy(Integer userId, Date meetingStartDate, Date meetingEndDate);

	public Page<Meeting> getMeetingsByPagination(PaginationRequest request);

	List<Meeting> getVisitorIncompleteMeetings(Integer vid, Integer companyId);

	public Meeting inProcessMeeting(Integer visitorId);

	public List<MeetingHourDto> getMeetingHoursLastWeek(List<Meeting> content, Date date);

	List<Meeting> getMeetingsByUserIdForDashboard(PaginationRequest request);

	List<Meeting> getMeetingByroomFortodayDate(Integer id);

	public List<Meeting> getMeetingByVisitorIdAndCompanyId(Integer visitorId, Integer companyId);
	

}
