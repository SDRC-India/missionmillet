package org.sdrc.missionmillet.model;

import java.util.List;
import java.util.Map;

public class Mail {
	
	private String fromUserName;
	private String toUserName;
	private List<String> toEmailIds;
	private List<String> ccEmailIds;
	private String subject;	
	private String message;
	private Map<String, String> attachments;
	private String regards;
	
	
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public List<String> getToEmailIds() {
		return toEmailIds;
	}
	public void setToEmailIds(List<String> toEmailIds) {
		this.toEmailIds = toEmailIds;
	}
	public List<String> getCcEmailIds() {
		return ccEmailIds;
	}
	public void setCcEmailIds(List<String> ccEmailIds) {
		this.ccEmailIds = ccEmailIds;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, String> getAttachments() {
		return attachments;
	}
	public void setAttachments(Map<String, String> attachments) {
		this.attachments = attachments;
	}
	public String getRegards() {
		return regards;
	}
	public void setRegards(String regards) {
		this.regards = regards;
	}
		
}
