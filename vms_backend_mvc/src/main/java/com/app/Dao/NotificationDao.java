package com.app.Dao;

import java.util.List;

import com.app.entity.Meeting;
import com.app.entity.Notification;
import com.app.entity.User;

public interface NotificationDao {

	public Notification save(Notification notification);

	public List<Notification> getAll();

	public Notification getById(Integer id);

	public Notification getByMeetingId(Integer id);

//	public List<Notification> getNotificationsByUser(User user, Integer companyId);
	
	public List<Notification> getNotificationsByUser(User user, Integer companyId, Integer buildingId);

	public Notification update(Notification notification);

	List<Notification> getNotificationsByMeeting(Meeting meeting);

	List<Notification> getNotificationsOfReceptionist(Integer companyId, Integer buildingId);

	public void deleteNotificationsOlderThanDays(Integer schedulerValue);
}
