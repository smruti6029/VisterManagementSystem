package com.app.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.CustomResponseDTO;
import com.app.dto.MeetingDto;
import com.app.dto.VisitorDto;
import com.app.dto.VisitorMeetingDetailsDto;
import com.app.emun.MeetingContext;
import com.app.emun.MeetingStatus;
import com.app.entity.Meeting;
import com.app.entity.Visitor;
import com.app.response.Response;
import com.app.service.FileService;
import com.app.service.VisitorService;

@RestController
@RequestMapping("/vis")
public class VisitorController {

	@Autowired
	private VisitorService visitorService;

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/add")
	public ResponseEntity<?> addVisitor(@RequestBody @Valid VisitorDto visitorDto, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			Map<String, String> errorMap = new HashMap<>();

			for (FieldError error : bindingResult.getFieldErrors()) {

				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
		}

		try {

			String phoneNumber = visitorDto.getPhoneNumber();

			Visitor existingVisitor = visitorService.serachVisitorByphone(phoneNumber);

//    		    if (visitorService.isPhoneAlreadyInUse(visitorDto.getPhoneNumber())) {
//    			 
//    	            CustomResponseDTO response = new CustomResponseDTO(null, "Phone number already exists", HttpStatus.BAD_REQUEST.value());
//    	            
//    	            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    	        }

			if (existingVisitor == null) {

				Response<?> addVisitor = visitorService.addVisitor(visitorDto);
				if (addVisitor.getStatus() == HttpStatus.OK.value()) {
					CustomResponseDTO response = new CustomResponseDTO(addVisitor, HttpStatus.OK.name(),
							HttpStatus.OK.value());

					return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
				}
				return new ResponseEntity<>(addVisitor, HttpStatus.OK);

			} else {

				VisitorDto updateVisitor = visitorService.updateVisitor(phoneNumber, visitorDto);

				CustomResponseDTO response = new CustomResponseDTO(updateVisitor, HttpStatus.OK.name(),
						HttpStatus.OK.value());

				return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
			}

		} catch (Exception e) {

			e.printStackTrace();

			return new ResponseEntity<>(
					new CustomResponseDTO(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping("/meetCon")
	public ResponseEntity<?> meetingContext() {

		try {

			List<MeetingContext> meetingContext = Arrays.asList(MeetingContext.values());

			CustomResponseDTO response = new CustomResponseDTO(meetingContext, "Meeting context fetched successfully",
					HttpStatus.OK.value());

			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));

		} catch (Exception e) {

			e.printStackTrace();

			return new ResponseEntity<>(
					new CustomResponseDTO(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping("/meetstatus")
	public ResponseEntity<?> meetingStatus() {

		try {

			List<MeetingStatus> meetingStatus = Arrays.asList(MeetingStatus.APPROVED, MeetingStatus.COMPLETED,
					MeetingStatus.PENDING, MeetingStatus.CANCELLED, MeetingStatus.INPROCESS);

			CustomResponseDTO response = new CustomResponseDTO(meetingStatus, "Meeting Statusfetched successfully",
					HttpStatus.OK.value());

			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));

		} catch (Exception e) {

			e.printStackTrace();

			return new ResponseEntity<>(
					new CustomResponseDTO(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping("/meetstatusadmin")
	public ResponseEntity<?> meetingStatusForAdmin() {

		try {

			List<MeetingStatus> meetingStatus = Arrays.asList(MeetingStatus.APPROVED, MeetingStatus.CANCELLED);

			CustomResponseDTO response = new CustomResponseDTO(meetingStatus, "Meeting Statusfetched successfully",
					HttpStatus.OK.value());

			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));

		} catch (Exception e) {

			e.printStackTrace();

			return new ResponseEntity<>(
					new CustomResponseDTO(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping("/getall")
	public ResponseEntity<?> getAllVisitors() {

		try {

			List<Visitor> visitors = visitorService.getAllVisitors();

			CustomResponseDTO response = new CustomResponseDTO(visitors, HttpStatus.OK.name(), HttpStatus.OK.value());

			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));

		} catch (Exception e) {

			e.printStackTrace();

			return new ResponseEntity<>(
					new CustomResponseDTO(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Visitor> getVisitorById(@PathVariable Integer id) {

		Visitor visitor = visitorService.getVisitorById(id);

		return ResponseEntity.ok(visitor);
	}

	@PostMapping("/checkIn")
	public ResponseEntity<?> checkIn(@RequestBody MeetingDto meetingDto) {
		Response<?> response = this.visitorService.checkIn(meetingDto);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/getByPhone")
	public ResponseEntity<?> getVisitorByPhoneNumber(@RequestParam String phoneNumber) {
		System.out.println(phoneNumber);
		Pattern pattern = Pattern.compile("^[0-9]{10}$");

		Matcher matcher = pattern.matcher(phoneNumber);

		if (!matcher.matches()) {

			CustomResponseDTO response = new CustomResponseDTO("Invalid phone number format",
					HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {

			MeetingDto serachVisitor = visitorService.serachVisitor(phoneNumber);

			if (serachVisitor != null) {

				CustomResponseDTO response = new CustomResponseDTO(serachVisitor, HttpStatus.OK.name(),
						HttpStatus.OK.value());

				return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));

			} else {

				return new ResponseEntity<>(new CustomResponseDTO("Meetings Not Found", HttpStatus.BAD_REQUEST.value()),
						HttpStatus.BAD_REQUEST);

			}

		} catch (Exception e) {

			e.printStackTrace();

			return new ResponseEntity<>(
					new CustomResponseDTO(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);

		} finally {
		}
	}

	@GetMapping("/getVisitorByPhone")
	public ResponseEntity<?> visitorExist(@RequestParam String phoneNumber) {

		Pattern pattern = Pattern.compile("^[0-9]{10}$");

		Matcher matcher = pattern.matcher(phoneNumber);

		if (!matcher.matches()) {

			CustomResponseDTO response = new CustomResponseDTO("Invalid phone number format",
					HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {

//			Meeting serachVisitor = visitorService.serachVisitor(phoneNumber);
			Visitor serachVisitor = visitorService.serachVisitorByphone(phoneNumber);

			if (serachVisitor != null) {

				CustomResponseDTO response = new CustomResponseDTO(VisitorDto.convertToDTO(serachVisitor),
						HttpStatus.OK.name(), HttpStatus.OK.value());

				return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));

			} else {

				return new ResponseEntity<>(new CustomResponseDTO("Visiter Not Found", HttpStatus.BAD_REQUEST.value()),
						HttpStatus.BAD_REQUEST);

			}

		} catch (Exception e) {

			e.printStackTrace();

			return new ResponseEntity<>(
					new CustomResponseDTO(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);

		} finally {
		}
	}

//	@GetMapping("/getVisitorByPhone")
//	public ResponseEntity<?> visitorExist(@RequestParam String phoneNumber) {
//
//		Pattern pattern = Pattern.compile("^[0-9]{10}$");
//
//		Matcher matcher = pattern.matcher(phoneNumber);
//
//		if (!matcher.matches()) {
//
//			CustomResponseDTO response = new CustomResponseDTO("Invalid phone number format",
//					HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value());
//			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//		}
//
//		try {
//
//			Visitor serachVisitor = visitorService.serachVisitorByphone(phoneNumber);
//
//			if (serachVisitor != null) {
//
//				return new ResponseEntity<>(
//						new Response<>("Visiter Found", VisitorDto.convertToDTO(serachVisitor), HttpStatus.OK.value()),
//						HttpStatus.OK);
//
//			} else {
//
//				return new ResponseEntity<>(
//						new Response<>("Visiter Not Found", new ArrayList<>(), HttpStatus.OK.value()), HttpStatus.OK);
//
//			}
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//			return new ResponseEntity<>(
//					new CustomResponseDTO(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value()),
//					HttpStatus.BAD_REQUEST);
//
//		} finally {
//		}
//	}

	@GetMapping("/getVis/{userId}")
	public ResponseEntity<?> getVisiterByUserId(@PathVariable Integer userId) {

		try {

			List<VisitorMeetingDetailsDto> visitors = visitorService.getAllVisitorsByUserId(userId);

			CustomResponseDTO response = new CustomResponseDTO(visitors, "Meeting details of the employee",
					HttpStatus.OK.value());

			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));

		} catch (Exception e) {

			e.printStackTrace();

			return new ResponseEntity<>(
					new CustomResponseDTO(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping("/getAllMeeting")
	public ResponseEntity<?> getAllMeeting() {

		try {

			List<Meeting> meetings = visitorService.getAllMeeting();

			CustomResponseDTO response = new CustomResponseDTO(meetings, "All meeting details!!",
					HttpStatus.OK.value());

			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));

		} catch (Exception e) {

			e.printStackTrace();

			return new ResponseEntity<>(
					new CustomResponseDTO(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);

		}

	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadPostImage(@RequestParam("image") MultipartFile image, HttpServletRequest request) {

		try {

//			String fileName = this.fileService.uploadImage(path, image);
			String fileName = this.fileService.uploadImage(image);

			return new ResponseEntity<>(
					new CustomResponseDTO(fileName, "Image uploaded successfully", HttpStatus.OK.value()),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new CustomResponseDTO("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR.value()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response)
			throws IOException {

		try {
			InputStream resource = this.fileService.getResource(path, imageName);

			if (resource != null) {

				response.setContentType(MediaType.IMAGE_JPEG_VALUE);

				StreamUtils.copy(resource, response.getOutputStream());

			} else {

				response.setStatus(HttpServletResponse.SC_NOT_FOUND);

				response.getWriter().write("Image not found");

			}

		} catch (Exception e) {

			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

			response.getWriter().write("Error occurred while fetching the image");
		}

	}

	@GetMapping("/company/name")
	public ResponseEntity<?> serchCompanyByName(@RequestParam String companyName) {
		Response<?> searchCompanyByName = visitorService.searchCompanyByName(companyName);

		return new ResponseEntity<>(searchCompanyByName, HttpStatus.valueOf(searchCompanyByName.getStatus()));

	}

}
