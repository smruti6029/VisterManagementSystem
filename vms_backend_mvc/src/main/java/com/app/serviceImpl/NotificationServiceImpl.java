package com.app.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.app.Dao.NotificationDao;
import com.app.Dao.UserDao;
import com.app.dto.MeetingDto;
import com.app.dto.NotificationDTO;
import com.app.entity.Notification;
import com.app.entity.User;
import com.app.response.Response;
import com.app.security.JwtHelper;
import com.app.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private JwtHelper jwtHelper;

	@Autowired
	private UserDao userDao;

	@Override
	public Response<?> save(NotificationDTO notificationDTO) throws Exception {

		notificationDTO.setCreatedAt(new Date());
		notificationDTO.setUpdatedAt(new Date());
		notificationDTO.setSeen(false);
		notificationDTO.setStatus(1); // 1 for PENDING
		Notification notification = NotificationDTO.toNotification(notificationDTO);

		Notification savedNotification = notificationDao.save(notification);

		return new Response<>("Notification Saved", NotificationDTO.toNotificationDTO(savedNotification),
				HttpStatus.OK.value());
	}

	@Override
	public Response<?> getAll() {
		List<Notification> list = this.notificationDao.getAll();
		return new Response<>("success", list, HttpStatus.OK.value());
	}

	@Override
	public Response<?> getByMeetingId(MeetingDto meetingDto) {
		try {
			Notification notification = this.notificationDao.getByMeetingId(meetingDto.getId());
			if (notification != null) {
				return new Response<>("success", NotificationDTO.toNotificationDTO(notification),
						HttpStatus.OK.value());
			}
			return new Response<>("No notification found", null, HttpStatus.NOT_FOUND.value());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

//	@Override
//	public Response<?> getByUser(HttpServletRequest request, Integer companyId) {
//		String requestTokenHeader = request.getHeader("Authorization");
//		String phone = jwtHelper.getUsernameFromToken(requestTokenHeader.substring(7));
//		User user = this.userDao.getUserbyPhone(phone);
//		if (user != null) {
//			List<Notification> list = new ArrayList<>();
//			if (user.getRole().getName().equals("RECEPTIONIST")) {
//				list = this.notificationDao.getNotificationsByUser(null, companyId);
//			} else {
//				list = this.notificationDao.getNotificationsByUser(user, companyId);
////				for (Notification not : list) {
////					System.out.println(not.getText() + " 1");
////				}
//			}
//			List<NotificationDTO> notificationDtoList = list.stream().map(t -> {
//				try {
//					return NotificationDTO.toNotificationDTO(t);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					return null;
//				}
//			}).collect(Collectors.toList());
//			return new Response<>("Success", notificationDtoList, HttpStatus.OK.value());
//		}
//		return new Response<>("User not found", null, HttpStatus.NOT_FOUND.value());
//	}

	@Override
	public Response<?> getByUser(HttpServletRequest request, Integer companyId, Integer buildingId) {
		String requestTokenHeader = request.getHeader("Authorization");
		String phone = jwtHelper.getUsernameFromToken(requestTokenHeader.substring(7));
		User user = this.userDao.getUserbyPhone(phone);

		if (user != null) {
			List<Notification> list;

			if (user.getRole().getName().equals("RECEPTIONIST")) {
				if (companyId == null && buildingId == null) {
					return new Response<>("Kindly provide either building ID or CompanyID", null,
							HttpStatus.BAD_REQUEST.value());
				}
				list = this.notificationDao.getNotificationsByUser(null, companyId, buildingId);
			} else {
				list = this.notificationDao.getNotificationsByUser(user, companyId, null);
			}

			if (list != null) {
				List<NotificationDTO> notificationDtoList = list.stream().map(t -> {
					try {
						return NotificationDTO.toNotificationDTO(t);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}).collect(Collectors.toList());

				return new Response<>("Success", notificationDtoList, HttpStatus.OK.value());
			} else {
				// Handle the case where the list is null
				return new Response<>("Notification list is null", null, HttpStatus.OK.value());
			}
		}

		return new Response<>("User not found", null, HttpStatus.NOT_FOUND.value());
	}

	@Override
	public Response<?> updateNotification(NotificationDTO notificationDTO) {
		// TODO Auto-generated method stub
		try {
			notificationDTO.setUpdatedAt(new Date());
			Notification notification = this.notificationDao.update(NotificationDTO.toNotification(notificationDTO));
			return new Response<>("Success", NotificationDTO.toNotificationDTO(notification), HttpStatus.OK.value());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Response<?> markAsSeen(HttpServletRequest request, Integer companyId, Integer buildingId) throws Exception {
		String requestTokenHeader = request.getHeader("Authorization");
		String phone = jwtHelper.getUsernameFromToken(requestTokenHeader.substring(7));
		User user = this.userDao.getUserbyPhone(phone);
		try {
			if (user != null) {
				if (user.getRole().getName().equals("RECEPTIONIST")) {
					List<Notification> list = this.notificationDao.getNotificationsOfReceptionist(companyId,
							buildingId);
					if (!list.isEmpty()) {
						for (Notification notification : list) {
							if (!notification.getSeen()) {
								notification.setSeen(true);
//								notification.setUpdatedAt(new Date());
								this.notificationDao.update(notification);
							}
						}
					}
				} else {
					List<Notification> list = this.notificationDao.getNotificationsByUser(user,
							user.getCompany().getId(), null);
					if (!list.isEmpty()) {
						for (Notification notification : list) {
							if (!notification.getSeen()) {
								notification.setSeen(true);
//								notification.setUpdatedAt(new Date());
								this.notificationDao.update(notification);
							}
						}
					}
				}
			}
			return new Response<>("success", null, HttpStatus.OK.value());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Something went wrong", null, HttpStatus.BAD_REQUEST.value());
		}
	}

}
