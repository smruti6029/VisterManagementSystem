package com.app.dto;

import java.util.Date;

import com.app.entity.Notification;

public class NotificationDTO {

	private Integer id;

	private MeetingDto meeting;

	private Integer status;

	private String text;

	private Boolean seen;

	private String userRole;

	private Date createdAt;

	private Date updatedAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MeetingDto getMeeting() {
		return meeting;
	}

	public void setMeeting(MeetingDto meeting) {
		this.meeting = meeting;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public Boolean getSeen() {
		return seen;
	}

	public void setSeen(Boolean seen) {
		this.seen = seen;
	}

	public NotificationDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static NotificationDTO toNotificationDTO(Notification notification) throws Exception {
		NotificationDTO notificationDTO = new NotificationDTO();
		if (notification == null) {
			return null;
		}
		notificationDTO.setId(notification.getId());
		notificationDTO.setMeeting(MeetingDto.convertToDTO(notification.getMeeting()));
		notificationDTO.setText(notification.getText());
		notificationDTO.setStatus(notification.getStatus());
		notificationDTO.setSeen(notification.getSeen());
		notificationDTO.setUserRole(notification.getUserRole());
		notificationDTO.setCreatedAt(notification.getCreatedAt());
		notificationDTO.setUpdatedAt(notification.getUpdatedAt());
		return notificationDTO;
	}

	public static Notification toNotification(NotificationDTO notificationDTO) {
		Notification notification = new Notification();
		if (notificationDTO == null) {
			return null;
		}
		notification.setId(notificationDTO.getId());
		notification.setMeeting(MeetingDto.convertToEntity(notificationDTO.getMeeting()));
		notification.setText(notificationDTO.getText());
		notification.setStatus(notificationDTO.getStatus());
		notification.setSeen(notificationDTO.getSeen());
		notification.setUserRole(notificationDTO.getUserRole());
		notification.setCreatedAt(notificationDTO.getCreatedAt());
		notification.setUpdatedAt(notificationDTO.getUpdatedAt());
		return notification;
	}
}
