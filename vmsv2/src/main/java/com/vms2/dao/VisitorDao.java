package com.vms2.dao;


import java.util.List;

import com.vms2.dto.VisitormeetingDTO;
import com.vms2.entity.Meeting;
import com.vms2.entity.Visitor;

public interface VisitorDao {
	
	     List<Visitor> getAllVisitors();

	    Visitor getVisitorById(Integer id);

	    Visitor addVisitor(VisitormeetingDTO visitorDto);

	    Visitor updateVisitor(Integer visiterId ,  VisitormeetingDTO visitorDto);

		Visitor addVisitor(Visitor visitor);

		Visitor getVisitorByPhone(String phoneNumber);



}

