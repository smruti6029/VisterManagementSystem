package com.vms2.serviceImp;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vms2.dao.CrediantialDao;
import com.vms2.dao.UserDao;
import com.vms2.dto.UserDto;
import com.vms2.entity.CredentialMaster;
import com.vms2.entity.User;
import com.vms2.mailservice.EmailSendService;
import com.vms2.response.Response;
import com.vms2.service.CredentialService;



@Service
public class CredentialServiceImp implements CredentialService
{
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CrediantialDao crediantialDao;
	
	@Autowired
	private EmailSendService emailSendService;
	
	final public String password="1234";

	@Override
	public Response<?> adduserInCrediantial(UserDto userDto) 
	{
		User user = userDao.getUserbyPhone(userDto.getPhone());
		
		CredentialMaster credentialMaster =new CredentialMaster();
		
		credentialMaster.setUser(user);
		credentialMaster.setUsername(user.getPhone());
		credentialMaster.setupdatedOn(new Date());
		credentialMaster.setIsActive(true);
		credentialMaster.setCreatedOn(new Date());
		credentialMaster.setCreated_by("ADMIN");
		credentialMaster.setPassword(credentialMaster.passwordEncoder(password));
		
		Integer save = crediantialDao.saveCrediantial(credentialMaster);
		
		if(save>0)
		{String toEmail = user.getEmail();
		String subject = "Registration Successfully";

		// Create the HTML email content with placeholders
		user.getCompany().getName();
		String emailContent = "<html><body>"
		        + "<h2>" + user.getCompany().getName() + "</h2>"
		        + "<p>Dear " + user.getFirstname() +"  "+user.getLastname()  + ",</p>"
		        + "<p>Your registration was successful. Here are your credential:</p>"
		        + "<ul>"
		        + "<li><strong>Username:</strong> " + credentialMaster.getUsername() + "</li>"
		        + "<li><strong>Password:</strong> " + password + "</li>"
		        + "</ul>"
		        + "<p>Thank you for joining us!</p>"
		        + "</body></html>";

			
		try
		{
			emailSendService.sendEmail(toEmail, subject, emailContent);
				
		}catch (Exception e) {
			
		}
			return new Response<>("User Succesfully Added", user, HttpStatus.OK.value());
		}
		else
		{
			return new Response<>("try again", userDto, HttpStatus.BAD_REQUEST.value());
		}
		
		
		
		
		
		
	}
	


}
