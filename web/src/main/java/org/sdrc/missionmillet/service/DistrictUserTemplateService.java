package org.sdrc.missionmillet.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
/**
 * @author Maninee Mahapatra(maninee@sdrc.co.in) This controller for district
 *         User date(08-12-2017)
 * 
 *
 */

public interface DistrictUserTemplateService {
	
	/**
	 * @param file
	 * @param request
	 * @param ngoId
	 * @param timePeriodId 
	 * @return String filepath
	 */
	public String uploadTemplate(MultipartFile file, HttpServletRequest request,Integer ngoId, Integer timePeriodId);

	}
