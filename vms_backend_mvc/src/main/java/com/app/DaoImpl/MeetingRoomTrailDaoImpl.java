package com.app.DaoImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.Dao.MeetingRoomTrailDao;
import com.app.entity.MeetingRoomTrial;

@Repository
@Transactional
public class MeetingRoomTrailDaoImpl implements MeetingRoomTrailDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public MeetingRoomTrial save(MeetingRoomTrial meetingRoomTrial) {
		try {
			sessionFactory.getCurrentSession().save(meetingRoomTrial);
			return meetingRoomTrial;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<MeetingRoomTrial> getTrialDataByMeetingId(Integer meetingId) {
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MeetingRoomTrial.class);
			criteria.add(Restrictions.eq("meetingId", meetingId));
			criteria.addOrder(Order.desc("createdAt"));
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
