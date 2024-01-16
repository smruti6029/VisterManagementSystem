package com.app.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.entity.Visitor;

@Service
public class SmsService {

	@Value("${sms.api}")
	public String api;

	@Value("${appKey}")
	public String apiKey;

	public void sendSmstoUser(String phoneNo, Visitor visitor) {
		String message = "RAPIDSOFT: " + visitor.getName() + " has come to meet you and is in reception.";

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("message", message);
			requestBody.put("messageType", "VISITOR_NOTIFICATION");
			requestBody.put("appKey", apiKey);
			requestBody.put("phoneNumber", phoneNo);

			HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
			RestTemplate restTemplate = new RestTemplate();

			ResponseEntity<String> response = restTemplate.postForEntity(
					"http://192.168.200.14:8085/messagingService/messaging-controller/v1/send-message", request,
					String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				System.out.println("SMS sent successfully");
			} else {
				System.out.println("Failed to send SMS");
			}
		} catch (Exception e) {
			System.out.println("Some issue for Sending Sms");
		}
	}
}
