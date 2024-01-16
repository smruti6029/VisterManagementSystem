package com.app.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.Dao.MeetingDao;
import com.app.Dao.MeetingRoomTrailDao;
import com.app.Dao.NotificationDao;
import com.app.Dao.RoomDao;
import com.app.Dao.UserDao;
import com.app.Dao.VisitorDao;
import com.app.dto.IsActiveDto;
import com.app.dto.MeetingDashboardDto;
import com.app.dto.MeetingDto;
import com.app.dto.MeetingHourDto;
import com.app.dto.NotificationDTO;
import com.app.dto.PaginatedMeetingDashboard;
import com.app.dto.PaginatedMeetingResponse;
import com.app.dto.PaginationRequest;
import com.app.dto.RoomDto;
import com.app.dto.UpdateMeetingDto;
import com.app.dto.VisitorMeetingDto;
import com.app.emun.MeetingContext;
import com.app.emun.MeetingStatus;
import com.app.entity.Meeting;
import com.app.entity.MeetingRoomTrial;
import com.app.entity.Notification;
import com.app.entity.Room;
import com.app.entity.User;
import com.app.entity.Visitor;
import com.app.mailservice.EmailSendService;
import com.app.response.Response;
import com.app.security.JwtHelper;
import com.app.service.MeetingService;
import com.app.service.NotificationService;
import com.app.service.NotificationText;
import com.app.service.PdfService;
import com.app.service.RoomService;
import com.app.util.ExcelHepler;
import com.app.validation.VallidationClass;

@Service
public class MeetingServiceImpl implements MeetingService {

	@Autowired
	private MeetingDao meetingDao;

	@Value("${project.excel}")
	private String excelDir;

	@Override
	public List<Meeting> getMeetingsByVisitor(Visitor visitor) {

		return meetingDao.getMeetingsByVisitor(visitor);
	}

	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private SmsService smsService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private PdfService pdfService;

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private RoomService roomService;

	@Autowired
	private VisitorDao visitorDao;

	@Autowired
	private EmailSendService emailSendService;

	@Autowired
	private VallidationClass validationClass;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private NotificationText notificationText;

	@Autowired
	private JwtHelper jwtHelper;

	@Autowired
	private MeetingRoomTrailDao roomTrailDao;

	@Override
	public Response<?> addMeeting(MeetingDto meetingDto) throws Exception {
		User user = userDao.getuserByid(meetingDto.getUser().getId());
		try {
			Visitor visitor = visitorDao.serachVisitor(meetingDto.getPhone());

			if (visitor == null) {
				return new Response<>("Phone Number Does' Exit", null, HttpStatus.NOT_FOUND.value());
			}

			if (this.validationClass.checkMeeting(meetingDto).getStatus() != HttpStatus.OK.value()) {
				return this.validationClass.checkMeeting(meetingDto);
			}

			if (user == null) {
				return new Response<>("Employee Not found", null, HttpStatus.BAD_REQUEST.value());
			}
			Date date = null;

			List<Meeting> list = this.meetingDao.getMeetingByVisitorIdAndTodayDate(visitor.getId());
			if (!list.isEmpty()) {
				for (Meeting meeting : list) {
					if (meeting.getStatus().equals(MeetingStatus.APPROVED)
							|| meeting.getStatus().equals(MeetingStatus.PENDING)) {
						return new Response<>("Kindly complete your first meeting", null,
								HttpStatus.BAD_REQUEST.value());
					}
					if (meeting.getStatus().equals(MeetingStatus.INPROCESS)) {
						meeting.setStatus(MeetingStatus.COMPLETED);
						meeting.setMeetingEndDateTime(new Date());
						meeting.setUpdatedAt(new Date());
						this.meetingDao.update(meeting);
						Room room = meeting.getRoom();

//						room.setIsAvailable(true);
//						this.roomDao.update(room);
						this.roomDao.roomAvailability(new IsActiveDto(room.getId(), true));
						date = meeting.getCheckInDateTime();
					}
				}
			}

			Meeting meeting = new Meeting();
			meeting.setId(meetingDto.getId());
			meeting.setEmployee(user);
			meeting.setVisitor(visitor);
			meeting.setContext(meetingDto.getContext());
			meeting.setRemarks(meetingDto.getRemarks());
			meeting.setMeetingStartDateTime(meetingDto.getMeetingStartDateTime());
			meeting.setMeetingEndDateTime(meetingDto.getMeetingEndDateTime());
			meeting.setStatus(MeetingStatus.PENDING);
			meeting.setCompany(meeting.getEmployee().getCompany());

			meeting.setIsActive(true);
			meeting.setCreatedAt(new Date());
			meeting.setUpdatedAt(new Date());
			meeting.setUpdatedBy(null);
			meeting.setRoomChanged(false);
			if (date != null) {
				meeting.setCheckInDateTime(date);
			} else {
				meeting.setCheckInDateTime(new Date());
			}
			Meeting save = meetingDao.save(meeting);

			NotificationDTO notificationDTO = new NotificationDTO();
			notificationDTO.setText(this.notificationText.pending(MeetingDto.convertToDTO(save)));
			notificationDTO.setSeen(false);
			notificationDTO.setUserRole(meeting.getEmployee().getRole().getName());
			notificationDTO.setMeeting(MeetingDto.convertToDTO(save));
			this.notificationService.save(notificationDTO);

			// Notification to Receptionist

			NotificationDTO notificationToReceptionist = new NotificationDTO();
			notificationToReceptionist
					.setText(user.getFirstname() + " has a new meeting request with " + visitor.getName());
			notificationToReceptionist.setSeen(false);
			notificationToReceptionist.setUserRole("RECEPTIONIST");
			notificationToReceptionist.setMeeting(MeetingDto.convertToDTO(save));
			this.notificationService.save(notificationToReceptionist);

			Thread emailThread = new Thread(() -> {

				String subject = "Meeting Request";
				String companySection = visitor.getCompanyName() != null
						? "<p><strong>Company:</strong> " + visitor.getCompanyName() + "</p>\n"
						: "";

				String emailContent = "<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "<head>\n"
						+ "    <meta charset=\"UTF-8\">\n"
						+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
						+ "    <title>Meeting Request</title>\n" + "    <style>\n" + "        body {\n"
						+ "            font-family: Arial, sans-serif;\n" + "            background-color: #f4f4f4;\n"
						+ "        }\n" + "        .container {\n" + "            max-width: 600px;\n"
						+ "            margin: 0 auto;\n" + "            padding: 20px;\n"
						+ "            background-color: #ffffff;\n"
						+ "            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);\n" + "        }\n"
						+ "        h1 {\n" + "            color: #007bff;\n" + "        }\n" + "        p {\n"
						+ "            font-size: 16px;\n" + "            line-height: 1.5;\n"
						+ "            margin: 10px 0;\n" + "        }\n" + "    </style>\n" + "</head>\n" + "<body>\n"
						+ "    <div class=\"container\">\n" + "        <h1>Meeting Request</h1>\n"
						+ "        <p><strong>Name:</strong> " + visitor.getName() + "</p>\n" + "</p>\n" + "</p>\n"
						+ companySection + "\n"
						+ "        <p><strong>URL :</strong> <a href=\"https://vms.nyggs.com/\">Go to log in</a></p>\n"
						+ "        <p><strong>Context:</strong> PENDING</p>\n" + "    </div>\n" + "</body>\n"
						+ "</html>";

				String toEmail = user.getEmail();
				String phoneNo = user.getPhone();

				try {

					// Notification to Host

					emailSendService.sendEmail(toEmail, subject, emailContent);
					smsService.sendSmstoUser(phoneNo, visitor);

				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			emailThread.start();

			return new Response<>("Meeting Added Successfully", MeetingDto.convertToDTO(save), HttpStatus.OK.value());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public Response<?> isEmployeeBusy(@RequestParam Integer id,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") Date meetingStartDate,
			@RequestParam Integer duration) {
		long durationInMilliseconds = duration * 60 * 1000;
		Date meetingEndDate = new Date(meetingStartDate.getTime() + durationInMilliseconds);
		Meeting meeting = this.meetingDao.isEmployeeBusy(id, meetingStartDate, meetingEndDate);
		if (meeting == null) {
			return new Response<>("No meeting found", null, HttpStatus.NOT_FOUND.value());
		}
		return new Response<>("Success", meeting, HttpStatus.OK.value());
	}

	public String convertTime(Date date) {
		System.out.println("Date: " + date);

		// Create a calendar instance and add 5.5 hours to the date
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, 5);
		calendar.add(Calendar.MINUTE, 30);

		// Format the updated date as time
		SimpleDateFormat localTimeFormatter = new SimpleDateFormat("HH:mm");
		localTimeFormatter.setTimeZone(TimeZone.getDefault());
		String localTime = localTimeFormatter.format(calendar.getTime());

		return localTime;
	}

	@Override
	public Response<?> getmeetingWithstatusByuser(Integer id) {

		List<Meeting> meetings = meetingDao.getMeetingsByUserId(id);
		if (meetings != null) {
			List<MeetingDto> allmeeting = new ArrayList<>();
			meetings.forEach(x -> {
				MeetingDto convertToDTO = null;
				try {
					convertToDTO = MeetingDto.convertToDTO(x);
				} catch (Exception e) {
					e.printStackTrace();
				}
				allmeeting.add(convertToDTO);
			});
			return new Response<>("Meeting Detalis", allmeeting, HttpStatus.OK.value());
		}
		return new Response<>(" No Recod Found ", null, HttpStatus.NO_CONTENT.value());
	}

	@Override
	public Response<?> getallmeetingWithstatus() {

		List<Meeting> all = meetingDao.getAll(0);

		if (!all.isEmpty()) {
			List<MeetingDto> allmeeting = new ArrayList<>();
			all.forEach(x -> {
				MeetingDto convertToDTO = null;
				try {
					convertToDTO = MeetingDto.convertToDTO(x);
				} catch (Exception e) {
					e.printStackTrace();
				}
				allmeeting.add(convertToDTO);
			});

			return new Response<>(" All Meeting Detalis", allmeeting, HttpStatus.OK.value());
		}
		return new Response<>(" No Recod Found ", null, HttpStatus.NO_CONTENT.value());
	}

	@Override
	public Response<?> updatemeetingStatus(UpdateMeetingDto meetingDTO, HttpServletRequest request) throws Exception {
		String requestTokenHeader = request.getHeader("Authorization");
		String phone = jwtHelper.getUsernameFromToken(requestTokenHeader.substring(7));
		User user = this.userDao.getUserbyPhone(phone);

		try {
			if (user == null) {
				return new Response<>("User not found", null, HttpStatus.NOT_FOUND.value());
			}

			if (meetingDTO.getId() == null) {
				return new Response<>("Invalid meeting ID", null, HttpStatus.BAD_REQUEST.value());
			}
			Meeting meeting = meetingDao.getById(meetingDTO.getId());

			if (meeting == null) {
				return new Response<>("Meeting not found", null, HttpStatus.NOT_FOUND.value());
			}

			if (meeting.getStatus().equals(MeetingStatus.COMPLETED)) {
				return new Response<>("Meeting is completed", null, HttpStatus.BAD_REQUEST.value());
			}

			Visitor visitor = this.visitorDao.getVisitorById(meeting.getVisitor().getId());
			if (visitor == null) {
				return new Response<>("Visitor not found", null, HttpStatus.NOT_FOUND.value());
			}

			if (!user.getId().equals(meeting.getEmployee().getId())
					&& !user.getRole().getName().equals("RECEPTIONIST")) {
				return new Response<>("You cannot make changes on others meeting", null,
						HttpStatus.BAD_REQUEST.value());
			}

			if (meetingDTO.getStatus().equals(MeetingStatus.APPROVED)
					|| meetingDTO.getStatus().equals(MeetingStatus.CANCELLED)) {
				if (user.getRole().getName().equals("RECEPTIONIST")) {
					if (meetingDTO.getStatus().equals(MeetingStatus.CANCELLED)) {

						meeting.setStatus(MeetingStatus.CANCELLED);
						meeting.setUpdatedBy("R");
						meeting.setUpdatedAt(new Date());
						Meeting cancelledMeeting = this.meetingDao.update(meeting);

//						Thread helperThread = new Thread(() -> {
//
//							try {
//								Room room = meeting.getRoom();
//								if (room != null) {
//									this.roomDao.roomAvailability(new IsActiveDto(room.getId(), true));
//								}
//								helperFunction(meetingDTO, cancelledMeeting, user);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						});
//
//						helperThread.start();
						
						helperFunction(meetingDTO, cancelledMeeting, user);

						return new Response<>("Meeting is cancelled", null, HttpStatus.OK.value());
					}

					if (meeting.getStatus().equals(MeetingStatus.PENDING)) {

						if (meeting.getRoom() == null && meetingDTO.getRoom() == null) {
							return new Response<>("Room is not alloted yet", null, HttpStatus.BAD_REQUEST.value());
						}

						Room room = this.roomDao.getById(meetingDTO.getRoom().getId());
						if (room == null) {
							return new Response<>("Invalid room ID", null, HttpStatus.BAD_REQUEST.value());
						}

						if (!room.getIsAvailable()) {
							return new Response<>("Room is not available", null, HttpStatus.OK.value());
						}

						meeting.setRoom(room);
						this.roomDao.roomAvailability(new IsActiveDto(room.getId(), false));

						MeetingRoomTrial trialData = new MeetingRoomTrial();
						trialData.setMeetingId(meeting.getId());
						trialData.setRoomId(room.getId());
						trialData.setText("Room Assigned");
						trialData.setCreatedBy(user.getId());
						trialData.setCreatedAt(new Date());
						roomTrailDao.save(trialData);

						meeting.setMeetingStartDateTime(new Date());
						meeting.setStatus(MeetingStatus.APPROVED);
						meeting.setUpdatedAt(new Date());
						meeting.setUpdatedBy("R");
						Meeting updateMeeting = meetingDao.update(meeting);
//						Thread helperThread = new Thread(() -> {
//
//							try {
//								helperFunction(meetingDTO, updateMeeting, user);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						});
//
//						helperThread.start();
						helperFunction(meetingDTO, updateMeeting, user);
						return new Response<>("Meeting Added Successfully ", MeetingDto.convertToDTO(updateMeeting),
								HttpStatus.OK.value());

					} else if (meeting.getStatus().equals(MeetingStatus.APPROVED)) {
						if (meeting.getMeetingStartDateTime() != null) {
							LocalDate todayDateUTC = LocalDate.now(ZoneOffset.UTC);
							LocalDate meetingStartDateUTC = meeting.getMeetingStartDateTime().toInstant()
									.atZone(ZoneOffset.UTC).toLocalDate();
							System.out.println("Today date, " + todayDateUTC);
							System.out.println("Meeting date, " + meetingStartDateUTC);
							if (!todayDateUTC.isEqual(meetingStartDateUTC)) {
								return new Response<>("Meeting is not scheduled today", null,
										HttpStatus.BAD_REQUEST.value());
							}

						}

						if (meeting.getCheckInDateTime() == null) {
							meeting.setCheckInDateTime(new Date());
							meeting.setMeetingStartDateTime(new Date());
						}

						Room room = this.roomDao.getById(meetingDTO.getRoom().getId());
						if (room == null) {
							return new Response<>("Invalid room ID", null, HttpStatus.BAD_REQUEST.value());
						}
						if (!room.getIsAvailable()) {
							return new Response<>("Room is not available", null, HttpStatus.BAD_REQUEST.value());
						}

						if (meeting.getRoom() != null) {
							if (meetingDTO.getRoom().getId() != meeting.getRoom().getId()) {
								Room alreadyAssignedRoom = meeting.getRoom();

								this.roomDao.roomAvailability(new IsActiveDto(alreadyAssignedRoom.getId(), true));

								Room roomToBeUpdated = roomDao.getById(meetingDTO.getRoom().getId());
								if (!roomToBeUpdated.getIsActive() || !roomToBeUpdated.getIsAvailable()) {
									return new Response<>("Room is not available", null,
											HttpStatus.BAD_REQUEST.value());
								}
								roomToBeUpdated.setUpdatedAt(new Date());
								this.roomDao.roomAvailability(new IsActiveDto(roomToBeUpdated.getId(), false));
								meeting.setRoom(roomToBeUpdated);
								meeting.setRoomChanged(true);

								meeting.setUpdatedAt(new Date());
								Meeting updateMeeting = meetingDao.update(meeting);

								MeetingRoomTrial trialData = new MeetingRoomTrial();
								trialData.setMeetingId(meeting.getId());
								trialData.setRoomId(roomToBeUpdated.getId());
								trialData.setText("Room Updated");
								trialData.setCreatedBy(user.getId());
								trialData.setCreatedAt(new Date());
								roomTrailDao.save(trialData);

								List<Notification> notificationsList = this.notificationDao
										.getNotificationsByMeeting(meeting);

								for (Notification notification : notificationsList) {
									if (notification.getUserRole().equals("RECEPTIONIST")) {
										notification.setText(
												"Room change for meeting with " + meeting.getEmployee().getFirstname()
														+ " and " + meeting.getVisitor().getName() + " - New room: "
														+ roomToBeUpdated.getRoomName());
										;
										notification.setUpdatedAt(new Date());
										notification.setSeen(false);
										notificationDao.update(notification);
									} else {
										notification.setText("Room has been changed by receptionist for meeting with "
												+ meeting.getVisitor().getName() + " - New room: "
												+ roomToBeUpdated.getRoomName());
										;
										notification.setUpdatedAt(new Date());
										notification.setSeen(false);
										notificationDao.update(notification);
									}
								}

								return new Response<>("Room Updated Successfully",
										MeetingDto.convertToDTO(updateMeeting), HttpStatus.OK.value());

							}
						} else {
							meeting.setRoom(room);
							this.roomDao.roomAvailability(new IsActiveDto(room.getId(), false));
							MeetingRoomTrial trialData = new MeetingRoomTrial();
							trialData.setMeetingId(meeting.getId());
							trialData.setRoomId(room.getId());
							trialData.setText("Room Assigned");
							trialData.setCreatedBy(user.getId());
							trialData.setCreatedAt(new Date());
							roomTrailDao.save(trialData);

							meeting.setStatus(MeetingStatus.INPROCESS);
							meeting.setUpdatedBy(null);
							meeting.setUpdatedAt(new Date());
							Meeting updateMeeting = meetingDao.update(meeting);

//							Thread helperThread = new Thread(() -> {
//
//								try {
//									helperFunction(meetingDTO, updateMeeting, user);
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//							});
//
//							helperThread.start();
							
							helperFunction(meetingDTO, updateMeeting, user);
							
							return new Response<>("Meeting Added Successfully", MeetingDto.convertToDTO(updateMeeting),
									HttpStatus.OK.value());

						}
					}

				} else {
					if (meetingDTO.getStatus().equals(MeetingStatus.APPROVED)) {
						@SuppressWarnings("unchecked")
						List<RoomDto> list = (List<RoomDto>) this.roomService
								.getAvailableRooms(meeting.getEmployee().getCompany().getId()).getData();

						List<RoomDto> filterList = list.stream().filter(room -> room.getIsAvailable())
								.collect(Collectors.toList());

						Room selectedRoom = this.roomService.allocateRoomsRandomly(filterList);
						if (selectedRoom != null) {
							meeting.setRoom(selectedRoom);
							this.roomDao.roomAvailability(new IsActiveDto(selectedRoom.getId(), false));

							MeetingRoomTrial trialData = new MeetingRoomTrial();
							trialData.setMeetingId(meeting.getId());
							trialData.setRoomId(selectedRoom.getId());
							trialData.setText("Room Assigned");
							trialData.setCreatedBy(user.getId());
							trialData.setCreatedAt(new Date());
							roomTrailDao.save(trialData);
						}
					}
					meeting.setStatus(meetingDTO.getStatus());
					meeting.setMeetingStartDateTime(new Date());
					meeting.setUpdatedAt(new Date());
					if (meetingDTO.getStatus().equals(MeetingStatus.CANCELLED)) {
						meeting.setMeetingStartDateTime(null);
						if (meeting.getRoom() != null) {
							this.roomDao.roomAvailability(new IsActiveDto(meeting.getRoom().getId(), true));

						}
					}

					meeting.setUpdatedBy("H");
					Meeting updateMeeting = meetingDao.update(meeting);
//
//					Thread helperThread = new Thread(() -> {
//
//						try {
//							helperFunction(meetingDTO, updateMeeting, user);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					});
//
//					helperThread.start();
					
					helperFunction(meetingDTO, updateMeeting, user);

					return new Response<>("Updated Successfully", meetingDTO, HttpStatus.OK.value());
				}
			}
			return new Response<>("You cannot update a meeting now", null, HttpStatus.BAD_REQUEST.value());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Fail", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	@Override
	public Response<?> addMeetingByuser(VisitorMeetingDto visitorDto) {
		
		try {
			User user = userDao.getuserByid(visitorDto.getUser().getId());
			Visitor visitor = visitorDao.serachVisitor(visitorDto.getPhoneNumber());

			Meeting meeting = new Meeting();
			meeting.setStatus(MeetingStatus.APPROVED);
			meeting.setCreatedAt(new Date());
			meeting.setUpdatedAt(new Date());
			meeting.setMeetingStartDateTime(visitorDto.getMeetingStartDateTime());
			meeting.setIsActive(true);
			meeting.setEmployee(user);
			meeting.setRemarks(visitorDto.getRemarks());
			meeting.setVisitor(visitor);
			meeting.setContext(visitorDto.getContext());
			meeting.setUpdatedBy("H");
			meeting.setCompany(meeting.getEmployee().getCompany());
			Meeting savedMeeting = meetingDao.save(meeting);
			
			NotificationDTO notificationDTO = new NotificationDTO();
			notificationDTO.setText("You have scheduled a new meeting with " + savedMeeting.getVisitor().getName());
			notificationDTO.setSeen(false);
			notificationDTO.setUserRole(meeting.getEmployee().getRole().getName());
			notificationDTO.setMeeting(MeetingDto.convertToDTO(savedMeeting));
			this.notificationService.save(notificationDTO);

			// Notification to Receptionist

			NotificationDTO notificationToReceptionist = new NotificationDTO();
			notificationToReceptionist.setText("Host - " + savedMeeting.getEmployee().getFirstname()
					+ " has scheduled a new meeting with " + savedMeeting.getVisitor().getName());
			notificationToReceptionist.setSeen(false);
			notificationToReceptionist.setUserRole("RECEPTIONIST");
			notificationToReceptionist.setMeeting(MeetingDto.convertToDTO(savedMeeting));
			this.notificationService.save(notificationToReceptionist);

			Thread emailThread = new Thread(() -> {

//				String subject = "Meeting scheduled";
//				String companySection = visitor.getCompanyName() != null
//						? "<p><strong>Company:</strong> " + visitor.getCompanyName() + "</p>\n"
//						: "";
	//
//				String fileName = meeting.getVisitor().getName() + "_" + meeting.getVisitor().getPhoneNumber()
//						+ "_VisitorPass.pdf";

				try {

					// Notification to Host

//					NotificationDTO notificationDTO = new NotificationDTO();
//					notificationDTO.setText("You have scheduled a new meeting with " + savedMeeting.getVisitor().getName());
//					notificationDTO.setSeen(false);
//					notificationDTO.setUserRole(meeting.getEmployee().getRole().getName());
//					notificationDTO.setMeeting(MeetingDto.convertToDTO(savedMeeting));
//					this.notificationService.save(notificationDTO);
	//
//					// Notification to Receptionist
	//
//					NotificationDTO notificationToReceptionist = new NotificationDTO();
//					notificationToReceptionist.setText("Host - " + savedMeeting.getEmployee().getFirstname()
//							+ " has scheduled a new meeting with " + savedMeeting.getVisitor().getName());
//					notificationToReceptionist.setSeen(false);
//					notificationToReceptionist.setUserRole("RECEPTIONIST");
//					notificationToReceptionist.setMeeting(MeetingDto.convertToDTO(savedMeeting));
//					this.notificationService.save(notificationToReceptionist);
					String subject = "Meeting scheduled";
					String companySection = visitor.getCompanyName() != null
							? "<p><strong>Company:</strong> " + visitor.getCompanyName() + "</p>\n"
							: "";

					String fileName = meeting.getVisitor().getName() + "_" + meeting.getVisitor().getPhoneNumber()
							+ "_VisitorPass.pdf";
					byte[] generateVisitorPassPDF = pdfService.generateVisitorPassPDF(MeetingDto.convertToDTO(meeting));
					String toEmail = visitor.getEmail();

					String content = String.format("<div class='container'>" + "<h1>%s</h1>"
//							+ "<p>Meeting Start: %s</p>" + "<p> Meeting End: %s</p>" + "<p>Status: %s</p>"
							+ "<p> Your request has been Approved</p>" + "<p> Download your pass </p>" + "</div>",
							meeting.getVisitor().getName(), meeting.getStatus());

					emailSendService.sendEmail(toEmail, subject, content, generateVisitorPassPDF, fileName);

				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			emailThread.start();

			return new Response<>("Meeting Scheduled Succesfully", null, HttpStatus.OK.value());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Something went  wrong", null, HttpStatus.BAD_REQUEST.value());
		}
		
		

	}

	@Override
	public Response<?> updateCheckoutStatus(Visitor visitor) throws Exception {

		List<Meeting> list = this.meetingDao.getMeetingByVisitorIdAndTodayDate(visitor.getId());
		try {
			if (!list.isEmpty()) {
				for (Meeting meeting : list) {

					if (meeting.getStatus().equals(MeetingStatus.PENDING)
							|| meeting.getStatus().equals(MeetingStatus.APPROVED)) {
//						meeting.setStatus(MeetingStatus.CANCELLED_BY_VISITOR);
						meeting.setStatus(MeetingStatus.CANCELLED);
						meeting.setUpdatedBy("V");
						// Mail and notification to be sent to the employee

//						String subject = "Meeting Cancelled By Visitor";
//
//						String content = String.format(
//								"<div class='container'>" + "<h1>Visitor: %s</h1>" + "<p>Status: %s</p>"
//										+ "<p>Remarks: %s</p>" + "<p>%s</p>" + "</div>",
//								meeting.getVisitor().getName(), meeting.getStatus(), meeting.getRemarks(), subject);

						List<Notification> notificationsList = this.notificationDao.getNotificationsByMeeting(meeting);

						if (!notificationsList.isEmpty()) {
							for (Notification notification : notificationsList) {
								if (notification.getUserRole().equals("RECEPTIONIST")) {
									notification.setText("Visitor - " + meeting.getVisitor().getName()
											+ " has checked out. Meeting with " + meeting.getEmployee().getFirstname()
											+ " has been cancelled");
								} else {
									notification.setText("Visitor - " + meeting.getVisitor().getName()
											+ " has checked out. Meeting is cancelled");
								}
								notification.setUpdatedAt(new Date());
								notification.setSeen(false);
								this.notificationDao.update(notification);
							}
						}

						Thread emailThread = new Thread(() -> {

							try {

								String subject = "Meeting Cancelled By Visitor";

								String content = String.format(
										"<div class='container'>" + "<h1>Visitor: %s</h1>" + "<p>Status: %s</p>"
												+ "<p>Remarks: %s</p>" + "<p>%s</p>" + "</div>",
										meeting.getVisitor().getName(), meeting.getStatus(), meeting.getRemarks(),
										subject);

//								if (!notificationsList.isEmpty()) {
//									for (Notification notification : notificationsList) {
//										if (notification.getUserRole().equals("RECEPTIONIST")) {
//											notification.setText("Visitor - " + meeting.getVisitor().getName()
//													+ " has checked out. Meeting with "
//													+ meeting.getEmployee().getFirstname() + " has been cancelled");
//										} else {
//											notification.setText("Visitor - " + meeting.getVisitor().getName()
//													+ " has checked out. Meeting is cancelled");
//										}
//										notification.setUpdatedAt(new Date());
//										notification.setSeen(false);
//										this.notificationDao.update(notification);
//									}
//								}

								emailSendService.sendEmail(meeting.getEmployee().getEmail(), subject, content);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});

						emailThread.start();

					}
					if (meeting.getMeetingEndDateTime() == null) {

						if (meeting.getRoom() != null) {
							Room room = this.roomDao.getById(meeting.getRoom().getId());
							if (room != null) {
								this.roomDao.roomAvailability(new IsActiveDto(room.getId(), true));
							}

						}

					}
					if (meeting.getStatus().equals(MeetingStatus.INPROCESS)) {
						meeting.setStatus(MeetingStatus.COMPLETED);
						meeting.setUpdatedBy("V");
						meeting.setMeetingEndDateTime(new Date());
					}

					meeting.setCheckOutDateTime(new Date());
					meeting.setUpdatedAt(new Date());

					this.meetingDao.update(meeting);
				}

			}
			return new Response<>("success", null, HttpStatus.OK.value());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Fail", null, HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public Response<?> updateCheckoutStatusByReceptionist(Visitor visitor) throws Exception {
		List<Meeting> list = this.meetingDao.getMeetingByVisitorIdAndTodayDate(visitor.getId());
		try {
			if (!list.isEmpty()) {
				for (Meeting meeting : list) {

					if (meeting.getStatus().equals(MeetingStatus.PENDING)
							|| meeting.getStatus().equals(MeetingStatus.APPROVED)) {
//						meeting.setStatus(MeetingStatus.CANCELLED_BY_VISITOR);
						meeting.setStatus(MeetingStatus.CANCELLED);
						meeting.setUpdatedBy("V");
						// Mail and notification to be sent to the employee

//						String subject = "Meeting Cancelled By Visitor";
//
//						String content = String.format(
//								"<div class='container'>" + "<h1>Visitor: %s</h1>" + "<p>Status: %s</p>"
//										+ "<p>Remarks: %s</p>" + "<p>%s</p>" + "</div>",
//								meeting.getVisitor().getName(), meeting.getStatus(), meeting.getRemarks(), subject);

						List<Notification> notificationsList = this.notificationDao.getNotificationsByMeeting(meeting);

						if (!notificationsList.isEmpty()) {
							for (Notification notification : notificationsList) {
								if (notification.getUserRole().equals("RECEPTIONIST")) {
									notification.setText("Visitor - " + meeting.getVisitor().getName()
											+ " has checked out. Meeting with " + meeting.getEmployee().getFirstname()
											+ " has been cancelled");
								} else {
									notification.setText("Visitor - " + meeting.getVisitor().getName()
											+ " has checked out. Meeting is cancelled");
								}
								notification.setUpdatedAt(new Date());
								notification.setSeen(false);
								this.notificationDao.update(notification);
							}
						}

						Thread emailThread = new Thread(() -> {

							try {

								String subject = "Meeting Cancelled By Visitor";

								String content = String.format(
										"<div class='container'>" + "<h1>Visitor: %s</h1>" + "<p>Status: %s</p>"
												+ "<p>Remarks: %s</p>" + "<p>%s</p>" + "</div>",
										meeting.getVisitor().getName(), meeting.getStatus(), meeting.getRemarks(),
										subject);

//								if (!notificationsList.isEmpty()) {
//									for (Notification notification : notificationsList) {
//										if (notification.getUserRole().equals("RECEPTIONIST")) {
//											notification.setText("Visitor - " + meeting.getVisitor().getName()
//													+ " has checked out. Meeting with "
//													+ meeting.getEmployee().getFirstname() + " has been cancelled");
//										} else {
//											notification.setText("Visitor - " + meeting.getVisitor().getName()
//													+ " has checked out. Meeting is cancelled");
//										}
//										notification.setUpdatedAt(new Date());
//										notification.setSeen(false);
//										this.notificationDao.update(notification);
//									}
//								}

								emailSendService.sendEmail(meeting.getEmployee().getEmail(), subject, content);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});

						emailThread.start();

					}
					if (meeting.getMeetingEndDateTime() == null) {

						if (meeting.getRoom() != null) {
							Room room = this.roomDao.getById(meeting.getRoom().getId());
							if (room != null) {
								this.roomDao.roomAvailability(new IsActiveDto(room.getId(), true));
							}

						}

					}
					if (meeting.getStatus().equals(MeetingStatus.INPROCESS)) {
						meeting.setStatus(MeetingStatus.COMPLETED);
						meeting.setMeetingEndDateTime(new Date());
						meeting.setUpdatedBy("R");
					}

					meeting.setCheckOutDateTime(new Date());
					meeting.setUpdatedAt(new Date());
					this.meetingDao.update(meeting);
				}

			}
			return new Response<>("success", null, HttpStatus.OK.value());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Fail", null, HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public Response<?> updateMeeting(MeetingDto meetingDto) {
//		Visitor visitor = visitorDao.getVisitorById(meetingDto.getVisitor().getId());

//		Meeting meeting = meetingDao.getMeetingByVisitorIdAndTodayDate(meetingDto.getVisitor().getId());
//
////		if (meeting == null) {
////			
////			return addMeeting(visitorDto);
////	
////		}
//
//		if (meeting.getStatus().equals(MeetingStatus.APPROVED)) {
//
//			meeting.setCheckInDateTime(new Date());
//
//			meeting.setStatus(MeetingStatus.INPROCESS);
//
//			meetingDao.update(meeting);
//
//			return new Response<>("Check In succesfully", null, HttpStatus.OK.value());
//		} else {
//			return new Response<>("meeting is not approved !! ", null, HttpStatus.OK.value());
//		}

		return null;
	}

	@Override
	public Response<?> getMeetingsByPagination(PaginationRequest request) {

		Page<Meeting> meetings = meetingDao.getMeetingsByPagination(request);

		int pageSize = meetings.getSize();

		long totalElements = meetings.getTotalElements();

		int totalPages = meetings.getTotalPages();

		List<Meeting> content = meetings.getContent();

		Date today = new Date();

		List<MeetingDto> meetingDtos;

		meetingDtos = content.stream().sorted((m1, m2) -> {

			Date date1 = m1.getCreatedAt();

			Date date2 = m1.getCreatedAt();

			// Check if both dates are on the same day
			if (isSameDay(date1, today) && isSameDay(date2, today)) {
				return date1.compareTo(date2); // Sort meetings occurring on today's date by time
			} else if (isSameDay(date1, today)) {
				return -1; // Date1 is on today, so it comes first
			} else if (isSameDay(date2, today)) {
				return 1; // Date2 is on today, so it comes first
			} else {
				return date1.compareTo(date2); // Sort other dates
			}
		}).map(t -> {
			try {
				return MeetingDto.convertToDTO(t);
			} catch (Exception e) {
				// Handle the exception, e.g., log it and provide a default DTO
				e.printStackTrace();
				return new MeetingDto(); // Provide a default DTO or handle the exception as needed
			}
		}).collect(Collectors.toList());

		PaginatedMeetingResponse obj = new PaginatedMeetingResponse(pageSize, totalElements, totalPages, meetingDtos);

		long meetingCount = meetingDtos.size();

		obj.setTotalMeeting(meetingCount);

		return new Response<>("All Meetings", obj, HttpStatus.OK.value());
	}

	public long countMeetingByStatus(MeetingStatus status, List<MeetingDto> meetings) {

		long count = meetings.stream().filter(meeting -> meeting.getStatus() == status).count();

		return count;
	}

	public long countMeetingByStatusDashboard(MeetingStatus status, List<Meeting> meetings) {

		long count = meetings.stream().filter(meeting -> meeting.getStatus() == status).count();

		return count;
	}

	@Override
	public List<MeetingDto> getMeetingByPaginationExcel(PaginationRequest request) {

		Page<Meeting> meetings = meetingDao.getMeetingsByPagination(request);

		List<Meeting> content = meetings.getContent();

		List<MeetingDto> meetingDtos;

		meetingDtos = content.stream().sorted((m2, m1) -> {

			Date checkInDateTime2 = m2.getCreatedAt();
			Date checkInDateTime1 = m1.getCreatedAt();

			if (checkInDateTime1 == null && checkInDateTime2 == null) {
				return 0;
			} else if (checkInDateTime1 == null) {
				return 1;
			} else if (checkInDateTime2 == null) {
				return -1;
			} else {
				return checkInDateTime1.compareTo(checkInDateTime2);
			}
		}).map(t -> {
			try {
				return MeetingDto.convertToDTO(t);
			} catch (Exception e) {
				// Handle the exception, e.g., log it and provide a default DTO
				e.printStackTrace();
				return new MeetingDto(); // Provide a default DTO or handle the exception as needed
			}
		}).collect(Collectors.toList());

		return meetingDtos;

	}

	private boolean isSameDay(Date date1, Date date2) {

		java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyyMMdd");

		return fmt.format(date1).equals(fmt.format(date2));
	}

	@Override
	public Response<?> getMeetingByPaginationDashBoardReceptionist(PaginationRequest request) {

		Page<Meeting> meetings = meetingDao.getMeetingsByPagination(request);

		Date today = new Date();

		List<Meeting> content = meetings.getContent();

		List<Meeting> todaysMeetings = content.stream().filter(m -> isSameDay(m.getCreatedAt(), today))
				.collect(Collectors.toList());

		long pendingMeetings = countMeetingByStatusDashboard(MeetingStatus.PENDING, todaysMeetings);
		long inProcessMeeting = countMeetingByStatusDashboard(MeetingStatus.INPROCESS, todaysMeetings);
		long approvedMeeting = countMeetingByStatusDashboard(MeetingStatus.APPROVED, todaysMeetings);
		long completedMeeting = countMeetingByStatusDashboard(MeetingStatus.COMPLETED, todaysMeetings);
		long canceledMeeting = countMeetingByStatusDashboard(MeetingStatus.CANCELLED, todaysMeetings);

		long totalElements = pendingMeetings + inProcessMeeting + approvedMeeting + completedMeeting + canceledMeeting;

		List<Room> allRooms = roomDao.getAllactive2(request.getCompanyId(), request.getBuildingId());

		long totalRooms = allRooms.size();
		long availableRooms = allRooms.stream().filter(room -> room.getIsAvailable() == true).count();
		long busyRooms = allRooms.stream().filter(room -> room.getIsAvailable() == false).count();

		PaginatedMeetingDashboard obj = new PaginatedMeetingDashboard();

		obj.setTotalElements(totalElements);
		obj.setTotalMeeting(totalElements);
		obj.setTotalPending(pendingMeetings);
		obj.setTotalApproved(approvedMeeting);
		obj.setTotalInProcess(inProcessMeeting);
		obj.setTotalCompleted(completedMeeting);

		obj.setTotalAvailableRoom(availableRooms);
		obj.setTotalBusyRooms(busyRooms);
		obj.setTotalRooms(totalRooms);

		List<MeetingHourDto> meetingHours = meetingDao.getMeetingHoursLastWeek(content, request.getToDate());

		double meetingAvg = 0.0;
		double noOfMeeting = 0.0;

		for (MeetingHourDto hour : meetingHours) {
			meetingAvg += hour.getTotalMeetingHours();
			noOfMeeting += hour.getTotalMeeting();
		}

		double numWeeks = meetingHours.size();
		double avgHoursPerWeek = meetingAvg / numWeeks;

		avgHoursPerWeek = new BigDecimal(meetingAvg / numWeeks).setScale(3, RoundingMode.HALF_UP).doubleValue();

		obj.setAvgHoursPerWeek(avgHoursPerWeek);
		obj.setTotalMeetingPerWeek(noOfMeeting);
		obj.setMeetingHours(meetingHours);

		return new Response<>("All Meetings", obj, HttpStatus.OK.value());
	}

	@Override
	public Response<?> getMeetingByid(int id) {
		try {

			Meeting meeting = meetingDao.getById(id);
			if (meeting != null) {
				MeetingDto convertToDTO = MeetingDto.convertToDTO(meeting);
				return new Response<>("Success", convertToDTO, HttpStatus.OK.value());
			}
			return new Response<>("No meeting found", null, HttpStatus.BAD_REQUEST.value());
		} catch (Exception e) {
			return new Response<>("Some thing Went Wrong", null, HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public ByteArrayInputStream generateExcel(List<MeetingDto> meetingsByPagination) {

		return ExcelHepler.dataToExcel(meetingsByPagination);
	}

	@Override
	public Response<?> getmeetingForDashboardByuser(PaginationRequest request) {

		List<Meeting> meetingsByUserId = meetingDao.getMeetingsByUserIdForDashboard(request);

		Long totalMeetings = (long) meetingsByUserId.size();
		Long pending = countMeetingByStatusDashboard(MeetingStatus.PENDING, meetingsByUserId);
		Long completed = countMeetingByStatusDashboard(MeetingStatus.COMPLETED, meetingsByUserId);
		Long rescheduled = countMeetingByStatusDashboard(MeetingStatus.RESCHEDULED, meetingsByUserId);
		Long cancelled = countMeetingByStatusDashboard(MeetingStatus.CANCELLED, meetingsByUserId);
		Long totalHoursOfMeeting = calculateTotalHoursOfMeeting(meetingsByUserId, MeetingStatus.COMPLETED);

		Map<Date, Map<String, Map<String, Long>>> meetingsContextDate = new TreeMap<>();

		if (request.getToDate() == null && request.getFromDate() == null && completed == 0) {

			meetingsContextDate.put(new Date(), null);
		}

		else {

			for (Meeting meeting : meetingsByUserId) {
				if (meeting.getStatus() == MeetingStatus.COMPLETED) {
					Date meetingDate = normalizeDateToMidnight(meeting.getMeetingStartDateTime());
					MeetingContext context = meeting.getContext();

					meetingsContextDate.computeIfAbsent(meetingDate, k -> new TreeMap<>());
					Map<String, Map<String, Long>> contextData = meetingsContextDate.get(meetingDate);

					contextData.computeIfAbsent(context.name(), k -> new TreeMap<>());
					Map<String, Long> contextCount = contextData.get(context.name());

					contextCount.computeIfAbsent("count", k -> 0L);
					contextCount.computeIfAbsent("hour", k -> 0L);

					long count = contextCount.get("count") + 1;

					long meetingStartHour = meeting.getMeetingStartDateTime().getTime() / (60 * 60 * 1000);

					long meetingEndHour = meeting.getMeetingEndDateTime().getTime() / (60 * 60 * 1000);

					long meetingDuration = meetingEndHour - meetingStartHour;

					long hourCount = contextCount.get("hour") + meetingDuration;

					contextCount.put("count", count);
					contextCount.put("hour", hourCount);
				}
			}

			if (request.getFromDate() != null && request.getToDate() != null) {
				Set<Date> datesInRange = getDatesInRange(request.getFromDate(), request.getToDate());
				for (Date date : datesInRange) {
					if (meetingsContextDate.containsKey(date)) {
						continue;
					}
					meetingsContextDate.put(date, null);
				}
			}
		}

		MeetingDashboardDto meetingDashboardDto = new MeetingDashboardDto(totalMeetings, pending, completed,
				rescheduled, cancelled, totalHoursOfMeeting, meetingsContextDate);

		return new Response<>("User Meetings For Dashboard", meetingDashboardDto, HttpStatus.OK.value());
	}

	private Set<Date> getDatesInRange(Date fromDate, Date b) {
		Set<Date> dateRange = new TreeSet<>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fromDate);

		while (!calendar.getTime().after(b)) {
			dateRange.add(calendar.getTime());
			calendar.add(Calendar.DATE, 1);
		}

		return dateRange;
	}

	private int getMeetingHour(Date startDateTime, Date endDateTime) {
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDateTime);
		int startHour = startCalendar.get(Calendar.HOUR_OF_DAY);

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDateTime);
		int endHour = endCalendar.get(Calendar.HOUR_OF_DAY);

		// Assuming the meeting duration is within a single hour
		return endHour - startHour + 1;
	}

	private Long calculateTotalHoursOfMeeting(List<Meeting> meetings, MeetingStatus status) {
		long totalHours = 0L;

		Long meetingStartMillis = 0L;
		Long meetingEndMillis = 0L;

		for (Meeting meeting : meetings) {
			if (meeting.getStatus().equals(status)) {
				if (meeting.getMeetingStartDateTime() != null) {
					meetingStartMillis = meeting.getMeetingStartDateTime().getTime();
				}

				if (meeting.getMeetingEndDateTime() != null) {
					meetingEndMillis = meeting.getMeetingEndDateTime().getTime();
				}
				long meetingDurationMillis = meetingEndMillis - meetingStartMillis;

				// Convert milliseconds to hours (3600000 milliseconds in an hour)
				totalHours = totalHours + meetingDurationMillis;
			}
		}

		return totalHours;
	}

	private Date normalizeDateToMidnight(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	@Override
	public Response<?> getMeetingOfVisitor(String phoneNumber, Integer companyId) {
		Visitor visitor = this.visitorDao.serachVisitor(phoneNumber);
		try {
			if (visitor == null) {
				return new Response<>("Visitor not found", new ArrayList<>(), HttpStatus.OK.value());
			}

			List<Meeting> list = this.meetingDao.getVisitorIncompleteMeetings(visitor.getId(), companyId);
			List<MeetingDto> meetingDtoList = list.stream().map(t -> {
				try {
					return MeetingDto.convertToDTO(t);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}).collect(Collectors.toList());
			return new Response<>("success", meetingDtoList, HttpStatus.OK.value());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Response<?> getMeetingsToCheckOut(String phoneNumber) throws Exception {
		try {
			Visitor visitor = this.visitorDao.serachVisitor(phoneNumber);
			if (visitor == null) {
				return new Response<>("Visitor not found", null, HttpStatus.NOT_FOUND.value());
			}

			List<Meeting> list = this.meetingDao.getMeetingByVisitorIdAndTodayDate(visitor.getId());
			for (Meeting meeting : list) {
				if (meeting.getMeetingEndDateTime() == null && meeting.getCheckOutDateTime() == null) {
					return new Response<>("success", MeetingDto.convertToDTO(meeting), HttpStatus.OK.value());
				}
			}

			return new Response<>("success", null, HttpStatus.OK.value());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Fail", null, HttpStatus.BAD_REQUEST.value());
		}

	}

	@Override
	public Response<?> getMeetingsToCheckOut2(Integer companyId, String phoneNumber) throws Exception {
		try {
			Visitor visitor = this.visitorDao.serachVisitor(phoneNumber);
			if (visitor == null) {
				return new Response<>("Visitor not found", null, HttpStatus.NOT_FOUND.value());
			}

			List<Meeting> list = this.meetingDao.getMeetingByVisitorIdAndCompanyId(visitor.getId(), companyId);

			list.forEach(x -> {

				System.out.println(x.getEmployee().getCompany().getId() + " company - ID");

			});

			for (Meeting meeting : list) {
				if (meeting.getMeetingEndDateTime() == null && meeting.getCheckOutDateTime() == null) {
					return new Response<>("success", MeetingDto.convertToDTO(meeting), HttpStatus.OK.value());
				}
			}

			return new Response<>("success", null, HttpStatus.OK.value());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Fail", null, HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public Response<?> getMeetingByidForQr(int id) {

		try {

			String meetingHtml = null;
			Boolean check = false;

			List<Meeting> meeting = meetingDao.getMeetingByVisitorIdAndTodayDate(id);
			List<MeetingDto> meetingDto = new ArrayList<>();
			if (!meeting.isEmpty()) {

				for (Meeting x : meeting) {

					if (x.getCheckOutDateTime() == null) {

						MeetingDto convertToDTO = MeetingDto.convertToDTO(x);
						meetingDto.add(convertToDTO);
						check = true;
					}
				}
				if (check) {
					return new Response<>("Success", meetingDto, HttpStatus.OK.value());
				} else {
					meetingHtml = "PASS EXPIRED";
				}
				return new Response<>("Success", meetingHtml, HttpStatus.OK.value());

			}
			return new Response<>("No meeting found", null, HttpStatus.BAD_REQUEST.value());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Some thing Went Wrong", null, HttpStatus.BAD_REQUEST.value());
		}

	}

	public Response<?> helperFunction(UpdateMeetingDto meetingDto, Meeting meeting, User user) {
		try {
			String email = meeting.getVisitor().getEmail();
			if (MeetingStatus.CANCELLED.equals(meeting.getStatus())
					|| ("V".equals(meeting.getUpdatedBy()) && meeting.getUpdatedBy() != null)) {
				List<Notification> notificationList = this.notificationDao.getNotificationsByMeeting(meeting);
				if (!notificationList.isEmpty()) {
					for (Notification notification : notificationList) {
						if (("V".equals(meeting.getUpdatedBy()) && meeting.getUpdatedBy() != null)) {
							if (notification.getUserRole().equals("RECEPTIONIST")) {
								notification.setText(
										meeting.getVisitor().getName() + " has checked out. Meeting cancelled with "
												+ meeting.getEmployee().getFirstname());
							} else {
								notification.setText(
										meeting.getVisitor().getName() + " has checked out. Meeting is cancelled");
							}
						} else {
							if (user != null && "RECEPTIONIST".equals(user.getRole().getName())) {
								if (notification.getUserRole().equals("RECEPTIONIST")) {
									notification.setText("Meeting request of " + meeting.getVisitor().getName()
											+ " has been cancelled");
								} else {
									notification.setText("Meeting request of " + meeting.getVisitor().getName()
											+ " has been cancelled by the receptionist");
								}
							} else {
								if (notification.getUserRole().equals("RECEPTIONIST")) {
									notification.setText("Meeting request of " + meeting.getVisitor().getName()
											+ " has been cancelled by the Host - "
											+ meeting.getEmployee().getFirstname());
								} else {
									notification.setText("Meeting request of " + meeting.getVisitor().getName()
											+ " has been cancelled");
								}
							}
						}

						notification.setUpdatedAt(new Date());
						notification.setSeen(false);
						this.notificationDao.update(notification);
					}
				}

				Thread emailThread = new Thread(() -> {
					String content = String.format(
							"<div class='container'>" + "<h1>%s</h1>" + "<p>Status: %s</p>" + "<p>Remarks: %s</p>"
									+ "<p>Your request has been Rejected</p>" + "</div>",
							meeting.getVisitor().getName(), meeting.getStatus(), meeting.getRemarks());

					try {
						emailSendService.sendEmail(email, "Cancelled By Admin", content);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				emailThread.start();

				return new Response<>("CANCELLED Successfully", null, HttpStatus.OK.value());
			}

			if (MeetingStatus.APPROVED.equals(meeting.getStatus())) {
				List<Notification> notificationList = this.notificationDao.getNotificationsByMeeting(meeting);
				if (!notificationList.isEmpty()) {
					for (Notification notification : notificationList) {
						if (user != null && "RECEPTIONIST".equals(user.getRole().getName())) {
							if (notification.getUserRole().equals("RECEPTIONIST")) {
								if (meeting.getRoom() != null) {
									notification.setText("Meeting request of " + meeting.getVisitor().getName()
											+ " with " + meeting.getEmployee().getFirstname()
											+ " has been approved. Room allotted is "
											+ meeting.getRoom().getRoomName());
								} else {
									notification.setText("Meeting request of " + meeting.getVisitor().getName()
											+ " with " + meeting.getEmployee().getFirstname()
											+ " has been approved by receptionist");
								}
							} else {
								if (meeting.getRoom() != null) {
									System.out.println("meeting room name, " + meeting.getRoom().getRoomName());
									notification.setText("Meeting request of " + meeting.getVisitor().getName()
											+ " with " + meeting.getEmployee().getFirstname()
											+ " has been approved by receptionist. Room allotted is "
											+ meeting.getRoom().getRoomName());
								} else {
									notification.setText("Meeting request of " + meeting.getVisitor().getName()
											+ " with " + meeting.getEmployee().getFirstname()
											+ " has been approved by the receptionist");
								}
							}
						} else {
							if (notification.getUserRole().equals("RECEPTIONIST")) {
								if (meeting.getRoom() != null) {
									notification.setText("Meeting request of " + meeting.getVisitor().getName()
											+ " has been approved by the Host -" + meeting.getEmployee().getFirstname()
											+ ". Room allotted is " + meeting.getRoom().getRoomName());
								} else {
									notification.setText("Meeting request of " + meeting.getVisitor().getName()
											+ " has been approved by the Host" + meeting.getEmployee().getFirstname());
								}
							} else {
								if (meeting.getRoom() != null) {
									notification.setText("Meeting request of " + meeting.getVisitor().getName()
											+ " has been approved. Room allotted is "
											+ meeting.getRoom().getRoomName());
								} else {
									notification.setText("Meeting request of " + meeting.getVisitor().getName()
											+ " has been approved.");
								}
							}
						}

						notification.setUpdatedAt(new Date());
						notification.setSeen(false);
						this.notificationDao.update(notification);
					}
				}

				Thread emailThread = new Thread(() -> {

					try {
						String fileName = meeting.getVisitor().getName() + "_" + meeting.getVisitor().getPhoneNumber()
								+ "_VisitorPass.pdf";

						byte[] generateVisitorPassPDF = pdfService
								.generateVisitorPassPDF(MeetingDto.convertToDTO(meeting));

						String subject = "Meeting Approved";

						String content = String.format(
								"<div class='container'>" + "<h1>%s</h1>" + "<p> Your request has been Approved</p>"
										+ "<p> Download your pass </p>" + "</div>",
								meeting.getVisitor().getName(), meeting.getStatus());

						emailSendService.sendEmail(email, subject, content, generateVisitorPassPDF, fileName);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				emailThread.start();

//				String fileName = meeting.getVisitor().getName() + "_" + meeting.getVisitor().getPhoneNumber()
//						+ "_VisitorPass.pdf";
//
//				byte[] generateVisitorPassPDF = pdfService.generateVisitorPassPDF(MeetingDto.convertToDTO(meeting));
//
//				String subject = "Meeting Approved";
//
//				String content = String.format(
//						"<div class='container'>" + "<h1>%s</h1>" + "<p> Your request has been Approved</p>"
//								+ "<p> Download your pass </p>" + "</div>",
//						meeting.getVisitor().getName(), meeting.getStatus());
//
//				emailSendService.sendEmail(email, subject, content, generateVisitorPassPDF, fileName);
			}

			if (MeetingStatus.INPROCESS.equals(meeting.getStatus())) {
				List<Notification> notificationList = this.notificationDao.getNotificationsByMeeting(meeting);
				if (!notificationList.isEmpty()) {
					for (Notification notification : notificationList) {
						if (user != null && "RECEPTIONIST".equals(user.getRole().getName())) {
							if (notification.getUserRole().equals("RECEPTIONIST")) {
								if (meeting.getRoom() != null) {
									notification
											.setText("Room alloted for the meeting of " + meeting.getVisitor().getName()
													+ " and " + meeting.getEmployee().getFirstname() + " is "
													+ meeting.getRoom().getRoomName());
								}
							} else {
								if (meeting.getRoom() != null) {
									notification.setText(
											"Room alloted for the meeting with " + meeting.getVisitor().getName()
													+ " is " + meeting.getRoom().getRoomName());
								}
							}
						}
						notification.setUpdatedAt(new Date());
						notification.setSeen(false);
						this.notificationDao.update(notification);
					}
				}
			}

			else {

				Thread emailThread = new Thread(() -> {

					try {
						String subject = "Meeting " + meeting.getStatus();

						String content = String.format(
								"<div class='container'>" + "<h1>%s</h1>" + "<p> Status: %s</p>"
										+ "<p>Meeting Context: %s</p>" + "<p>%s</p>" + "</div>",
								meeting.getVisitor().getName(), meeting.getStatus(), meeting.getContext(), subject);

						emailSendService.sendEmail(email, subject, content);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				emailThread.start();

//				String subject = "Meeting " + meeting.getStatus();
//
//				String content = String.format(
//						"<div class='container'>" + "<h1>%s</h1>" + "<p> Status: %s</p>" + "<p>Meeting Context: %s</p>"
//								+ "<p>%s</p>" + "</div>",
//						meeting.getVisitor().getName(), meeting.getStatus(), meeting.getContext(), subject);
//
//				emailSendService.sendEmail(email, subject, content);
			}
			return new Response<>("success", null, HttpStatus.OK.value());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Fail", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	@Override
	public Response<?> getmeetingforUserDashboard(PaginationRequest meetingPaginationRequest) {
		List<Meeting> meetingsByUserId = meetingDao.getMeetingsByUserIdForDashboard(meetingPaginationRequest);
		if (meetingsByUserId.isEmpty()) {
			return new Response<>("No Data", null, HttpStatus.OK.value());
		}

		List<MeetingDto> meetingDtos = new ArrayList<>();

		meetingsByUserId.forEach(x -> {
			try {
				MeetingDto meetingDto = MeetingDto.convertToDTO(x);
				meetingDtos.add(meetingDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		// Sort the meetingDtos based on meeting status (Pending first)
		Collections.sort(meetingDtos, (m1, m2) -> {
			if (m1.getStatus() == MeetingStatus.PENDING && m2.getStatus() != MeetingStatus.PENDING) {
				return -1;
			} else if (m1.getStatus() != MeetingStatus.PENDING && m2.getStatus() == MeetingStatus.PENDING) {
				return 1;
			} else {
				return m2.getCreatedBy().compareTo(m1.getCreatedBy());
			}
		});

		return new Response<>("Success", meetingDtos, HttpStatus.OK.value());
	}

	@Override
	public Void automaticallyCancelledMeeting(Integer noOfDay) {

		PaginationRequest request = new PaginationRequest();

		request.setToDate(new Date());

		if (request.getToDate() != null) {

			Calendar toDate = Calendar.getInstance();
			toDate.setTime(request.getToDate());
			toDate.add(Calendar.DAY_OF_MONTH, -1);
			request.setToDate(toDate.getTime());

			Calendar fromDateCalendar = Calendar.getInstance();
			fromDateCalendar.setTime(request.getToDate());
			fromDateCalendar.add(Calendar.DAY_OF_MONTH, -noOfDay);
			request.setFromDate(fromDateCalendar.getTime());

		} else {

			System.err.println("Error: request.setToDate() is null");

			return null;
		}

		request.setPage(0);

		request.setStatus(MeetingStatus.PENDING);

		Page<Meeting> meetings = meetingDao.getMeetingsByPagination(request);

		List<Meeting> content = meetings.getContent();

		content.forEach(meeting -> {

			meeting.setStatus(MeetingStatus.CANCELLED);
			meeting.setRemarks("NO ACTION TAKEN (MEETING CANCELLED AUTOMATICALLY)");
			meeting.setUpdatedAt(new Date());
			meeting.setUpdatedBy("A");
			meetingDao.update(meeting);

		});

		request.setStatus(MeetingStatus.INPROCESS);

		Page<Meeting> meetings1 = meetingDao.getMeetingsByPagination(request);

		List<Meeting> content1 = meetings1.getContent();

		content1.forEach(meeting1 -> {

			meeting1.setStatus(MeetingStatus.COMPLETED);
			meeting1.setRemarks(" MEETING COMPLETED");
			meeting1.setUpdatedAt(new Date());
			meeting1.setUpdatedBy("A");
			if (meeting1.getRoom() != null) {
				Room exisitingRoom = meeting1.getRoom();
				exisitingRoom.setIsAvailable(true);
				exisitingRoom.setUpdatedAt(new Date());
				this.roomDao.update(exisitingRoom);
			}

			meetingDao.update(meeting1);

		});

		return null;
	}

	@Override
	public void deleteOldFiles() {
		try {
//			File excelFolder = new File(excelDir);
//
//			File[] files = excelFolder.listFiles();
//			if (files != null) {
//				for (File file : files) {
//					if (file.isFile()) {
//						System.out.println(file.getName());
//
//						if (file.delete()) {
//							System.out.println("Deleted file: " + file.getName());
//						} else {
//							System.err.println("Failed to delete file: " + file.getName());
//						}
//					}
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}