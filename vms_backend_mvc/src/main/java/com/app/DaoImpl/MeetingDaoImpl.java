package com.app.DaoImpl;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.app.Dao.MeetingDao;
import com.app.Dao.VisitorDao;
import com.app.dto.IsActiveDto;
import com.app.dto.MeetingDto;
import com.app.dto.MeetingHourDto;
import com.app.dto.PaginationRequest;
import com.app.emun.MeetingStatus;
import com.app.entity.Meeting;
import com.app.entity.Room;
import com.app.entity.Visitor;

@Repository
@Transactional
public class MeetingDaoImpl implements MeetingDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private VisitorDao visitorDao;

	@Override
	public List<Meeting> getMeetingsByVisitor(Visitor visitor) {

		Session session = sessionFactory.getCurrentSession();

		Query<Meeting> query = session.createQuery("FROM Meeting WHERE visitor = :visitor", Meeting.class);

		query.setParameter("visitor", visitor);

		return query.getResultList();
	}

	@Override
	public Meeting save(Meeting meeting) {
		try {
			sessionFactory.getCurrentSession().save(meeting);
			return meeting;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Meeting getById(Integer id) {

		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Meeting.class);

			criteria.add(Restrictions.eq("id", id));

			return (Meeting) criteria.uniqueResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Meeting> getAll(Integer companyId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Meeting.class, "meeting");

		if (companyId != null) {
			criteria.createAlias("meeting.employee", "employee"); // Create an alias for joining User (employee)

			criteria.add(Restrictions.eq("employee.company.id", companyId)); // Filter by company_id
		}

		List<Meeting> meetings = criteria.list();

		// Sorting logic
		List<Meeting> sortedMeetings = meetings.stream().sorted((m1, m2) -> {
			if (m1.getStatus() == MeetingStatus.PENDING && m2.getStatus() != MeetingStatus.PENDING) {
				return -1;
			} else if (m1.getStatus() != MeetingStatus.PENDING && m2.getStatus() == MeetingStatus.PENDING) {
				return 1;
			} else {
				return m2.getCreatedAt().compareTo(m1.getCreatedAt());
			}
		}).collect(Collectors.toList());

		return sortedMeetings;

	}

	@Override
	public Meeting update(Meeting meeting) {
		try {
			meeting.setUpdatedAt(new Date());
			sessionFactory.getCurrentSession().update(meeting);
			return meeting;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Meeting delete(IsActiveDto idIsactiveDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Meeting> getMeetingsByUserId(Integer userId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Meeting.class);

		criteria.add(Restrictions.eq("employee.id", userId));

		List<Meeting> meetings = criteria.list();

		List<Meeting> sortedMeetings = meetings.stream().sorted((m1, m2) -> {
			if (m1.getStatus() == MeetingStatus.PENDING && m2.getStatus() != MeetingStatus.PENDING) {
				return -1;
			} else if (m1.getStatus() != MeetingStatus.PENDING && m2.getStatus() == MeetingStatus.PENDING) {
				return 1;
			} else {
				return m2.getCreatedAt().compareTo(m1.getCreatedAt());
			}
		}).collect(Collectors.toList());

		return sortedMeetings;

	}

	@Override
	public Meeting checkIfRoomAvailable(Room room, Date startDate, Date endDate) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Meeting.class);

		criteria.add(Restrictions.eq("room", room));
		criteria.add(Restrictions.or(
				Restrictions.and(Restrictions.ge("meetingStartDateTime", startDate),
						Restrictions.le("meetingEndDateTime", endDate)),
				Restrictions.and(Restrictions.le("meetingStartDateTime", startDate),
						Restrictions.ge("meetingEndDateTime", startDate))));

		Meeting meeting = (Meeting) criteria.uniqueResult();

		return meeting;
	}

	@Override
	public List<Meeting> getMeetingByVisitorIdAndTodayDate(Integer vid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Meeting.class);

		LocalDate currentDateIST = LocalDate.now(ZoneId.of("Asia/Kolkata")); // IST time zone
		LocalDate currentDateUTC = currentDateIST.atStartOfDay().toInstant(ZoneOffset.UTC)
				.atZone(ZoneId.systemDefault()).toLocalDate();

		// Add the criteria to filter a meeting for the given visitor on today's date
		criteria.add(Restrictions.eq("visitor.id", vid));
		criteria.add(Restrictions.between("createdAt",
				Date.from(currentDateUTC.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
				Date.from(currentDateUTC.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));

//		Disjunction or = Restrictions.disjunction();
//		or.add(Restrictions.eq("status", MeetingStatus.PENDING));
//		or.add(Restrictions.eq("status", MeetingStatus.APPROVED));
//		or.add(Restrictions.eq("status", MeetingStatus.INPROCESS));
//		or.add(Restrictions.eq("status", MeetingStatus.COMPLETED));
//		criteria.add(or);

		return criteria.list();
	}

	@Override
	public List<Meeting> getMeetingByVisitorIdAndCompanyId(Integer visitorId, Integer companyId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Meeting.class);

		LocalDate currentDateIST = LocalDate.now(ZoneId.of("Asia/Kolkata")); // IST time zone
		LocalDate currentDateUTC = currentDateIST.atStartOfDay().toInstant(ZoneOffset.UTC)
				.atZone(ZoneId.systemDefault()).toLocalDate();

		// Add the criteria to filter a meeting for the given visitor and company on
		// today's date
		criteria.addOrder(Order.desc("updatedAt"));
		criteria.createAlias("visitor", "v");
		criteria.createAlias("employee", "e");
		criteria.createAlias("e.company", "c");

		criteria.add(Restrictions.eq("v.id", visitorId));
		criteria.add(Restrictions.eq("c.id", companyId));
		criteria.add(Restrictions.between("createdAt",
				Date.from(currentDateUTC.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
				Date.from(currentDateUTC.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));

		// If needed, you can add additional status restrictions using Disjunction
//		    Disjunction or = Restrictions.disjunction();
//		    or.add(Restrictions.eq("status", MeetingStatus.PENDING));
//		    or.add(Restrictions.eq("status", MeetingStatus.APPROVED));
//		    or.add(Restrictions.eq("status", MeetingStatus.INPROCESS));
//		    or.add(Restrictions.eq("status", MeetingStatus.COMPLETED));
//		    criteria.add(or);

		return criteria.list();
	}

	@Override
	public Meeting getMeetingByUseridandDate(MeetingDto meetingDto) {
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Meeting.class);

			// Add the criteria to filter a meeting for the given user and date range
			criteria.add(Restrictions.eq("employee.id", meetingDto.getUser().getId()));
			criteria.add(Restrictions.between("meetingStartDateTime", meetingDto.getMeetingStartDateTime(),
					meetingDto.getMeetingEndDateTime()));

			// Retrieve a single meeting (assuming there's only one for the given criteria)
			Meeting meeting = (Meeting) criteria.uniqueResult();
			return meeting;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Meeting isEmployeeBusy(Integer userId, Date meetingStartDate, Date meetingEndDate) {
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Meeting.class);

			// Add the criteria to filter a meeting for the given user and date range
			criteria.createAlias("employee", "e");

			// Add a condition to match the companyId
			criteria.add(Restrictions.eq("e.id", userId));

			// Modify the condition to check for meetings that overlap with the specified
			// date range
			Criterion dateRangeOverlap = Restrictions.or(
					Restrictions.and(Restrictions.ge("meetingStartDateTime", meetingStartDate),
							Restrictions.le("meetingStartDateTime", meetingEndDate)),
					Restrictions.and(Restrictions.le("meetingStartDateTime", meetingStartDate),
							Restrictions.ge("meetingEndDateTime", meetingStartDate)));

			criteria.add(dateRangeOverlap);

			// Retrieve a single meeting (assuming there's only one for the given criteria)
			Meeting meeting = (Meeting) criteria.uniqueResult();
			return meeting;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Page<Meeting> getMeetingsByPagination(PaginationRequest request) {

		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(Meeting.class);

		criteria.addOrder(Order.desc("createdAt"));

		// Apply dynamic filtering based on the request
		if (request.getDate() != null) {
			Date selectedDate = request.getDate();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(selectedDate);

			// Set the time to the start of the selected date (midnight)
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			Date startDate = calendar.getTime();

			// Set the time to the end of the selected date (just before midnight)
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			Date endDate = calendar.getTime();

			criteria.add(Restrictions.ge("createdAt", startDate));
			criteria.add(Restrictions.le("createdAt", endDate));
		}

		if (request.getStatus() != null) {
			System.out.println("Request status: " + request.getStatus());
			criteria.add(Restrictions.eq("status", request.getStatus()));
		}

		if (request.getBuildingId() != null && request.getCompanyId() == null) {
	        criteria.createAlias("employee", "e1");
	        criteria.createAlias("e1.company", "c1");
	        criteria.createAlias("c1.building", "b");

	        criteria.add(Restrictions.eq("b.buildingId", request.getBuildingId())); 
	    }
		
		if (request.getBuildingId() == null && request.getCompanyId() != null) {
	        criteria.createAlias("employee", "e2");
	        criteria.createAlias("e2.company", "c2");

	        criteria.add(Restrictions.eq("c2.id", request.getCompanyId())); 
	    }
	      
	    if (request.getBuildingId() != null && request.getCompanyId() != null) {
	        // Assuming "employee" is a relationship with the "User" entity
	        criteria.createAlias("employee", "e");
	        criteria.createAlias("e.company", "c1");
	        criteria.createAlias("c1.building", "b1");

	        Conjunction conjunction = Restrictions.conjunction();
	        conjunction.add(Restrictions.eq("b1.buildingId", request.getBuildingId()));
	        conjunction.add(Restrictions.eq("c1.id", request.getCompanyId()));

	        criteria.add(conjunction);
	    }

		// User filter

		if (request.getUser() != null) {
			if (request.getUser().getId() != null) {
				criteria.add(Restrictions.eq("employee.id", request.getUser().getId()));
			}
		}

		// Room filter
		if (request.getRoom() != null) {
			if (request.getRoom().getId() != null) {
				criteria.add(Restrictions.eq("room.id", request.getRoom().getId()));

			}
		}

		// Visitor filter
		if (request.getPhoneNumber() != null) {

			System.out.println("inside");

			Visitor visitor = visitorDao.findByPhone(request.getPhoneNumber());

			criteria.add(Restrictions.eq("visitor", visitor));

		}

		criteria.setFirstResult(request.getPage() * request.getSize());

		criteria.setMaxResults(request.getSize());

		if (request.getFromDate() != null && request.getToDate() != null) {

			criteria.add(Restrictions.ge("createdAt", request.getFromDate()));

			// Convert java.util.Date to Calendar
			Calendar calendar = Calendar.getInstance();

			calendar.setTime(request.getToDate());

			// Add one day to the toDate
			calendar.add(Calendar.DAY_OF_MONTH, 1);

			// Convert Calendar back to java.util.Date
			Date toDatePlusOne = calendar.getTime();

			criteria.add(Restrictions.le("createdAt", toDatePlusOne));

		}

		@SuppressWarnings("unchecked")
		List<Meeting> results = criteria.list();

		int totalResults = getTotalMeetingCount(request);

		return new PageImpl(results, PageRequest.of(request.getPage(), request.getSize()), totalResults);
	}

	private int getTotalMeetingCount(PaginationRequest request) {

		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(Meeting.class);

		if (request.getDate() != null) {
			Date selectedDate = request.getDate();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(selectedDate);

			// Set the time to the start of the selected date (midnight)
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			Date startDate = calendar.getTime();

			// Set the time to the end of the selected date (just before midnight)
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			Date endDate = calendar.getTime();

			criteria.add(Restrictions.ge("createdAt", startDate));
			criteria.add(Restrictions.le("createdAt", endDate));
		}

		if (request.getStatus() != null) {
			criteria.add(Restrictions.eq("status", request.getStatus()));
		}

		if (request.getBuildingId() != null && request.getCompanyId() == null) {
	        criteria.createAlias("employee", "e1");
	        criteria.createAlias("e1.company", "c1");
	        criteria.createAlias("c1.building", "b");

	        criteria.add(Restrictions.eq("b.buildingId", request.getBuildingId())); 
	    }
	      
		if (request.getBuildingId() == null && request.getCompanyId() != null) {
	        criteria.createAlias("employee", "e2");
	        criteria.createAlias("e2.company", "c2");

	        criteria.add(Restrictions.eq("c2.id", request.getCompanyId())); 
	    }
		
	    if (request.getBuildingId() != null && request.getCompanyId() != null) {
	        // Assuming "employee" is a relationship with the "User" entity
	        criteria.createAlias("employee", "e");
	        criteria.createAlias("e.company", "c1");
	        criteria.createAlias("c1.building", "b1");

	        Conjunction conjunction = Restrictions.conjunction();
	        conjunction.add(Restrictions.eq("b1.buildingId", request.getBuildingId()));
	        conjunction.add(Restrictions.eq("c1.id", request.getCompanyId()));

	        criteria.add(conjunction);
	    }

		if (request.getFromDate() != null && request.getToDate() != null) {

			criteria.add(Restrictions.ge("createdAt", request.getFromDate()));

			// Convert java.util.Date to Calendar
			Calendar calendar = Calendar.getInstance();

			calendar.setTime(request.getToDate());

			// Add one day to the toDate
			calendar.add(Calendar.DAY_OF_MONTH, 1);

			// Convert Calendar back to java.util.Date
			Date toDatePlusOne = calendar.getTime();

			criteria.add(Restrictions.le("createdAt", toDatePlusOne));

		}

		if (request.getUser() != null) {
			if (request.getUser().getId() != null) {
				criteria.add(Restrictions.eq("employee.id", request.getUser().getId()));
			}
		}

		// Room filter
		if (request.getRoom() != null) {
			if (request.getRoom().getId() != null) {
				criteria.add(Restrictions.eq("room.id", request.getRoom().getId()));

			}
		}

		if (request.getPhoneNumber() != null) {

			Visitor visitor = visitorDao.findByPhone(request.getPhoneNumber());

			criteria.add(Restrictions.eq("visitor", visitor));

		}

		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	@Override
	public List<Meeting> getVisitorIncompleteMeetings(Integer vid, Integer companyId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Meeting.class);

		LocalDate currentDateIST = LocalDate.now(ZoneId.of("Asia/Kolkata")); // IST time zone
		LocalDate currentDateUTC = currentDateIST.atStartOfDay().toInstant(ZoneOffset.UTC)
				.atZone(ZoneId.systemDefault()).toLocalDate();

		// Add the criteria to filter a meeting for the given visitor on today's date
		criteria.add(Restrictions.eq("visitor.id", vid));
		criteria.add(Restrictions.between("createdAt",
				Date.from(currentDateUTC.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
				Date.from(currentDateUTC.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));

		Disjunction or = Restrictions.disjunction();
		or.add(Restrictions.eq("status", MeetingStatus.PENDING));
		or.add(Restrictions.eq("status", MeetingStatus.APPROVED));
		criteria.add(or);

		criteria.createAlias("employee", "e");
		criteria.add(Restrictions.eq("e.company.id", companyId));

		return criteria.list();
	}

	@Override
	public Meeting inProcessMeeting(Integer visitorId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Meeting.class);

		try {
			LocalDate currentDateIST = LocalDate.now(ZoneId.of("Asia/Kolkata")); // IST time zone
			LocalDate currentDateUTC = currentDateIST.atStartOfDay().toInstant(ZoneOffset.UTC)
					.atZone(ZoneId.systemDefault()).toLocalDate();

			// Add the criteria to filter a meeting for the given visitor on today's date
			criteria.add(Restrictions.eq("visitor.id", visitorId));
			criteria.add(Restrictions.between("createdAt",
					Date.from(currentDateUTC.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
					Date.from(currentDateUTC.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));

			Disjunction or = Restrictions.disjunction();
			or.add(Restrictions.eq("status", MeetingStatus.INPROCESS));
			or.add(Restrictions.eq("status", MeetingStatus.CANCELLED));
			criteria.add(or);

			return (Meeting) criteria.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public List<MeetingHourDto> getMeetingHoursLastWeek(List<Meeting> meetings, Date todayDate) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(todayDate);

		// Add one day to the toDate
		calendar.add(Calendar.DAY_OF_MONTH, 1);

		// Convert Calendar back to java.util.Date
		Date toDatePlusOne = calendar.getTime();

		calendar.setTime(toDatePlusOne);

		// Get the start date of the last week (Monday)
		calendar.add(Calendar.DAY_OF_YEAR, -7);
		Date lastWeekStartDate = calendar.getTime();

		List<MeetingHourDto> meetingHours = new ArrayList<>();

		// Iterate over each day of the last week
		for (int i = 0; i < 7; i++) {
			// Get the date of the current day
			calendar.setTime(lastWeekStartDate);
			calendar.add(Calendar.DAY_OF_YEAR, i);
			Date date = calendar.getTime();

			// Filter the meetings for the current day
			List<Meeting> meetingsForDay = meetings.stream().filter(meeting -> {
				if (meeting.getMeetingStartDateTime() == null || meeting.getMeetingEndDateTime() == null) {
					return false;
				}
				return meeting.getMeetingEndDateTime().after(date)
						&& meeting.getMeetingStartDateTime().before(getNextDay(date));
			}).collect(Collectors.toList());

			// Calculate the total meeting hours for the current day
			double totalMeetingHours = meetingsForDay.stream().mapToDouble(this::calculateMeetingHours).sum();

			totalMeetingHours = Double.parseDouble(String.format("%.2f", totalMeetingHours));

			double MeetingAverage = (double) (totalMeetingHours / meetingsForDay.size());

			MeetingAverage = Math.round(MeetingAverage * 1000) / 1000d;

//			 DecimalFormat df = new DecimalFormat("#.## hrs");
//			  String formattedMeetingHours = df.format(totalMeetingHours);

			// Create a MeetingHourDto object for the current day
			MeetingHourDto meetingHourDto = new MeetingHourDto(date, totalMeetingHours, meetingsForDay.size(),
					MeetingAverage);

			// Add the MeetingHourDto object to the list
			meetingHours.add(meetingHourDto);
		}

		return meetingHours;
	}

	private double calculateMeetingHours(Meeting meeting) {

		if (meeting.getMeetingEndDateTime() == null || meeting.getMeetingStartDateTime() == null) {
			return 0.0;
		}

		long duration = meeting.getMeetingEndDateTime().getTime() - meeting.getMeetingStartDateTime().getTime();

		double meetingHours = (double) TimeUnit.MILLISECONDS.toMinutes(duration) / 60.00;

		return meetingHours;
	}

	private Date getNextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		return calendar.getTime();
	}

	@Override
	public List<Meeting> getMeetingsByUserIdForDashboard(PaginationRequest meetingPaginationRequest) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Meeting.class);

		criteria.createAlias("employee", "e");
		criteria.add(Restrictions.eq("e.id", meetingPaginationRequest.getUser().getId()));

		if (meetingPaginationRequest.getFromDate() != null && meetingPaginationRequest.getToDate() != null) {
			Date fromDate = meetingPaginationRequest.getFromDate();
			Date toDate = meetingPaginationRequest.getToDate();

			// Include meetings for the specified date range and the current date
			criteria.add(Restrictions.or(Restrictions.between("createdAt", fromDate, toDate),
					Restrictions.between("createdAt", fromDate, getEndOfDay(toDate)),
					Restrictions.between("createdAt", getStartOfDay(fromDate), getEndOfDay(fromDate))));

			return criteria.list();

		} else {
			// Get the current date
			Date selectedDate = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(selectedDate);

			// Set the time to the start of the selected date (midnight)
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			Date startDate = calendar.getTime();

			// Set the time to the end of the selected date (just before midnight)
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			Date endDate = calendar.getTime();

			// Add an additional condition to include meetings for the current date
			criteria.add(Restrictions.or(Restrictions.between("createdAt", startDate, endDate),
					Restrictions.eq("createdAt", selectedDate)));

			return criteria.list();
		}
	}

	@Override
	public List<Meeting> getMeetingByroomFortodayDate(Integer roomId) {

		try {

			LocalDate currentDateIST = LocalDate.now(ZoneId.of("Asia/Kolkata")); // IST time zone
			LocalDate currentDateUTC = currentDateIST.atStartOfDay().toInstant(ZoneOffset.UTC)
					.atZone(ZoneId.systemDefault()).toLocalDate();

			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Meeting.class);

			// Assuming "meetingStartDateTime" is the field representing the meeting start
			// time
			Date todayStartDate = getTodayStartDate();
			Date todayEndDate = getTodayEndDate();

			// Add the criteria to filter meetings starting from today and ending today
			criteria.add(Restrictions.ge("meetingStartDateTime", todayStartDate));
			criteria.add(Restrictions.lt("meetingStartDateTime", todayEndDate));
			criteria.add(Restrictions.eq("room.id", roomId));
			criteria.add(Restrictions.between("createdAt",
					Date.from(currentDateUTC.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
					Date.from(currentDateUTC.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));

			List<Meeting> meetings = criteria.list();
			return meetings;
		} catch (Exception e) {
			return null;
		}

	}

	private Date getTodayStartDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	private Date getTodayEndDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		return calendar.getTime();
	}

	private Date getStartOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	private Date getEndOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

}
