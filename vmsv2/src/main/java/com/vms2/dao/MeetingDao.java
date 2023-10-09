package com.vms2.dao;

import java.util.List;

import com.vms2.dto.IdIsactiveDTO;
import com.vms2.dto.VisitormeetingDTO;
import com.vms2.entity.Meeting;

public interface MeetingDao {

	
    public Meeting save(Meeting meeting);
	
	public Meeting getById(Integer integer);
	
	public  List<Meeting> getAll();
	
	public Meeting update(Meeting meeting);
	
	public Meeting delete(IdIsactiveDTO idIsactiveDTO);

	public List<Meeting> getMeetingsByUserId(Integer id);


	Meeting getMeetingByVisitorIdAndTodayDate(Integer vid);

	Meeting getMeetingByUseridandDate(VisitormeetingDTO addVisitor);
}
