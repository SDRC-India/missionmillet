package org.sdrc.missionmillet.service;

import org.sdrc.missionmillet.model.Mail;


public interface MailService {
	
	String sendMail(Mail mail) throws Exception;
	
}
