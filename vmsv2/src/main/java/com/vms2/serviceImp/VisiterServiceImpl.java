package com.vms2.serviceImp;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vms2.dao.MeetingDao;
import com.vms2.dao.UserDao;
import com.vms2.dao.VisitorDao;
import com.vms2.dto.VisitormeetingDTO;
import com.vms2.entity.Meeting;
import com.vms2.entity.User;
import com.vms2.entity.Visitor;
import com.vms2.response.Response;
import com.vms2.service.MeetingService;
import com.vms2.service.VisitorService;




@Service
public class VisiterServiceImpl implements VisitorService{
	
	
	@Autowired
	private VisitorDao visitorDao;
	
	@Autowired
	private MeetingService meetingService;
	
	@Autowired
	private MeetingDao meetingDao;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public Response<?> addVisitor(VisitormeetingDTO visitorDto) {
		
		 Visitor visitor = VisitormeetingDTO.toVisitor(visitorDto);
		 
		 User user =  new User();
		 
		 Meeting meetingByUseridandDate = meetingDao.getMeetingByUseridandDate(visitorDto);
		 if(meetingByUseridandDate!=null)
		 {
			 return  new Response<>("Employee Is Busy In Another Meeting",null,HttpStatus.BAD_REQUEST.value());
		 }
 
		 Visitor addVisitor = visitorDao.addVisitor(visitor);
		 
		 
		 if(addVisitor!=null)
		 {
			 meetingService.addMeeting(visitorDto);
		 }
		 return  new Response<>("Your Request IS Registerd Wait For Response ",null,HttpStatus.CREATED.value());
	}

	@Override
	public List<Visitor> getAllVisitors() {
		
		List<Visitor> allVisitors = visitorDao.getAllVisitors();
		
		return  allVisitors;
	}

	@Override
	public Visitor getVisitorById(Integer id) {
		
		return visitorDao.getVisitorById(id);
		 
	   
	}



	@Override
	public Visitor updateVisitor(Integer visitorId, VisitormeetingDTO visitorDto) {
	
		return visitorDao.updateVisitor(visitorId, visitorDto);
		
	}

	
	@Override
	public Response<?> addVisitorByuser(VisitormeetingDTO visitorDto) {
		
		 Visitor visitorByPhone = visitorDao.getVisitorByPhone(visitorDto.getPhoneNumber());
		
		 if(visitorByPhone!=null)
		 {
		 Visitor visitor = VisitormeetingDTO.toVisitor(visitorDto);
		 User getuserByid = userDao.getuserByid(visitorDto.getUser().getId());
		
		 visitor.setCreatedBy(getuserByid.getFirstname());
		 
		 Visitor addVisitor = visitorDao.addVisitor(visitor);
		 
		 if(addVisitor!=null)
		 {
			 Response<?> addMeetingByuser = meetingService.addMeetingByuser(visitorDto);
		 }
		 return new Response<>("Visitor Added Succesfully",visitorDto,HttpStatus.OK.value());
		 }
		 else
		 {
			 return new Response<>("Phone Numnber Already Exit",null,HttpStatus.BAD_REQUEST.value()); 
		 }
	}

	@Override
	public Response<?> updateCheckoutStatus(String phone) {
		
		Visitor visitor = visitorDao.getVisitorByPhone(phone);
		if(visitor==null)
		{
			return new Response<>("No Visitor Found",null,HttpStatus.BAD_REQUEST.value());
		}
		
		Response<?> updateCheckoutStatus = meetingService.updateCheckoutStatus(visitor);
		
		return updateCheckoutStatus;
	}

	
	


}
