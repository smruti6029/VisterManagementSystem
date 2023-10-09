package com.vms2.serviceImp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vms2.constant.MeetingStatus;
import com.vms2.dao.MeetingDao;
import com.vms2.dao.RoomDao;
import com.vms2.dao.UserDao;
import com.vms2.dao.VisitorDao;
import com.vms2.dto.MeetingDTO;
import com.vms2.dto.VisitormeetingDTO;
import com.vms2.entity.Meeting;
import com.vms2.entity.Room;
import com.vms2.entity.User;
import com.vms2.entity.Visitor;
import com.vms2.mailservice.EmailSendService;
import com.vms2.response.Response;
import com.vms2.service.MeetingService;

@Service
public class MeetingServiceImp implements MeetingService {

	@Autowired
	private MeetingDao meetingDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private VisitorDao visitorDao;

	@Autowired
	private EmailSendService emailSendService;

	@Override
	public Response<?> addMeeting(VisitormeetingDTO addVisitor) {
		User user = userDao.getuserByid(addVisitor.getUser().getId());
		Visitor visitor = visitorDao.getVisitorByPhone(addVisitor.getPhoneNumber());
		

		Meeting meeting = new Meeting();

		meeting.setCheckInDateTime(new Date());
		meeting.setStatus(MeetingStatus.PENDING);
		meeting.setCompany(user.getCompany());
		meeting.setCreatedAt(new Date());
		meeting.setMeetingStartDateTime(addVisitor.getMeetingStartDateTime());
		meeting.setMeetingEndDateTIme(addVisitor.getMeetingEndDateTIme());

		meeting.setIsActive(true);
		meeting.setEmployee(user);
		meeting.setRemarks(addVisitor.getRemarks());
		meeting.setVisitor(visitor);
		Meeting save = meetingDao.save(meeting);

		String subject = "Meeting Request";
		String companySection = addVisitor.getCompanyName() != null
				? "<p><strong>Company:</strong> " + addVisitor.getCompanyName() + "</p>\n"
				: "";

		String emailContent = "<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "<head>\n"
				+ "    <meta charset=\"UTF-8\">\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
				+ "    <title>Meeting Request</title>\n" + "    <style>\n" + "        body {\n"
				+ "            font-family: Arial, sans-serif;\n" + "            background-color: #f4f4f4;\n"
				+ "        }\n" + "        .container {\n" + "            max-width: 600px;\n"
				+ "            margin: 0 auto;\n" + "            padding: 20px;\n"
				+ "            background-color: #ffffff;\n"
				+ "            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);\n" + "        }\n" + "        h1 {\n"
				+ "            color: #007bff;\n" + "        }\n" + "        p {\n" + "            font-size: 16px;\n"
				+ "            line-height: 1.5;\n" + "            margin: 10px 0;\n" + "        }\n" + "    </style>\n"
				+ "</head>\n" + "<body>\n" + "    <div class=\"container\">\n" + "        <h1>Meeting Request</h1>\n"
				+ "        <p><strong>Name:</strong> " + addVisitor.getName() + "</p>\n"
				+ "        <p><strong>Meeting Time:</strong> " + convertTime(addVisitor.getMeetingStartDateTime())
				+ " to " + convertTime(addVisitor.getMeetingEndDateTIme()) + "</p>\n"
				+ "        <p><strong>Remarks:</strong> " + addVisitor.getRemarks() + "</p>\n" + companySection
				+ "        <p><strong>Context:</strong> " + "PENDING" + "</p>\n" + "    </div>\n" + "</body>\n"
				+ "</html>";

		String toEmail = user.getEmail();

		try {
			emailSendService.sendEmail(toEmail, subject, emailContent);
		} catch (Exception e) {
		}

		return null;
	}

	public String convertTime(Date date) {
		SimpleDateFormat localTimeFormatter = new SimpleDateFormat("HH:mm");
		localTimeFormatter.setTimeZone(TimeZone.getDefault());
		String localTime = localTimeFormatter.format(date);
		return localTime;
	}

	@Override
	public Response<?> getmeetingWithstatusByuser(Integer id) {

		List<Meeting> meetings = meetingDao.getMeetingsByUserId(id);
		if (meetings != null) {
			List<MeetingDTO> allmeeting = new ArrayList<>();
			meetings.forEach(x -> {
				MeetingDTO convertToDTO = MeetingDTO.convertToDTO(x);
				allmeeting.add(convertToDTO);
			});
			return new Response<>("Meeting Detalis", allmeeting, HttpStatus.OK.value());
		}
		return new Response<>(" No Recod Found ", null, HttpStatus.NO_CONTENT.value());
	}

	@Override
	public Response<?> getallmeetingWithstatus() {

		List<Meeting> all = meetingDao.getAll();

		if (!all.isEmpty()) {
			List<MeetingDTO> allmeeting = new ArrayList<>();
			all.forEach(x -> {
				MeetingDTO convertToDTO = MeetingDTO.convertToDTO(x);
				allmeeting.add(convertToDTO);
			});

			return new Response<>(" All Meeting Detalis", allmeeting, HttpStatus.OK.value());
		}
		return new Response<>(" No Recod Found ", null, HttpStatus.NO_CONTENT.value());
	}

	@Override
	public Response<?> updatemeetingStatus(MeetingDTO meetingDTO) {

		Meeting meeting = meetingDao.getById(meetingDTO.getId());

		Room room = roomDao.getById(meetingDTO.getRoom().getId());

		User user = userDao.getuserByid(meetingDTO.getUser().getId());

		if (meetingDTO.getStatus().equals(MeetingStatus.CANCELLED)) {
			String email = meeting.getVisitor().getEmail();
			String content = String.format(
					"<div class='container'>" + "<h1>%s</h1>" + "<p>Status: %s</p>" + "<p>Remarks: %s</p>"
							+ "<p>Your request has been Rejected</p>" + "</div>",
					meeting.getVisitor().getName(), meetingDTO.getStatus(), meetingDTO.getRemarks());

		}

		if (room.getIsAvailable() != true) {
			return new Response<>("Room Is not Avilable", null, HttpStatus.BAD_REQUEST.value());
		}

		if (meeting != null) {

			if (meetingDTO.getContext() != null) {
				meeting.setContext(meetingDTO.getContext());
			}
			if (meetingDTO.getStatus() != null) {
				meeting.setStatus(meetingDTO.getStatus());
			}

			if (meetingDTO.getUser() != null) {
				meeting.setEmployee(user);
			}

			if (meetingDTO.getMeetingStartDateTime() != null) {
				meeting.setMeetingStartDateTime(meetingDTO.getMeetingStartDateTime());
			}

			if (meetingDTO.getMeetingEndDateTIme() != null) {
				meeting.setMeetingEndDateTIme(meetingDTO.getMeetingEndDateTIme());
			}

			if (meetingDTO.getRemarks() != null) {
				meeting.setRemarks(meetingDTO.getRemarks());
			}

			Meeting updateMeeting = meetingDao.update(meeting);

			room.setIsAvailable(false);

			Room update = roomDao.update(room);

//		PENDING, APPROVED, INPROCESS, RESCHEDULED, COMPLETED, CANCELLED

			String email = meeting.getVisitor().getEmail();

			if (meetingDTO.getStatus().equals(MeetingStatus.APPROVED)) {
				String subject = "Meeting Approved";
				String content = String.format(
						"<div class='container'>" + "<h1>%s</h1>" + "<p>Meeting Start: %s</p>"
								+ "<p>Meeting End: %s</p>" + "<p>Status: %s</p>"
								+ "<p>Your request has been Approved</p>" + "</div>",
						meeting.getVisitor().getName(), formatDate(updateMeeting.getMeetingStartDateTime()),
						formatDate(updateMeeting.getMeetingEndDateTIme()), updateMeeting.getStatus());
				try {
					emailSendService.sendEmail(email, subject, content);
				} catch (Exception e) {
				}
			}

			else if (meetingDTO.getStatus().equals(MeetingStatus.RESCHEDULED)) {
				String subject = "Meeting " + meetingDTO.getStatus();
				String content = String.format(
						"<div class='container'>" + "<h1>%s</h1>" + "<p>Meeting Start: %s</p>"
								+ "<p>Meeting End: %s</p>" + "<p>Status: %s</p>"
								+ "<p>Your request has been RESCHEDULED</p>" + "</div>",
						meeting.getVisitor().getName(), formatDate(updateMeeting.getMeetingStartDateTime()),
						formatDate(updateMeeting.getMeetingEndDateTIme()), updateMeeting.getStatus());
				try {
					emailSendService.sendEmail(email, subject, content);
				} catch (Exception e) {
				}
			} else {
				String subject = "Meeting " + meetingDTO.getStatus();
				String content = String.format(
						"<div class='container'>" + "<h1>%s</h1>" + "<p>Status: %s</p>" + "<p>Remarks: %s</p>"
								+ "<p>%s</p>" + "</div>",
						meeting.getVisitor().getName(), meetingDTO.getStatus(), meetingDTO.getRemarks(), subject);

				try {
					emailSendService.sendEmail(email, subject, content);
				} catch (Exception e) {
				}
			}
			return new Response<>("Updated Successfully", meetingDTO, HttpStatus.OK.value());
		}

		return new Response<>("Updated Falid", null, HttpStatus.BAD_REQUEST.value());
	}

	private String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	@Override
	public Response<?> addMeetingByuser(VisitormeetingDTO visitorDto) {

		

		User user = userDao.getuserByid(visitorDto.getUser().getId());
		Visitor visitor = visitorDao.getVisitorByPhone(visitorDto.getPhoneNumber());
		Room room = roomDao.getById(visitorDto.getRoom().getId());
		
		Meeting meeting = new Meeting();
		meeting.setRoom(room);
		meeting.setStatus(MeetingStatus.APPROVED);
		meeting.setCompany(user.getCompany());
		meeting.setCreatedAt(new Date());
		meeting.setMeetingStartDateTime(visitorDto.getMeetingStartDateTime());
		meeting.setMeetingEndDateTIme(visitorDto.getMeetingEndDateTIme());

		meeting.setIsActive(true);
		meeting.setEmployee(user);
		meeting.setRemarks(visitorDto.getRemarks());
		meeting.setVisitor(visitor);
		
		
		meetingDao.save(meeting);
		room.setIsActive(false);
		roomDao.update(room);

		String email = meeting.getVisitor().getEmail();

		String subject = "Meeting Scheduled";

		String companySection = visitorDto.getCompanyName() != null
				? "<p><strong>Company:</strong> " + visitorDto.getCompanyName() + "</p>\n"
				: "";

		String emailContent = "<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "<head>\n"
				+ "    <meta charset=\"UTF-8\">\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
				+ "    <title>Meeting Scheduled</title>\n" + "    <style>\n" + "        body {\n"
				+ "            font-family: Arial, sans-serif;\n" + "            background-color: #f4f4f4;\n"
				+ "        }\n" + "        .container {\n" + "            max-width: 600px;\n"
				+ "            margin: 0 auto;\n" + "            padding: 20px;\n"
				+ "            background-color: #ffffff;\n"
				+ "            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);\n" + "        }\n" + "        h1 {\n"
				+ "            color: #007bff;\n" + "        }\n" + "        p {\n" + "            font-size: 16px;\n"
				+ "            line-height: 1.5;\n" + "            margin: 10px 0;\n" + "        }\n" + "    </style>\n"
				+ "</head>\n" + "<body>\n" + "    <div class=\"container\">\n" + "        <h1>Meeting Detalis</h1>\n"
				+ "        <p><strong>Name:</strong> " + visitorDto.getName() + "</p>\n"
				+ "        <p><strong>Meeting Time:</strong> " + convertTime(visitorDto.getMeetingStartDateTime())
				+ " to " + convertTime(visitorDto.getMeetingEndDateTIme()) + "</p>\n"
				+ "        <p><strong>Remarks:</strong> " + visitorDto.getRemarks() + "</p>\n" + companySection
				+ "        <p><strong>Context:</strong> " + "PENDING" + "</p>\n" + "    </div>\n"
				+ "    <!-- Meeting with user's first name and last name -->\n" + "    <p>Meeting with "
				+ user.getFirstname() + " " + user.getLastname() + "</p>\n" + "</body>\n" + "</html>";
		try {
			emailSendService.sendEmail(email, subject, emailContent);
			return new Response<>("Mail Sent Succesfully",visitor,HttpStatus.OK.value());
		} catch (Exception e) {
			return new Response<>("Mail Not respond Correctly",null,HttpStatus.BAD_REQUEST.value());
		}

		
	}

	@Override
	public Response<?> updateCheckoutStatus(Visitor visitor) {
		
		try
		{
		Meeting meeting = meetingDao.getMeetingByVisitorIdAndTodayDate(visitor.getId());
		
		if(meeting==null)
		{
			return new Response<>("No Meeting  Found",null,HttpStatus.BAD_REQUEST.value());
		}
		
		if(meeting.getIsActive()==false)
		{
			return new Response<>("Already Check Out",null,HttpStatus.BAD_REQUEST.value());
		}
		meeting.setCheckOutDateTime(new Date());
		meeting.setStatus(MeetingStatus.COMPLETED);
		meeting.setIsActive(false);
		
		System.out.println(meeting.getRoom().getId());
		Room room = roomDao.getById(meeting.getRoom().getId());
		
		room.setIsAvailable(true);
		
		roomDao.update(room);
		
		meetingDao.update(meeting);
		
		return new Response<>("Check Out Succesfully",null,HttpStatus.OK.value());
		}catch (Exception e) {
			return new Response<>("Check Out Fallid Try Agin",null,HttpStatus.BAD_REQUEST.value());
		}	 
	}
}
