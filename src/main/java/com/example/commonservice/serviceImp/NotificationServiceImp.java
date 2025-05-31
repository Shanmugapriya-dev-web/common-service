package com.example.commonservice.serviceImp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.commonservice.config.UltraMsgConfig;
import com.example.commonservice.model.NotificationRequest;
import com.example.commonservice.model.NotificationResponse;
import com.example.commonservice.model.NotificationType;
import com.example.commonservice.model.Status;
import com.example.commonservice.service.NotificationService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.activation.URLDataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class NotificationServiceImp implements NotificationService {

	private static final Logger LOGGER = LogManager.getLogger(NotificationServiceImp.class);

	@Value("${twilio.phone.number}")
	private String fromNumber;

	@Value("${document.file.path}")
	private String filePath;

	@Value("${spring.mail.username}")
	private String fromEmail;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UltraMsgConfig ultraMsgConfig;

	@Override
	public NotificationResponse sendNotification(NotificationRequest request) {
		NotificationResponse response = new NotificationResponse();

		if (request.getType() == NotificationType.SMS) {

			Message message = Message
					.creator(new PhoneNumber(request.getToMobile()), new PhoneNumber(fromNumber), request.getBody())
					.create();

			LOGGER.info(message.getErrorCode() + ", " + message.getErrorMessage());

			if (message.getErrorMessage() == null) {
				response.setType(NotificationType.SMS);
				response.setStatus(Status.SUCCESS);
				response.setToMobile(message.getTo());
				response.setSendOn(message.getDateUpdated().toLocalDateTime());
			}

		} else if (request.getType() == NotificationType.EMAIL) {

			try {
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true);

				helper.setFrom(fromEmail);
				helper.setTo(request.getTo());
				helper.setSubject(request.getSubject());
				helper.setText(request.getBody(), true);

				if (request.getCc() != null && request.getCc().length > 0) {
					helper.setCc(request.getCc());
				}

				if (request.getBcc() != null && request.getBcc().length > 0) {
					helper.setBcc(request.getBcc());
				}
				try {
					URL url = new URL("http://localhost:8080/templates/brand_data_upload_template.xlsx");
					InputStream inputStream = url.openStream();

					File tempFile = File.createTempFile("brand_data_upload_template", ".xlsx");
					Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					if (tempFile.exists()) {
						helper.addAttachment(request.getFileName(), new FileSystemResource(tempFile));
					} else {
						System.out.println("Attachment file not found!");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mailSender.send(message);
				response.setType(NotificationType.EMAIL);
				response.setStatus(Status.SUCCESS);
				response.setTo(request.getTo());
				response.setSendOn(LocalDateTime.now());

			} catch (MessagingException e) {
				e.printStackTrace(); // or log properly
				response.setStatus(Status.FAILED);
			}

		} else if (request.getType() == NotificationType.WHATSAPP) {

			String url = "https://api.ultramsg.com/" + ultraMsgConfig.getInstanceId() + "/messages/chat";

			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("token", ultraMsgConfig.getToken());
			body.add("to", request.getToMobile());
			body.add("body", request.getBody());

			org.springframework.http.HttpEntity<MultiValueMap<String, String>> req = new org.springframework.http.HttpEntity<>(
					body, headers);

			ResponseEntity<String> resp = restTemplate.postForEntity(url, req, String.class);
			LOGGER.info(resp.getBody());
			JSONObject respObj = new JSONObject(resp.getBody());

			response = new NotificationResponse();
			if (respObj.getString("sent") == "true") {
				response.setType(NotificationType.WHATSAPP);
				response.setStatus(Status.SUCCESS);
				response.setToMobile(request.getToMobile());
				response.setSendOn(LocalDateTime.now());
			}
		}
		return response;
	}

}
