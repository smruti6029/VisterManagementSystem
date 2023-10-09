package com.vms2.daoImp;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vms2.dao.MeetingDao;
import com.vms2.dto.IdIsactiveDTO;
import com.vms2.dto.VisitormeetingDTO;
import com.vms2.entity.Meeting;


@Repository
@Transactional
public class MeetingDaoImp implements MeetingDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Meeting save(Meeting meeting) {
		try
		{
			sessionFactory.getCurrentSession().save(meeting);
			return meeting;
		}catch (Exception e) {
			return null;
		}
		
	}

	@Override
	public Meeting getById(Integer id) {
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Meeting.class); 
			
		    criteria.add(Restrictions.eq("id", id));
		return (Meeting) criteria.uniqueResult();
	}

	@Override
	public List<Meeting> getAll() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Meeting.class);
		return criteria.list();
		
		
	}

	@Override
	public Meeting update(Meeting meeting) {
		try
		{
		sessionFactory.getCurrentSession().update(meeting);
		return meeting;
		}catch (Exception e) {
			return null;
		}
		
	}

	@Override
	public Meeting delete(IdIsactiveDTO idIsactiveDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Meeting> getMeetingsByUserId(Integer userId) {
	    Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Meeting.class); 
	
	    criteria.add(Restrictions.eq("employee.id", userId));
	    return criteria.list();
	}
	
		@Override
		public Meeting getMeetingByVisitorIdAndTodayDate(Integer vid) {
		    Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Meeting.class);

		   
		    Date today = new Date();

		    // Set the time of the date to the beginning of the day (midnight)
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(today);
		    calendar.set(Calendar.HOUR_OF_DAY, 0);
		    calendar.set(Calendar.MINUTE, 0);
		    calendar.set(Calendar.SECOND, 0);
		    calendar.set(Calendar.MILLISECOND, 0);
		    Date startOfDay = calendar.getTime();

		    // Set the time of the date to the end of the day (23:59:59.999)
		    calendar.set(Calendar.HOUR_OF_DAY, 23);
		    calendar.set(Calendar.MINUTE, 59);
		    calendar.set(Calendar.SECOND, 59);
		    calendar.set(Calendar.MILLISECOND, 999);
		    Date endOfDay = calendar.getTime();

		    // Add the criteria to filter a meeting for the given visitor on today's date
		    criteria.add(Restrictions.eq("visitor.id", vid));
		    criteria.add(Restrictions.between("meetingStartDateTime", startOfDay, endOfDay));

		    // Retrieve a single meeting (assuming there's only one for the given criteria)
		    Meeting meeting = (Meeting) criteria.uniqueResult();

		    return meeting;
		}

		@Override
		public Meeting getMeetingByUseridandDate(VisitormeetingDTO addVisitor) {
		    Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Meeting.class);

		    // Add the criteria to filter a meeting for the given user and date range
		    criteria.add(Restrictions.eq("user.id", addVisitor.getUser().getId()));
		    criteria.add(Restrictions.between("meetingStartDateTime", 
		        addVisitor.getMeetingStartDateTime(), addVisitor.getMeetingEndDateTIme()));

		    // Retrieve a single meeting (assuming there's only one for the given criteria)
		    Meeting meeting = (Meeting) criteria.uniqueResult();

		    return meeting;
		}

	}
	


