package com.app.serviceImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.app.Dao.CrediantialDao;
import com.app.Dao.UserDao;
import com.app.dto.UserDto;
import com.app.entity.CredentialMaster;
import com.app.entity.User;
import com.app.mailservice.EmailSendService;
import com.app.response.Response;
import com.app.service.CredentialService;

@Service
public class CredentialServiceImp implements CredentialService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private CrediantialDao crediantialDao;

	@Autowired
	private EmailSendService emailSendService;

	final public String password = "1234";

	@Override
	public Response<?> adduserInCrediantial(UserDto userDto) {
		User user = userDao.getUserbyPhone(userDto.getPhone());

		CredentialMaster credentialMaster = new CredentialMaster();

		credentialMaster.setUser(user);
		credentialMaster.setUsername(user.getPhone());
		credentialMaster.setupdatedOn(new Date());
		credentialMaster.setIsActive(true);
		credentialMaster.setCreatedOn(new Date());
		credentialMaster.setCreated_by("ADMIN");
		credentialMaster.setPassword(credentialMaster.passwordEncoder(password));

		Integer save = crediantialDao.saveCrediantial(credentialMaster);

		if (save > 0) {
			String toEmail = user.getEmail();
			String subject = "Registration Successfully";

			// Create the HTML email content with placeholders
			user.getCompany().getName();
			String emailContent = "<html><body>" + "<h2>" + user.getCompany().getName() + "</h2>" + "<p>Dear "
					+ user.getFirstname() + "  " + user.getLastname() + ",</p>"
					+ "<p>Your registration was successful. Here are your credential:</p>" + "<ul>"
					+ "<li><strong>Username:</strong> " + credentialMaster.getUsername() + "</li>"
					+ "<li><strong>Password:</strong> " + password + "</li>" + "</ul>"
					+ "<p>Thank you for joining us!</p>" + "</body></html>";

			Thread emailThread = new Thread(() -> {

				try {
					emailSendService.sendEmail(toEmail, subject, emailContent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

			emailThread.start();
			return new Response<>("User Succesfully Added", userDto, HttpStatus.OK.value());
		} else {
			return new Response<>("try again", userDto, HttpStatus.BAD_REQUEST.value());
		}

	}

}
