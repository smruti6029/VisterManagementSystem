package com.vms2.service;

import java.util.List;

import com.vms2.dto.VisitormeetingDTO;
import com.vms2.entity.Visitor;
import com.vms2.response.Response;



public interface VisitorService {
	
	List<Visitor> getAllVisitors();

	Visitor getVisitorById(Integer id);

	Response<?> addVisitor(VisitormeetingDTO visitorDto);

	Visitor updateVisitor(Integer visitorId, VisitormeetingDTO visitorDto);

	Response<?> addVisitorByuser(VisitormeetingDTO visitorDto);

	Response<?> updateCheckoutStatus(String phone);	

}

