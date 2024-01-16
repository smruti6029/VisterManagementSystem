package com.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.app.Dao.ConfigurationDao;
import com.app.Dao.CrediantialDao;
import com.app.Dao.NotificationDao;
import com.app.Dao.RoomDao;
import com.app.Dao.UserDao;
import com.app.entity.Configuration;
import com.app.service.MeetingService;

@Component
@EnableScheduling
public class ScheduledTaskConfig {

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private CrediantialDao crediantialDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private MeetingService meetingService;

	@Autowired
	
	private NotificationDao notificationDao;

	@Autowired
	private ConfigurationDao configurationDao;

	@Scheduled(cron = "0 30 16 * * *", zone = "UTC")
	public void scheduleTask() {

		System.out.println("IN Side scheduler");

		roomDao.availableAllroom();

	}

	@Scheduled(cron = "0 30 16 * * *", zone = "UTC")
	public void clearOtp() {
		System.out.println("Inside OTP scheduler");
		crediantialDao.clearOtp();
	}

	@Scheduled(cron = "0 30 16 * * *", zone = "UTC")
	public void absentAllUser() {
		System.out.println("User  absent all ");
		userDao.absentAll();
	}
	
	
	
	@Scheduled(cron = "0 30 2 * * *", zone = "UTC")
	public void presentAlluser() {
		System.out.println("User Present All ");
		userDao.presentAll();
	}

	@Scheduled(cron = "0 30 16  * * *", zone = "UTC")
	public void automaticallyCancelledMeetingAndExcelDelete() {
		
		
		try {
			Configuration configuration = configurationDao.getByKey("NO_OF_DAYS_TO_DELETE_MEETING");
			if (configuration != null) {
				Integer noOfDays = Integer.parseInt(configuration.getValue());
				System.out.println("Pending meeting automatically Cancelled ");
				meetingService.automaticallyCancelledMeeting(noOfDays);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Scheduled(cron = "0 30 18 * * *", zone = "UTC")
	public void automaticallyDeleteOlderNotifications() {
		try {
			Configuration configuration = configurationDao.getByKey("NO_OF_DAYS_TO_DELETE_NOTIFICATIONS");
			if (configuration != null) {
				Integer schedulerValue = Integer.parseInt(configuration.getValue());
				System.out.println("Notifications that are older than, " + schedulerValue + " days are deleted.");
				notificationDao.deleteNotificationsOlderThanDays(schedulerValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}