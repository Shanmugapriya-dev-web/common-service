package com.example.commonservice.model;

import java.time.LocalDateTime;

public class NotificationResponse {
	
	private NotificationType type;
	private String toMobile;
	private String to;
	private Status status;
	private LocalDateTime sendOn;

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public String getToMobile() {
		return toMobile;
	}

	public void setToMobile(String toMobile) {
		this.toMobile = toMobile;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDateTime getSendOn() {
		return sendOn;
	}

	public void setSendOn(LocalDateTime sendOn) {
		this.sendOn = sendOn;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return "NotificationResponse [type=" + type + ", toMobile=" + toMobile + ", to=" + to + ", status=" + status
				+ ", sendOn=" + sendOn + "]";
	}

}
