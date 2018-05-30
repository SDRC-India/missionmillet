package org.sdrc.missionmillet.service;

import javax.servlet.http.HttpServletRequest;

import org.sdrc.missionmillet.model.CollectUserModel;

public interface SubmissionService {

	/**
	 * 
	 * This following method will do all the upload work 
	 * @return an in t value, if it is zero then the upload is successful
	 * @param request, the request object from client request
	 * @param deviceID, the client mobile IMEI number
	 * @param username, the clients username
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 01-Jun-2017 4:59:15 pm
	 */
	int uploadForm(HttpServletRequest request, String deviceID, CollectUserModel collectUserModel);
	
}
