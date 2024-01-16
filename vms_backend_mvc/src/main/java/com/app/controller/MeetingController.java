package com.app.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.app.Dao.ConfigurationDao;
import com.app.Dao.MeetingDao;
import com.app.Dao.UserDao;
import com.app.authorize.Authorization;
import com.app.dto.CustomResponseDTO;
import com.app.dto.MeetingDto;
import com.app.dto.PaginationRequest;
import com.app.dto.UpdateMeetingDto;
import com.app.dto.VisitorMeetingDto;
import com.app.emun.MeetingStatus;
import com.app.entity.Meeting;
import com.app.response.Response;
import com.app.security.JwtHelper;
import com.app.service.FileService;
import com.app.service.MeetingService;
import com.app.service.PdfService;
import com.app.service.UserService;
import com.app.service.VisitorService;

@RestController
@RequestMapping("/api/meeting/")
public class MeetingController {

	@Autowired
	private MeetingService meetingService;

	@Autowired
	private Authorization authorization;

	@Autowired
	private ConfigurationDao configurationDao;

	@Autowired
	private VisitorService visitorService;

	@Autowired
	private PdfService pdfService;

	@Autowired
	private MeetingDao meetingDao;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtHelper jwtHelper;

	@Autowired
	private FileService fileService;

	@Autowired
	private UserDao userDao;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${project.excel}")
	private String excelDir;

	@PostMapping("/save")
	public ResponseEntity<?> save(@RequestBody @Valid MeetingDto meetingDto) throws Exception {

		Response<?> response = this.meetingService.addMeeting(meetingDto);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/emp-status")
	public ResponseEntity<?> isEmployeeBusy(@RequestParam Integer id,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") Date meetingStartDate,
			@RequestParam Integer duration) {

		Response<?> response = this.meetingService.isEmployeeBusy(id, meetingStartDate, duration);
		System.out.println(new Date());
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/vis")
	public ResponseEntity<?> getMeetingWithStatus(@RequestParam Integer id) {

		Response<?> meetings = meetingService.getmeetingWithstatusByuser(id);

		if (meetings.getStatus() == HttpStatus.OK.value()) {

			return ResponseEntity.ok(meetings);
		}
		return new ResponseEntity<>(meetings, HttpStatus.NO_CONTENT);
	}

//	@GetMapping("/all")
//	public ResponseEntity<?> getAll(@RequestParam Integer id,
//			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date meetingStartDate,
//			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date meetingEndDate) {
//		Response<?> response = this.roomService.getAll(id, meetingStartDate, meetingEndDate);
//
//		if (response.getStatus() == HttpStatus.OK.value()) {
//			return new ResponseEntity<>(response, HttpStatus.OK);
//		}
//		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
//	}

	@GetMapping("/getbyid/{id}")
	public ResponseEntity<?> getMeeetingByid(@PathVariable int id) {

		Response<?> meetingByid = meetingService.getMeetingByid(id);

		return new ResponseEntity<>(meetingByid, HttpStatus.valueOf(meetingByid.getStatus()));

	}

	@PostMapping("/paginate")
	public ResponseEntity<?> getMeetingsByPagination(@RequestBody PaginationRequest PaginationRequest,
			HttpServletRequest request) throws Exception {

//		String header = request.getHeader("Authorization");
//		if (header == null) {
//			return new ResponseEntity<>((new Response<>("Unauthorize", null, HttpStatus.BAD_REQUEST.value())),
//					HttpStatus.BAD_REQUEST);
//		}
//		User authorize = authorization.authorizegetallUserdetalis(header.substring(7));
//		
//		if (authorize != null) {
//			
//			Response<?> user = userService.getUserByid(PaginationRequest.getUser().getId());
//
//			UserDto data = (UserDto) user.getData();
//			
//			if (data.getRole().getName().equals("RECEPTIONIST")) {
//				
//				Response<?> meetingsByPagination = meetingService.getMeetingsByPagination(PaginationRequest);
//
//				return ResponseEntity.ok(meetingsByPagination);
//				
//			}else if(data.getRole().getName().equals("ADMIN")) {
//				
//				PaginationRequest.setNoUser(PaginationRequest.getUser().getId());
//				
//				Response<?> meetingsByPagination = meetingService.getMeetingsByPagination(PaginationRequest);
//
//				return ResponseEntity.ok(meetingsByPagination);
//				
//				
//				
//			}
//
//			
//
//		} else {
//			return new ResponseEntity<>((new Response<>("Unauthorize", null, HttpStatus.BAD_REQUEST.value())),
//					HttpStatus.BAD_REQUEST);
//
//		}

		Response<?> meetingsByPagination = meetingService.getMeetingsByPagination(PaginationRequest);

		return ResponseEntity.ok(meetingsByPagination);

	}

	@PostMapping("/paginateDashBoard")
	public ResponseEntity<?> getMeetingByPaginationUserId(@RequestBody PaginationRequest request) {

		Response<?> meetingByid = meetingService.getMeetingByPaginationDashBoardReceptionist(request);

		return new ResponseEntity<>(meetingByid, HttpStatus.valueOf(meetingByid.getStatus()));

	}

	@PostMapping("/update/meeting")
	public ResponseEntity<?> updateVisitor(@RequestBody UpdateMeetingDto meetingDTO, HttpServletRequest request)
			throws Exception {
		System.out.println("Meetign status, " + meetingDTO.getStatus());
		String header = request.getHeader("Authorization");
		if (header == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize", null, HttpStatus.UNAUTHORIZED.value())),
					HttpStatus.UNAUTHORIZED);
		}

		if (meetingDTO.getStatus() == null || meetingDTO.getStatus().toString().isEmpty()
				|| meetingDTO.getStatus().toString() == "") {
			return new ResponseEntity<>((new Response<>("Select a valid status", null, HttpStatus.BAD_REQUEST.value())),
					HttpStatus.BAD_REQUEST);
		}

		Response<?> authorizetoAdduser = authorization.authorizetoAdduser(header.substring(7));
		if (authorizetoAdduser.getStatus() != HttpStatus.OK.value()) {
			return ResponseEntity.badRequest().body(authorizetoAdduser);
		}

		Response<?> updatemeetingStatus = meetingService.updatemeetingStatus(meetingDTO, request);
		if (updatemeetingStatus.getStatus() == HttpStatus.OK.value()) {
			return ResponseEntity.ok(updatemeetingStatus);
		}
		return ResponseEntity.badRequest().body(updatemeetingStatus);

	}

	@PostMapping("/add/byuser")
	public ResponseEntity<?> addVisitorUser(@RequestBody @Valid VisitorMeetingDto visitormeetingDTO) {

		Response<?> addVisitorByuser = visitorService.addVisitorByuser(visitormeetingDTO);
		if (addVisitorByuser.getStatus() == HttpStatus.OK.value()) {
			return ResponseEntity.ok(addVisitorByuser);
		}
		return new ResponseEntity<>(addVisitorByuser, HttpStatus.valueOf(addVisitorByuser.getStatus()));

	}

	@GetMapping("/checkout")
	public ResponseEntity<?> checkOut(@RequestParam("phone") String phone) throws Exception {

		Response<?> updateCheckoutStatus = visitorService.updateCheckoutStatus(phone);

		if (updateCheckoutStatus.getStatus() == HttpStatus.OK.value()) {
			return ResponseEntity.ok(updateCheckoutStatus);
		} else {
			return new ResponseEntity<>(updateCheckoutStatus, HttpStatus.valueOf(updateCheckoutStatus.getStatus()));
		}

	} // 8249738881

	@GetMapping("/checkout-by-receptionist")
	public ResponseEntity<?> checkOutByReceptionist(@RequestParam("phone") String phone) throws Exception {

		Response<?> updateCheckoutStatus = visitorService.updateCheckoutStatusByReceptionist(phone);

		if (updateCheckoutStatus.getStatus() == HttpStatus.OK.value()) {
			return ResponseEntity.ok(updateCheckoutStatus);
		} else {
			return new ResponseEntity<>(updateCheckoutStatus, HttpStatus.valueOf(updateCheckoutStatus.getStatus()));
		}

	} //

	@GetMapping("/downloadPass")
	public ResponseEntity<?> downloadVisitorPass(@RequestParam(name = "meetingId") Integer meetingId,
			HttpServletRequest request) throws Exception {
//
//		MeetingDto meetingDto = MeetingDto.convertToDTO(meetingDao.getById(meetingId));
		Meeting meeting = meetingDao.getById(meetingId);

		LocalDate todayDateUTC = LocalDate.now(ZoneOffset.UTC);
		LocalDate meetingStartDateUTC = meeting.getMeetingStartDateTime().toInstant().atZone(ZoneOffset.UTC)
				.toLocalDate();

		if (!todayDateUTC.isEqual(meetingStartDateUTC)) {
			return new ResponseEntity<>(
					new CustomResponseDTO("Meeting pass is not valid today.", HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);
		}

		if (meeting.getStatus() == MeetingStatus.APPROVED || meeting.getStatus() == MeetingStatus.INPROCESS) {
			if (meeting.getRoom() == null) {
				return new ResponseEntity<>(
						new Response<>("Room not alloted yet", null, HttpStatus.BAD_REQUEST.value()),
						HttpStatus.BAD_REQUEST);
			}
			byte[] pdfData = pdfService.generateVisitorPassPDF(MeetingDto.convertToDTO(meeting));

			Thread updateThread = new Thread(() -> {

				try {
					// if ((user != null)) {
//					UpdateMeetingDto updateMeetingDto = new UpdateMeetingDto();
//					updateMeetingDto.setId(meetingDto.getId());
//					updateMeetingDto.setEmployee(meetingDto.getUser());
//					updateMeetingDto.setRoom(meetingDto.getRoom());
//					updateMeetingDto.setStatus(meetingDto.getStatus());
//					updateMeetingDto.setVisitor(meetingDto.getVisitor());
//					this.meetingService.updatemeetingStatus(updateMeetingDto, request);
//					// }
					meeting.setRoom(meeting.getRoom());
					meeting.setStatus(MeetingStatus.INPROCESS);
					meeting.setUpdatedBy(null);
					meetingDao.update(meeting);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

			updateThread.start();

			String fileName = meeting.getVisitor().getName() + "-" + meeting.getVisitor().getPhoneNumber() + ".pdf";

			ByteArrayResource resource = new ByteArrayResource(pdfData);

			HttpHeaders headers = new HttpHeaders();

			headers.add("Content-Disposition", "inline; filename=" + fileName);

			System.out.println(new Date() + "mdfbskfbskjf");

			return ResponseEntity.ok().headers(headers).contentLength(pdfData.length)
					.contentType(MediaType.parseMediaType("application/pdf")).body(resource);

		} else {

			return new ResponseEntity<>(
					new CustomResponseDTO("Meeting pass has been expired !! ", HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);

		}

	}

	@PostMapping("/userdashboard")
	public ResponseEntity<?> getMeetingsForuserDashboard(@RequestBody PaginationRequest meetingPaginationRequest) {

		try {
			Response<?> getmeetingForDashboardByuser = meetingService
					.getmeetingForDashboardByuser(meetingPaginationRequest);

			return new ResponseEntity<>(getmeetingForDashboardByuser,
					HttpStatus.valueOf(getmeetingForDashboardByuser.getStatus()));

		} catch (Exception e) {

			e.printStackTrace();

			return new ResponseEntity<>(
					new CustomResponseDTO(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);

		}

	}

	@PostMapping("/exportdata")
	public ResponseEntity<?> exportDataToExcel(@RequestBody PaginationRequest request) {

		try {

			List<MeetingDto> meetingByPaginationExcel = meetingService.getMeetingByPaginationExcel(request);

			ByteArrayInputStream excelStream = meetingService.generateExcel(meetingByPaginationExcel);
			
			System.out.println(meetingByPaginationExcel.size()+ "Inside size  ");

			String filename = "Meeting report.xlsx";

			// Convert to byte array
			byte[] byteArray = IOUtils.toByteArray(excelStream);

			// Close the ByteArrayInputStream
			excelStream.close();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "company_data.xlsx");

			return new ResponseEntity<>(byteArray, headers, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new CustomResponseDTO("Error in exporting Excel data", HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/download/excel")
	public ResponseEntity<Resource> downloadExcel(@RequestParam("filename") String filename) {

		try {

			String filePath = excelDir + filename;

			File file = new File(filePath);

			if (!file.exists()) {

				System.out.println("file not found");

				return ResponseEntity.notFound().build();
			}

			FileSystemResource resource = new FileSystemResource(file);

			HttpHeaders headers = new HttpHeaders();

			headers.add("Content-Disposition", "attachment; filename=" + filename);

			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/getByPhone")
	public ResponseEntity<?> checkIfMeetingsExist(@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("companyId") Integer companyId) {

		Response<?> response = this.meetingService.getMeetingOfVisitor(phoneNumber, companyId);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/getMeetingsToCheckout")
	public ResponseEntity<?> getMeetingsToCheckOut(@RequestParam("phoneNumber") String phoneNumber) throws Exception {
		Response<?> response = this.meetingService.getMeetingsToCheckOut(phoneNumber);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/check-meetings")
	public ResponseEntity<?> getMeetingsToCheckOut2(@RequestParam("companyId") Integer companyId,
			@RequestParam("phoneNumber") String phoneNumber) throws Exception {

		Response<?> response = this.meetingService.getMeetingsToCheckOut2(companyId, phoneNumber);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/meeting-details/{id}")
	public ResponseEntity<?> getMeeetingfORqR(@PathVariable int id) {

		Response<?> meetingByid = meetingService.getMeetingByidForQr(id);

		return new ResponseEntity<>(meetingByid, HttpStatus.valueOf(meetingByid.getStatus()));

	}

	// pending request for dashboard
	@PostMapping("/meetingfordashboard")
	public ResponseEntity<?> getMeetingForUSerDashboard(@RequestBody PaginationRequest meetingPaginationRequest) {

		if (meetingPaginationRequest.getUser().getId() == null) {
			return new ResponseEntity<>(new Response<>("Provied USer ID Fast", null, HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);
		}

		Response<?> getmeetingforUserDashboard = meetingService.getmeetingforUserDashboard(meetingPaginationRequest);

		return new ResponseEntity<>(getmeetingforUserDashboard,
				HttpStatus.valueOf(getmeetingforUserDashboard.getStatus()));

	}

//	@GetMapping("/demo")
//	public ResponseEntity<?> getmeetingdetails() {
//
//		try {
//			meetingService.automaticallyCancelledMeeting(30);
//
//			return ResponseEntity.ok("Meeting cancellation process initiated successfully");
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//
//					.body("An error occurred while processing the meeting cancellation");
//		}
//
//	}

	@GetMapping("/deleteExcel")
	public ResponseEntity<?> deleteOldFile() {

		try {

			meetingService.deleteOldFiles();

			return ResponseEntity.ok("excel deleted initiated successfully");

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)

					.body("An error occurred while processing deleting the file");
		}

	}

}
