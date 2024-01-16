package com.app.DaoImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.Dao.NotificationDao;
import com.app.emun.MeetingStatus;
import com.app.entity.Meeting;
import com.app.entity.Notification;
import com.app.entity.User;

@Repository
@Transactional
public class NotificationDaoImpl implements NotificationDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Notification save(Notification notification) {
		try {
			sessionFactory.getCurrentSession().save(notification);
			return notification;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Notification> getAll() {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Notification.class);
		criteria.add(Restrictions.eq("isActive", true));
		criteria.addOrder(Order.desc("updatedAt"));
		return criteria.list();
	}

	@Override
	public List<Notification> getNotificationsOfReceptionist(Integer companyId, Integer buildingId) {
		try {
			Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Notification.class,
					"notification");
			criteria.createAlias("notification.meeting", "meeting");
			criteria.createAlias("meeting.employee", "employee");
			criteria.createAlias("employee.company", "company");

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			Date startOfDay = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			Date endOfDay = calendar.getTime();

			criteria.add(Restrictions.eq("notification.userRole", "RECEPTIONIST"));
			if (companyId != null) {
				criteria.add(Restrictions.eq("company.id", companyId));
			}
			if (companyId == null && buildingId != null) {
//				criteria.createAlias("meeting.company", "company");
				criteria.add(Restrictions.eq("company.building.buildingId", buildingId));
			}
			criteria.add(Restrictions.between("notification.createdAt", startOfDay, endOfDay));
			criteria.addOrder(Order.desc("updatedAt"));
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Notification getById(Integer id) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Notification.class);
		criteria.add(Restrictions.eq("id", id));
		return (Notification) criteria.uniqueResult();
	}

//	@Override
//	public List<Notification> getNotificationsByUser(User user, Integer companyId) {
//		try {
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(new Date());
//			calendar.set(Calendar.HOUR_OF_DAY, 0);
//			calendar.set(Calendar.MINUTE, 0);
//			calendar.set(Calendar.SECOND, 0);
//			Date startOfDay = calendar.getTime();
//			calendar.set(Calendar.HOUR_OF_DAY, 23);
//			calendar.set(Calendar.MINUTE, 59);
//			calendar.set(Calendar.SECOND, 59);
//			Date endOfDay = calendar.getTime();
//
//			Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Notification.class,
//					"notification");
//			criteria.createAlias("notification.meeting", "meeting");
//
//			if (user != null) {
//				criteria.add(Restrictions.eq("meeting.employee", user));
//				criteria.add(Restrictions.or(Restrictions.eq("meeting.status", MeetingStatus.PENDING),
////						Restrictions.eq("meeting.status", MeetingStatus.CANCELLED_BY_VISITOR),
//						Restrictions.eq("meeting.status", MeetingStatus.APPROVED),
//						Restrictions.eq("meeting.status", MeetingStatus.COMPLETED),
//						Restrictions.eq("meeting.status", MeetingStatus.CANCELLED),
//						Restrictions.eq("meeting.status", MeetingStatus.INPROCESS)));
//				criteria.add(Restrictions.ne("notification.userRole", "RECEPTIONIST"));
//			} else {
//				criteria.add(Restrictions.or(Restrictions.eq("meeting.status", MeetingStatus.APPROVED),
//						Restrictions.eq("meeting.status", MeetingStatus.PENDING),
//						Restrictions.eq("meeting.status", MeetingStatus.COMPLETED),
//						Restrictions.eq("meeting.status", MeetingStatus.INPROCESS),
////						Restrictions.eq("meeting.status", MeetingStatus.CANCELLED_BY_VISITOR),
//						Restrictions.eq("meeting.status", MeetingStatus.CANCELLED)));
//				criteria.createAlias("meeting.employee", "employee");
//				criteria.add(Restrictions.eq("employee.company.id", companyId));
//
//				criteria.add(Restrictions.eq("notification.userRole", "RECEPTIONIST"));
//			}
//
//			criteria.add(Restrictions.between("notification.createdAt", startOfDay, endOfDay));
//			criteria.addOrder(Order.desc("notification.updatedAt"));
//
//			return criteria.list();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

	@Override
	public List<Notification> getNotificationsByUser(User user, Integer companyId, Integer buildingId) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			Date startOfDay = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			Date endOfDay = calendar.getTime();

			Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Notification.class,
					"notification");
			criteria.createAlias("notification.meeting", "meeting");

			if (user != null) {
				criteria.add(Restrictions.eq("meeting.employee", user));
				criteria.add(Restrictions.or(Restrictions.eq("meeting.status", MeetingStatus.PENDING),
//						Restrictions.eq("meeting.status", MeetingStatus.CANCELLED_BY_VISITOR),
						Restrictions.eq("meeting.status", MeetingStatus.APPROVED),
						Restrictions.eq("meeting.status", MeetingStatus.COMPLETED),
						Restrictions.eq("meeting.status", MeetingStatus.CANCELLED),
						Restrictions.eq("meeting.status", MeetingStatus.INPROCESS)));
				criteria.add(Restrictions.ne("notification.userRole", "RECEPTIONIST"));
			} else {
				criteria.add(Restrictions.or(Restrictions.eq("meeting.status", MeetingStatus.APPROVED),
						Restrictions.eq("meeting.status", MeetingStatus.PENDING),
						Restrictions.eq("meeting.status", MeetingStatus.COMPLETED),
						Restrictions.eq("meeting.status", MeetingStatus.INPROCESS),
						Restrictions.eq("meeting.status", MeetingStatus.CANCELLED)));
				criteria.createAlias("meeting.company", "company");

				if (buildingId != null && companyId == null) {
					// Get all notifications for the given buildingId
					criteria.add(Restrictions.eq("company.building.buildingId", buildingId));
				} else if (buildingId == null && companyId != null) {
					// Get notifications based on company
					criteria.add(Restrictions.eq("company.id", companyId));
				} else if (buildingId != null && companyId != null) {
					// Get notifications based on both buildingId and companyId
					criteria.add(Restrictions.eq("company.building.buildingId", buildingId));
					criteria.add(Restrictions.eq("company.id", companyId));
				} else {
					// This block was updated to include both building and company restrictions
					criteria.add(Restrictions.isNull("company.id")); // Additional check for company ID being null
					criteria.add(Restrictions.eq("company.building.buildingId", buildingId));
				}

				criteria.add(Restrictions.eq("notification.userRole", "RECEPTIONIST"));
			}

			criteria.add(Restrictions.between("notification.createdAt", startOfDay, endOfDay));
			criteria.addOrder(Order.desc("notification.updatedAt"));

			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Add a method to get notifications by meeting if it doesn't exist already
	public List<Notification> getNotificationsByMeeting(Meeting meeting) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Notification.class);
		criteria.add(Restrictions.eq("meeting", meeting));
		criteria.addOrder(Order.desc("updatedAt"));
		return criteria.list();
	}

	@Override
	public Notification update(Notification notification) {
		try {
			sessionFactory.getCurrentSession().update(notification);
			return notification;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Notification getByMeetingId(Integer meetingId) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Notification.class);

			Criterion meetingIdCriterion = Restrictions.eq("meeting.id", meetingId);
			criteria.add(meetingIdCriterion);

			return (Notification) criteria.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void deleteNotificationsOlderThanDays(Integer schedulerValue) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Notification.class);

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -schedulerValue);
			Date thresholdDate = calendar.getTime();

			criteria.add(Restrictions.lt("createdAt", thresholdDate));

			List<Notification> notificationsToDelete = criteria.list();
			for (Notification notification : notificationsToDelete) {
				session.delete(notification);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
