package com.example.commonservice.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.commonservice.model.NotificationRequest;
import com.example.commonservice.model.NotificationResponse;
import com.example.commonservice.service.NotificationService;

@RestController
@RequestMapping("/api/send")
public class NotificationController {
	
	private static final Logger LOGGER = LogManager.getLogger(NotificationController.class);
	
	@Autowired
	NotificationService service;
	
	@PostMapping("/notification")
	public ResponseEntity<?> sendNotification(@RequestBody NotificationRequest request){
		LOGGER.info("request : "+request.toString());
		NotificationResponse response = service.sendNotification(request);
		return ResponseEntity.ok(response);
	}
	
}
