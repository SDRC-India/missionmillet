package org.sdrc.missionmillet.core;

import org.sdrc.missionmillet.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * @author Subham Ashish (subham@sdrc.co.in)
 *
 */
public class CustomMaintenanceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;

	private String errCode = Constants.WEB_MAINTENANCE;
	private String errMsg = "site is under Maintenance!";

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	//constructor
	public CustomMaintenanceException(String errCode, String errMsg) {

		super(errMsg);
		
		this.errCode = errCode;
		this.errMsg = errMsg;

	}

}
