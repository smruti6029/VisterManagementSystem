package com.vms2.mailservice;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailSendService {

	@Autowired
	private JavaMailSender mailSender;
	


	public void sendEmail(String toEmail,String subject,String emailContent) throws Exception {
					

		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

			messageHelper.setFrom("smruti97761@gmail.com");
			messageHelper.setTo(toEmail);
			messageHelper.setSubject(subject);
			messageHelper.setText(emailContent, true);	

			((JavaMailSenderImpl) mailSender).send(message);

			System.out.println("Mail sent successfully");
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println("Failed to send email: " + e.getMessage());
		}
	}

}