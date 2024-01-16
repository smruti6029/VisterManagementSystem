package com.app.DaoImpl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.Dao.VisitorDao;
import com.app.dto.CityDto;
import com.app.dto.StateDto;
import com.app.dto.VisitorMeetingDetailsDto;
import com.app.dto.VisitorMeetingDto;
import com.app.entity.Meeting;
import com.app.entity.Visitor;

@Repository
@Transactional
public class VisitorDaoImpl implements VisitorDao {

	@Autowired
	private SessionFactory sessionFactory;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Visitor> getAllVisitors() {
		try {

			Session session = sessionFactory.getCurrentSession();

			Criteria criteria = session.createCriteria(Visitor.class);

			return criteria.list();

		} catch (Exception e) {

			return null;
		}
	}

	@Override
	public Visitor addVisitor(VisitorMeetingDto visitorDto) {

		try {

			Session session = sessionFactory.getCurrentSession();

			Visitor visitor = VisitorMeetingDto.toVisitor(visitorDto);

			Serializable id = session.save(visitor);

			return session.get(Visitor.class, id);

		} catch (Exception e) {

			return null;
		}

	}

	@Override
	public Visitor getVisitorById(Integer id) {

		try {
			Session session = sessionFactory.getCurrentSession();

			return session.get(Visitor.class, id);

		} catch (Exception e) {

			return null;

		}
	}

	@Override
	public Visitor updateVisitor(String phoneNumber, VisitorMeetingDto visitorDto) {

		try {

			Session session = sessionFactory.getCurrentSession();

			Visitor existingVisiter = session.get(Visitor.class, phoneNumber);

			if (existingVisiter != null) {

				existingVisiter.setName(visitorDto.getName());
				existingVisiter.setEmail(visitorDto.getEmail());
				existingVisiter.setAddress(visitorDto.getAddress());
				existingVisiter.setCity(CityDto.convertDtoToEntity(visitorDto.getCity()));
				existingVisiter.setState(StateDto.convertDTOToEntity(visitorDto.getState()));
				existingVisiter.setImage(visitorDto.getImageUrl());
				existingVisiter.setCompanyName(visitorDto.getCompanyName());
				existingVisiter.setUpdatedBy(visitorDto.getName());
				existingVisiter.setModifiedOn(new Timestamp(System.currentTimeMillis()));

			}

			return existingVisiter;

		} catch (Exception e) {

			return null;
		}

	}

	@Override
	public Visitor getVisitorByEmail(String email) {

		try {

			Session session = sessionFactory.getCurrentSession();

			Criteria criteria = session.createCriteria(Visitor.class);

			criteria.add(Restrictions.eq("email", email));

			return (Visitor) criteria.uniqueResult();

		} catch (Exception e) {

			return null;
		}
	}

	@Override
	public Visitor addVisitor(Visitor visitor) {

		try {
			Session session = sessionFactory.getCurrentSession();

			session.save(visitor);
			return visitor;

		} catch (Exception e) {

			return null;
		}

	}

	@Override
	public Visitor findByPhone(String phone) {

		try {

			Session session = sessionFactory.getCurrentSession();

			Criteria criteria = session.createCriteria(Visitor.class);

			criteria.add(Restrictions.eq("phoneNumber", phone));

			return (Visitor) criteria.uniqueResult();

		} catch (Exception e) {

			return null;
		}

	}

	@Override
	public Visitor serachVisitor(String phoneNumber) {

		try {

			Session session = sessionFactory.getCurrentSession();

			Criteria criteria = session.createCriteria(Visitor.class);

			criteria.add(Restrictions.eq("phoneNumber", phoneNumber));

			return (Visitor) criteria.uniqueResult();

		} catch (Exception e) {

			return null;
		}

	}

	@Override
	public Visitor updateVisitor(Visitor serachVisitor) {
		try {

			Session session = sessionFactory.getCurrentSession();

			session.update(serachVisitor);

			return serachVisitor;

		} catch (Exception e) {

			return null;
		}
	}

	@Override
	public List<VisitorMeetingDetailsDto> getAllVisitorsByUserId(Integer userId) {

		try {

			String jpql = "SELECT new com.app.dto.VisitorMeetingDetailsDto(v,m) " + "FROM Visitor v "
					+ "LEFT JOIN v.meetings m " + "WHERE m.employee.id = " + userId;

//		String jpql = "SELECT * FROM visitor v LEFT JOIN meeting m on v.id=m.visitor_id WHERE m.employee_id =:userId";

			Query query = (Query) entityManager.createQuery(jpql, VisitorMeetingDetailsDto.class);

//		query.setParameter("userId", userId);

			List<VisitorMeetingDetailsDto> result = query.getResultList();

			return query.getResultList();

		} catch (Exception e) {

			return null;
		}
	}

	@Override
	public List<Meeting> getAllMeeting() {

		try {

			Session session = sessionFactory.getCurrentSession();

			Criteria criteria = session.createCriteria(Meeting.class);

			return criteria.list();

		} catch (Exception e) {

			return null;
		}
	}

	@Override
	public Meeting getTodayMeeting(Integer id) {

		try {

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
			criteria.add(Restrictions.eq("visitor.id", id));
			criteria.add(Restrictions.between("createdAt", startOfDay, endOfDay));

			// Retrieve a single meeting (assuming there's only one for the given criteria)
			Meeting meeting = (Meeting) criteria.uniqueResult();

			return meeting;

		} catch (Exception e) {

			return null;
		}
	}

////	@Override
////	public List<Meeting> serachVisitor2(String phone, Integer companyId) {
////
////		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Meeting.class);
////
////		// Add the criteria to filter a meeting for the given visitor's phone and
////		// company ID
////		criteria.createAlias("visitor", "v");
////		criteria.createAlias("employee", "e");
////		criteria.createAlias("e.company", "c");
////
////		criteria.add(Restrictions.eq("v.phoneNumber", phone));
////		criteria.add(Restrictions.eq("c.id", companyId));
////
////		LocalDate currentDateIST = LocalDate.now(ZoneId.of("Asia/Kolkata")); // IST time zone
////		LocalDate currentDateUTC = currentDateIST.atStartOfDay().toInstant(ZoneOffset.UTC)
////				.atZone(ZoneId.systemDefault()).toLocalDate();
////
////		criteria.add(Restrictions.between("createdAt",
////				Date.from(currentDateUTC.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
////				Date.from(currentDateUTC.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));
////
////		// If needed, you can add additional status restrictions using Disjunction
//////	    Disjunction or = Restrictions.disjunction();
//////	    or.add(Restrictions.eq("status", MeetingStatus.PENDING));
//////	    or.add(Restrictions.eq("status", MeetingStatus.APPROVED));
//////	    or.add(Restrictions.eq("status", MeetingStatus.INPROCESS));
//////	    or.add(Restrictions.eq("status", MeetingStatus.COMPLETED));
//////	    criteria.add(or);
////
////		return criteria.list();
////		 try (Session session = sessionFactory.getCurrentSession()) {
////			 System.out.println("in dao");
////	            Criteria criteria = session.createCriteria(Visitor.class, "v");
////	            
////	            criteria.createAlias("v.meetings", "m");
////	            criteria.createAlias("m.employee", "u");
////	            criteria.createAlias("u.company", "c");
////
////	            criteria.add(Restrictions.eq("v.phoneNumber", phone));
////	            criteria.add(Restrictions.eq("c.id", companyId));
////
////	            return  (Visitor) criteria.list();
////	        }
//
////		 try (Session session = sessionFactory.getCurrentSession()) {
////			 
////			 System.out.println("in dao");
////	            String hql = "SELECT v FROM visitor v " +
////	                         "JOIN v.meeting m " +
////	                         "JOIN m.user u " +
////	                         "JOIN u.company c " +
////	                         "WHERE v.phone_number = :phone" +
////	                         "AND c.id = :companyId";
////
////	            Query<Visitor> query = session.createQuery(hql, Visitor.class);
////	            query.setParameter("phone", phone);
////	            query.setParameter("companyId", companyId);
////
////	            return (Visitor) query.list();
////	        }
//	return null;
//	}

}
