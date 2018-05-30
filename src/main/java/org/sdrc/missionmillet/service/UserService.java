package org.sdrc.missionmillet.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.sdrc.missionmillet.model.ChangePasswordModel;
import org.sdrc.missionmillet.model.CollectUserModel;
import org.sdrc.missionmillet.model.ValueObject;
import org.sdrc.missionmillet.util.CustomErrorMessageModel;

/**
 * 
 * @author Subrata
 * 
 */

public interface UserService {

	CollectUserModel findByUsername(String name);
	
	public void createTimePeriod();
	
	void createMonthlyTimePeriod();

	void createQuarterlyTimePeriod();

	void createYearlyTimePeriod();

	CustomErrorMessageModel changePassword(ChangePasswordModel changePasswordModel);

	CustomErrorMessageModel forgotPassword(ChangePasswordModel changePasswordModel);

	List<ValueObject> getUsers();

	Long saveUserLoginMeta(String ipAddress, Integer userId, String userAgent);

	void updateLoggedOutStatus(long userLoginMetaId);

	String getIpAddr(HttpServletRequest request);

}
