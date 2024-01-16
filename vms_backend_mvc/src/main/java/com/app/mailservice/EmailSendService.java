package com.app.mailservice;





import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSendService {

	@Autowired
	private JavaMailSender mailSender;
	
	
	public void sendEmail(String toEmail,String subject,String emailContent) throws Exception {
					
		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

			messageHelper.setFrom("vms.nyggs@gmail.com");
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
	
	
	public void sendEmail(String toEmail, String subject, String emailContent, byte[] generateVisitorPassPDF, String attachmentFileName)
	        throws Exception {
	    MimeMessage message = mailSender.createMimeMessage();

	    try {
	        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

	        messageHelper.setFrom("vms.nyggs@gmail.com");
	        messageHelper.setTo(toEmail);
	        messageHelper.setSubject(subject);

	        // Create and add the text part
	        MimeBodyPart textPart = new MimeBodyPart();
	        textPart.setText(emailContent, "utf-8", "html");

	        // Create and add the attachment part
	        MimeBodyPart attachmentPart = new MimeBodyPart();
	        DataSource source = new ByteArrayDataSource(generateVisitorPassPDF, "application/pdf");
	        attachmentPart.setDataHandler(new DataHandler(source));
	        attachmentPart.setFileName(attachmentFileName);

	        // Create the Multipart
	        Multipart multipart = new MimeMultipart();
	        multipart.addBodyPart(textPart);
	        multipart.addBodyPart(attachmentPart);

	        // Set the content of the message to the multipart
	        message.setContent(multipart);

	        ((JavaMailSenderImpl) mailSender).send(message);

	        System.out.println("Mail sent successfully");
	    } catch (MessagingException e) {
	        e.printStackTrace();
	        System.out.println("Failed to send email: " + e.getMessage());
	    }
	}


	
	
	

}