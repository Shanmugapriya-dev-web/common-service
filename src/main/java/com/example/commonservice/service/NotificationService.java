package com.example.commonservice.service;


import com.example.commonservice.model.NotificationRequest;
import com.example.commonservice.model.NotificationResponse;

public interface NotificationService {
	
	NotificationResponse sendNotification(NotificationRequest request);
}
