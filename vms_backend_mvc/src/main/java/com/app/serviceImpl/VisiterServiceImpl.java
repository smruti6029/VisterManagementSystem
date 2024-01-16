package com.app.serviceImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.app.Dao.CityDao;
import com.app.Dao.StateDao;
import com.app.Dao.UserDao;
import com.app.Dao.VisitorCompanyDao;
import com.app.Dao.VisitorDao;
import com.app.dto.MeetingDto;
import com.app.dto.VisitorCompanyDto;
import com.app.dto.VisitorDto;
import com.app.dto.VisitorMeetingDetailsDto;
import com.app.dto.VisitorMeetingDto;
import com.app.entity.City;
import com.app.entity.Meeting;
import com.app.entity.State;
import com.app.entity.User;
import com.app.entity.Visitor;
import com.app.entity.VisitorCompany;
import com.app.response.Response;
import com.app.service.MeetingService;
import com.app.service.VisitorService;
import com.app.validation.VallidationClass;

@Service
public class VisiterServiceImpl implements VisitorService {

	@Autowired
	private VisitorDao visitorDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private StateDao stateDao;

	@Autowired
	private CityDao cityDao;

	@Autowired
	private VisitorCompanyDao visitorCompanyDao;

	@Autowired
	private MeetingService meetingService;

	@Autowired
	private VallidationClass validationClass;

	@Override
	public Response<?> addVisitor(VisitorDto visitorDto) {

		Visitor visitor = VisitorDto.toVisitor(visitorDto);

		Visitor addVisitor = visitorDao.addVisitor(visitor);

		return new Response<>("Visitor Data Saved", VisitorDto.convertToDTO(addVisitor), HttpStatus.OK.value());

	}

	@Override
	public List<Visitor> getAllVisitors() {

		List<Visitor> allVisitors = visitorDao.getAllVisitors();

		return allVisitors;
	}

	@Override
	public Visitor getVisitorById(Integer id) {

		return visitorDao.getVisitorById(id);

	}

	@Override
	public VisitorDto updateVisitor(String phoneNumber, VisitorDto visitorDto) {

//		Visitor serachVisitor = visitorDao.serachVisitor(phoneNumber);
//
//		Meeting meeting = new Meeting();
//
//		User user = userService.getUserById(visitorDto.getUser().getId());
//
//		if (user == null) {
//			throw new EntityNotFoundException("User with the provided ID not found");
//		}
//
//		user.setId(visitorDto.getUser().getId());
//
//		serachVisitor.setName(visitorDto.getName());
//		serachVisitor.setPhoneNumber(visitorDto.getPhoneNumber());
//		serachVisitor.setEmail(visitorDto.getEmail().toLowerCase());
//		serachVisitor.setAddress(visitorDto.getAddress());
//		serachVisitor.setGender(visitorDto.getGender());
//		serachVisitor.setAge(visitorDto.getAge());
//		serachVisitor.setState(visitorDto.getState());
//		serachVisitor.setCity(visitorDto.getCity());
//		serachVisitor.setAadhaarNumber(visitorDto.getAadhaarNumber());
//		serachVisitor.setCompanyName(visitorDto.getCompanyName());
//
//		Visitor updateVisitor = visitorDao.updateVisitor(serachVisitor);
//
//		meetingService.updateMeeting(visitorDto);
//
//		System.out.println(serachVisitor.getName());
//
//		return VisitorMeetingDto.toVisitorDto(updateVisitor);
		return null;

	}

	@Override
	public boolean isPhoneAlreadyInUse(String phone) {

		Visitor existingVisitor = visitorDao.findByPhone(phone);

		return existingVisitor != null;

	}

	@Override
	public MeetingDto serachVisitor(String phoneNumber) {
		try {
			Visitor serachVisitor = visitorDao.serachVisitor(phoneNumber);

			Meeting todayMeeting = visitorDao.getTodayMeeting(serachVisitor.getId());

			return MeetingDto.convertToDTO(todayMeeting);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Visitor serachVisitorByphone(String phoneNumber) {

		Visitor serachVisitor = visitorDao.serachVisitor(phoneNumber);

		return serachVisitor;
	}

	@Override
	public List<VisitorMeetingDetailsDto> getAllVisitorsByUserId(Integer userId) {

		return visitorDao.getAllVisitorsByUserId(userId);
	}

	@Override
	public List<Meeting> getAllMeeting() {
		// TODO Auto-generated method stub
		return visitorDao.getAllMeeting();
	}

	@Override
	public Response<?> addVisitorByuser(VisitorMeetingDto visitorDto) {

		Visitor visitorByPhone = visitorDao.serachVisitor(visitorDto.getPhoneNumber());

		Date meetingStartDateTime = visitorDto.getMeetingStartDateTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(meetingStartDateTime);

		calendar.add(Calendar.HOUR_OF_DAY, 5);
		calendar.add(Calendar.MINUTE, 30);

		Date updatedDate = calendar.getTime();

		Date date = new Date();

		if (updatedDate.before(date)) {
			return new Response<>("Meeting Start Date Can't Be Less Than Today Date", null,
					HttpStatus.BAD_REQUEST.value());
		}

		if (visitorByPhone == null) {

			if (visitorDto.getName().trim().isEmpty()) {
				return new Response<>("Visitor Name can not be empty", null, HttpStatus.BAD_REQUEST.value());
			}

//			if (visitorDto.getCompanyName().trim().isEmpty()) {
//				return new Response<>("Company Name can not be empty", null, HttpStatus.BAD_REQUEST.value());
//			}

			Visitor visitor = VisitorMeetingDto.toVisitor(visitorDto);

			User getuserByid = userDao.getuserByid(visitorDto.getUser().getId());

			if (visitorDto.getVisitorCompany() != null) {

				if (visitorDto.getVisitorCompany().getName() != null) {

					String trimmedCompanyName = visitorDto.getVisitorCompany().getName().trim();

					if (trimmedCompanyName.isEmpty()) {
						return new Response<>("Provide Comapany ", null, HttpStatus.BAD_REQUEST.value());
					}

					VisitorCompany visitorCompany = visitorCompanyDao
							.getBycompanyName(visitorDto.getVisitorCompany().getName().trim());

					if (visitorCompany == null) {

						VisitorCompany save = visitorCompanyDao
								.save(VisitorCompanyDto.toEntity(visitorDto.getVisitorCompany()));
						visitor.setCompany(save);

					} else {
						visitor.setCompany(visitorCompany);
					}
				} else if (visitorDto.getVisitorCompany().getId() != null) {
					VisitorCompany byId = visitorCompanyDao.getById(visitorDto.getVisitorCompany().getId());
					if (byId != null) {
						visitor.setCompany(byId);
					} else {
						return new Response<>("Provide Comapany ", null, HttpStatus.BAD_REQUEST.value());
					}
				}
			} else {
				return new Response<>("Provide Comapany ", null, HttpStatus.BAD_REQUEST.value());
			}

			visitor.setCreatedBy(getuserByid.getFirstname());

			Visitor addVisitor = visitorDao.addVisitor(visitor);

			if (addVisitor != null) {
				Response<?> addMeetingByuser = meetingService.addMeetingByuser(visitorDto);
			}
			return new Response<>("Visitor Added Succesfully", visitorDto, HttpStatus.OK.value());
		} else {
			
			Visitor updatedVisitor = visitorDao.updateVisitor(visitorByPhone);

			if (updatedVisitor != null) {
				Response<?> addMeetingByUser = meetingService.addMeetingByuser(visitorDto);
				return new Response<>("Visitor Updated Successfully", visitorDto, HttpStatus.OK.value());
			}
		}
		return new Response<>("Failed to add or update visitor", null, HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public Response<?> updateCheckoutStatus(String phone) throws Exception {

		Visitor visitor = visitorDao.serachVisitor(phone);
		if (visitor == null) {
			return new Response<>("No Visitor Found", null, HttpStatus.BAD_REQUEST.value());
		}

		Response<?> updateCheckoutStatus = meetingService.updateCheckoutStatus(visitor);

		return updateCheckoutStatus;
	}

	@Override
	public Response<?> updateCheckoutStatusByReceptionist(String phone) throws Exception {
		Visitor visitor = visitorDao.serachVisitor(phone);
		if (visitor == null) {
			return new Response<>("No Visitor Found", null, HttpStatus.BAD_REQUEST.value());
		}

		Response<?> updateCheckoutStatus = meetingService.updateCheckoutStatusByReceptionist(visitor);

		return updateCheckoutStatus;
	}

	@Override
	public Response<?> checkIn(MeetingDto meetingDto) {
		// TODO Auto-generated method stub
		if (this.validationClass.checkMeeting(meetingDto).getStatus() != HttpStatus.OK.value()) {
			return this.validationClass.checkMeeting(meetingDto);
		}
		Response<?> response = this.meetingService.updateMeeting(meetingDto);
		return response;
	}

	@Override
	public Visitor getVisitorByEmail(String email) {

		Visitor visitorByEmail = visitorDao.getVisitorByEmail(email);
		if (visitorByEmail != null) {
			return visitorByEmail;
		}

		return null;
	}

	@Override
	public Response<?> updateVisitorMeeting(String phoneNumber, VisitorMeetingDto visitorMeetingDto) throws Exception {

		Visitor existingVisitor = visitorDao.serachVisitor(phoneNumber);

		if (existingVisitor != null) {

			existingVisitor.setName(visitorMeetingDto.getName());
			existingVisitor.setPhoneNumber(visitorMeetingDto.getPhoneNumber());
			existingVisitor.setEmail(visitorMeetingDto.getEmail());
			existingVisitor.setAge(visitorMeetingDto.getAge());
			existingVisitor.setCompanyName(visitorMeetingDto.getCompanyName());
			existingVisitor.setImage(visitorMeetingDto.getImageUrl());
			existingVisitor.setPhoneNumber(visitorMeetingDto.getPhoneNumber());

			State state = stateDao.getStateById(visitorMeetingDto.getState().getId());
			existingVisitor.setState(state);

			City city = cityDao.getCityById(visitorMeetingDto.getCity().getId());
			existingVisitor.setCity(city);

			Visitor updatedVisitor = visitorDao.updateVisitor(existingVisitor);

			MeetingDto meetingDto = new MeetingDto();
			meetingDto.setVisitor(VisitorDto.convertToDTO(updatedVisitor));
			meetingDto.setUser(visitorMeetingDto.getUser());
			meetingDto.setContext(visitorMeetingDto.getContext());
			meetingDto.setRemarks(visitorMeetingDto.getRemarks());
			meetingDto.setPhone(visitorMeetingDto.getPhoneNumber());

			Response<?> savedMeetingResponse = this.meetingService.addMeeting(meetingDto);

			return savedMeetingResponse;
		} else {
			return new Response<>("Visitor not found", null, HttpStatus.NOT_FOUND.value());
		}
	}

	@Override
	public Response<?> updateCheckoutStatus2(String phone, Integer companyId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<?> searchCompanyByName(String companyName) {

		if (companyName != null) {
			List<VisitorCompany> searchCompanyByName = visitorCompanyDao.searchCompanyByName(companyName);

			if (!searchCompanyByName.isEmpty() && searchCompanyByName.size() > 0) {
				List<VisitorCompanyDto> dtoList = searchCompanyByName.stream().map(VisitorCompanyDto::toDto)
						.collect(Collectors.toList());

				return new Response<>("Visitor Company found ", dtoList, HttpStatus.OK.value());
			}
			return new Response<>(" Visitor Company Not found ", null, HttpStatus.BAD_REQUEST.value());
		} else {
			return new Response<>("Provide  Visitor Comapany Name ", null, HttpStatus.BAD_REQUEST.value());
		}

	}

}
