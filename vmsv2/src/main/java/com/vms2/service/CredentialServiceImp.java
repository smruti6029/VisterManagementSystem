package com.vms2.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.stereotype.Service;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.OneofDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.UnknownFieldSet;
import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;
import com.mysql.cj.x.protobuf.MysqlxCrud.CollectionOrBuilder;
import com.mysql.cj.x.protobuf.MysqlxCrud.CreateViewOrBuilder;
import com.mysql.cj.x.protobuf.MysqlxCrud.Find;
import com.mysql.cj.x.protobuf.MysqlxCrud.FindOrBuilder;
import com.mysql.cj.x.protobuf.MysqlxCrud.ViewAlgorithm;
import com.mysql.cj.x.protobuf.MysqlxCrud.ViewCheckOption;
import com.mysql.cj.x.protobuf.MysqlxCrud.ViewSqlSecurity;
import com.vms2.dao.CrediantialDao;
import com.vms2.dao.UserDao;
import com.vms2.dto.UserDto;
import com.vms2.entity.CredentialMaster;
import com.vms2.entity.User;
import com.vms2.mailservice.EmailSendService;
import com.vms2.response.Response;



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
		credentialMaster.setUpdatedBY(new Date());
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
		        + "<p>Dear " + user.getName() + ",</p>"
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
			return new Response("User Succesfully Added", user, HttpStatus.OK.value());
		}
		else
		{
			return new Response("try again", userDto, HttpStatus.BAD_REQUEST.value());
		}
		
		
		
		
		
		
	}
	


}
