package com.example.commonservice.model;

import java.util.Arrays;

public class NotificationRequest {

	private NotificationType type;
	private String toMobile;
	private String subject;
	private String to;
	private String[] cc;
	private String[] bcc;
	private String body;
	private String fileName;

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

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String[] getCc() {
		return cc;
	}

	public void setCc(String[] cc) {
		this.cc = cc;
	}

	public String[] getBcc() {
		return bcc;
	}

	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "NotificationRequest [type=" + type + ", toMobile=" + toMobile + ", subject=" + subject + ", to=" + to
				+ ", cc=" + Arrays.toString(cc) + ", bcc=" + Arrays.toString(bcc) + ", body=" + body + ", fileName="
				+ fileName + "]";
	}

}
