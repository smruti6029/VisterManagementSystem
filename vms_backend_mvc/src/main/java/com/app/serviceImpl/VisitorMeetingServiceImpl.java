package com.app.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.app.Dao.CityDao;
import com.app.Dao.StateDao;
import com.app.Dao.VisitorCompanyDao;
import com.app.Dao.VisitorDao;
import com.app.dto.MeetingDto;
import com.app.dto.VisitorCompanyDto;
import com.app.dto.VisitorDto;
import com.app.dto.VisitorMeetingDto;
import com.app.entity.City;
import com.app.entity.State;
import com.app.entity.Visitor;
import com.app.entity.VisitorCompany;
import com.app.response.Response;
import com.app.service.MeetingService;
import com.app.service.VisitorCompanyService;
import com.app.service.VisitorMeetingService;
import com.app.service.VisitorService;

@Service
public class VisitorMeetingServiceImpl implements VisitorMeetingService {

	@Autowired
	private VisitorService visitorService;

	@Autowired
	private MeetingService meetingService;

	@Autowired
	private VisitorCompanyService visitorCompanyService;

	@Autowired
	private StateDao stateDao;

	@Autowired
	private CityDao cityDao;

	@Autowired
	private VisitorDao visitorDao;

	@Autowired
	private VisitorCompanyDao visitorCompanyDao;

	@Override
	public Response<?> save(VisitorMeetingDto visitorMeetingDto) throws Exception {

		Visitor existingVisitor = visitorService.serachVisitorByphone(visitorMeetingDto.getPhoneNumber());
		
		if(visitorMeetingDto.getVisitorCompany() == null && visitorMeetingDto.getVisitorCompany().getName().isEmpty()) {
		    return new Response<>("Provide Company", null, HttpStatus.BAD_REQUEST.value());
		}


		if (existingVisitor == null) {
			VisitorDto visitorDto = new VisitorDto();
			visitorDto.setName(visitorMeetingDto.getName());
			visitorDto.setPhoneNumber(visitorMeetingDto.getPhoneNumber());
			visitorDto.setEmail(visitorMeetingDto.getEmail().toLowerCase());
			visitorDto.setAge(visitorMeetingDto.getAge());
			if (visitorMeetingDto.getVisitorCompany() != null) {
				if (visitorMeetingDto.getVisitorCompany().getId() == null) {
					if (visitorMeetingDto.getVisitorCompany().getName() != null) {

						VisitorCompany visitorCompany = new VisitorCompany();

						VisitorCompany bycompanyName = visitorCompanyDao
								.getBycompanyName(visitorMeetingDto.getVisitorCompany().getName());

						if (bycompanyName != null) {
							return new Response<>("Company Name already exist. Provide company ID", null,
									HttpStatus.BAD_REQUEST.value());
						}
						visitorCompany.setName(visitorMeetingDto.getVisitorCompany().getName().trim());

						VisitorCompany savedCompany = visitorCompanyDao.save(visitorCompany);

						if (savedCompany != null) {
							visitorDto.setVisitorCompanyDto(VisitorCompanyDto.toDto(savedCompany));
						} else {
							return new Response<>("Failed to save VisitorCompany", null,
									HttpStatus.BAD_REQUEST.value());
						}
					} else {
						return new Response<>("Provide company data", null, HttpStatus.BAD_REQUEST.value());
					}
				} else {
					VisitorCompany visitorCompany = visitorCompanyDao
							.getById(visitorMeetingDto.getVisitorCompany().getId());
					if (visitorCompany != null) {
						visitorDto.setVisitorCompanyDto(visitorMeetingDto.getVisitorCompany());
					} else {
						return new Response<>("Provide Valid CompanyId", null, HttpStatus.BAD_REQUEST.value());
					}
				}
			} else {
				return new Response<>("Provide Visitors Company Details", null, HttpStatus.BAD_REQUEST.value());
			}

			visitorDto.setImageUrl(visitorMeetingDto.getImageUrl());
			visitorDto.setState(visitorMeetingDto.getState());
			visitorDto.setCity(visitorMeetingDto.getCity());

			Response<?> savedVisitorDto = this.visitorService.addVisitor(visitorDto);

			if (savedVisitorDto.getStatus() == HttpStatus.OK.value()) {
				VisitorDto visitors = (VisitorDto) savedVisitorDto.getData();
				return addMeeting(visitorMeetingDto, visitors);
			} else {
				return new Response<>("Fail", null, HttpStatus.BAD_REQUEST.value());
			}

		} else

		{

			if (visitorMeetingDto.getVisitorCompany().getId() == null) {

				VisitorCompany visitorCompany = new VisitorCompany();

				VisitorCompany bycompanyName = visitorCompanyDao
						.getBycompanyName(visitorMeetingDto.getVisitorCompany().getName());

				if (bycompanyName == null) {
					visitorCompany.setName(visitorMeetingDto.getVisitorCompany().getName());

					VisitorCompany savedCompany = visitorCompanyDao.save(visitorCompany);
					existingVisitor.setCompany(savedCompany);
				} else {
					existingVisitor.setCompany(bycompanyName);
				}

			}

			existingVisitor.setName(visitorMeetingDto.getName());
			existingVisitor.setPhoneNumber(visitorMeetingDto.getPhoneNumber());
			existingVisitor.setEmail(visitorMeetingDto.getEmail().toLowerCase());
			existingVisitor.setAge(visitorMeetingDto.getAge());
			try {
				VisitorCompany visitorCompany = visitorCompanyDao
						.getById(visitorMeetingDto.getVisitorCompany().getId());
				if (visitorCompany != null) {
					existingVisitor.setCompany(VisitorCompanyDto.toEntity(visitorMeetingDto.getVisitorCompany()));

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			// existingVisitor.setCompany(VisitorCompanyDto.toEntity(visitorMeetingDto.getVisitorCompany()));
			existingVisitor.setImage(visitorMeetingDto.getImageUrl());
			existingVisitor.setPhoneNumber(visitorMeetingDto.getPhoneNumber());

			State state = stateDao.getStateById(visitorMeetingDto.getState().getId());
			existingVisitor.setState(state);

			City city = cityDao.getCityById(visitorMeetingDto.getCity().getId());
			existingVisitor.setCity(city);

			Visitor updatedVisitor = visitorDao.updateVisitor(existingVisitor);

			return addMeeting(visitorMeetingDto, VisitorDto.convertToDTO(updatedVisitor));
		}
	}

	private Response<?> addMeeting(VisitorMeetingDto visitorMeetingDto, VisitorDto visitorDto) throws Exception {

		MeetingDto meetingDto = new MeetingDto();
		meetingDto.setVisitor(visitorDto);
		meetingDto.setUser(visitorMeetingDto.getUser());
		meetingDto.setContext(visitorMeetingDto.getContext());
		meetingDto.setRemarks(visitorMeetingDto.getRemarks());
		meetingDto.setPhone(visitorMeetingDto.getPhoneNumber());

		Response<?> savedMeetingResponse = this.meetingService.addMeeting(meetingDto);

		return savedMeetingResponse;

	}

}
