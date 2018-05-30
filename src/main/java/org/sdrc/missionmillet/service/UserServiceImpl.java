package org.sdrc.missionmillet.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.sdrc.missionmillet.domain.CollectUser;
import org.sdrc.missionmillet.domain.TXNChangePassword;
import org.sdrc.missionmillet.domain.TimePeriod;
import org.sdrc.missionmillet.domain.UserLoginMeta;
import org.sdrc.missionmillet.model.ChangePasswordModel;
import org.sdrc.missionmillet.model.CollectUserModel;
import org.sdrc.missionmillet.model.Mail;
import org.sdrc.missionmillet.model.ValueObject;
import org.sdrc.missionmillet.repository.CollectUserRepository;
import org.sdrc.missionmillet.repository.TXNChangePasswordRepository;
import org.sdrc.missionmillet.repository.TimePeriodRepository;
import org.sdrc.missionmillet.repository.UserLoginMetaRepository;
import org.sdrc.missionmillet.util.Constants;
import org.sdrc.missionmillet.util.CustomErrorMessageModel;
import org.sdrc.missionmillet.util.DomainToModelConverter;
import org.sdrc.missionmillet.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @author Subrata
 * 
 */

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired
	private CollectUserRepository collectUserRepository;
	
	@Autowired
	private TimePeriodRepository timePeriodRepository;
	
	@Autowired
	private MessageDigestPasswordEncoder passwordEncoder;
	
	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;
	
	@Autowired
	private TXNChangePasswordRepository txnChangePasswordRepository;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private StateManager stateManager;
	
	@Autowired
	private UserLoginMetaRepository userLoginMetaRepository;
	
	/**
	 * @author Subrata
	 * 
	 * @param username
	 * 
	 * @Description This method will take username as the parameter and 
	 * it will assign a user credentials and if not found the user it will return null.
	 * 
	 */
	@Override
	public CollectUserModel findByUsername(String username) {
		CollectUserModel collectUserModel = null;
		CollectUser collectUser = collectUserRepository.findByUsernameAndIsLiveTrue(username);
		if(collectUser!=null){
			collectUserModel = new CollectUserModel();
			collectUserModel.setUserId(collectUser.getUserId());
			collectUserModel.setName(collectUser.getName());
			collectUserModel.setUsername(collectUser.getUsername());
			collectUserModel.setPassword(collectUser.getPassword());
			collectUserModel.setEmailId(collectUser.getEmailId());
			collectUserModel.setLive(collectUser.isLive());
			collectUserModel.setTypeOfUser(collectUser.getTypeOfUser());
			collectUserModel.setNgo(collectUser.getNgo() == null ? null :collectUser.getNgo());
			collectUserModel.setParentId(collectUser.getParentId());
			collectUserModel.setUserAreaModels(DomainToModelConverter.toUserAreaMappingModel(collectUser.getUserAreaMappings()));
		}
		return collectUserModel;
	}
	
	/**
	 * @author Subrata
	 * 
	 *  @Description On first day of April and October this method will execute, 
	 *  creating time period in TimePeriod Table. (in every six month for a periodicity 6). 
	 */

	@Transactional
	@Scheduled(cron="0 0 0 1 4/6 ?")
	@Override
	public void createTimePeriod() {
		
		LocalDateTime time = LocalDateTime.now();
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		SimpleDateFormat sdf = new SimpleDateFormat("MMM");
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		TimePeriod timePeriod = new TimePeriod();
		
		Timestamp startDate = new Timestamp(cal.getTime().getTime());
		
		timePeriod.setStartDate(startDate);
		
		String sDate = sdf.format(startDate);
		cal.add(Calendar.MONTH, 5);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		Timestamp endDate = new Timestamp(cal.getTime().getTime());
		String eDate = sdf.format(endDate);
		
		timePeriod.setEndDate(endDate);
		
		timePeriod.setPeriodicity("6"); //for periodicity
		timePeriod.setTimePeriod(sDate+"-"+eDate);
		timePeriod.setYear(time.getYear()); // for year
		int preYear =0, nextYear =0;
		Calendar calYear = Calendar.getInstance();
		
		if(month > 2){
			preYear = calYear.get(Calendar.YEAR);
			calYear.add(Calendar.YEAR, 1);
			nextYear = calYear.get(Calendar.YEAR);
		} else {
			calYear.add(Calendar.YEAR, -1);
			preYear = calYear.get(Calendar.YEAR);
			calYear.add(Calendar.YEAR, 1);
			nextYear = calYear.get(Calendar.YEAR);
		}
		timePeriod.setFinancialYear(preYear+"-"+nextYear);
		timePeriodRepository.save(timePeriod);
	}
	
	/**
	 * @author Subrata
	 * 
	 *  @Description On first day of every month this method will execute, 
	 *  creating time period in TimePeriod Table. (in every month for a periodicity 1).
	 *   
	 */
	@Scheduled(cron="0 2 0 1 1/1 ?")
	@Override
	public void createMonthlyTimePeriod() {
		
		LocalDateTime time = LocalDateTime.now();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM");
		int month = cal.get(Calendar.MONTH);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		TimePeriod timePeriod = new TimePeriod();
		
		Timestamp startDate = new Timestamp(cal.getTime().getTime());
		
		timePeriod.setStartDate(startDate);
		
		String sDate = sdf.format(startDate);
		cal.add(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		Timestamp endDate = new Timestamp(cal.getTime().getTime());
		String eDate = sdf.format(endDate);
		
		timePeriod.setEndDate(endDate);
		
		timePeriod.setPeriodicity("1"); //for periodicity
		timePeriod.setTimePeriod(sDate.equals(eDate) ? sDate : sDate+"-"+eDate);
		timePeriod.setYear(time.getYear()); // for year
		int preYear =0, nextYear =0;
		if(month > 2){
			preYear = cal.get(Calendar.YEAR);
			cal.add(Calendar.YEAR, 1);
			nextYear = cal.get(Calendar.YEAR);
		} else {
			cal.add(Calendar.YEAR, -1);
			preYear = cal.get(Calendar.YEAR);
			cal.add(Calendar.YEAR, 1);
			nextYear = cal.get(Calendar.YEAR);
		}
		timePeriod.setFinancialYear(preYear+"-"+nextYear);
		timePeriodRepository.save(timePeriod);
	}
	
	/**
	 * @author Subrata
	 * 
	 *  @Description On first day of every three month this method will execute, 
	 *  creating time period in TimePeriod Table. (in every three month for a periodicity 3).
	 *   
	 */
	@Scheduled(cron="0 3 0 1 1/3 ?")
	@Override
	public void createQuarterlyTimePeriod() {
		
		LocalDateTime time = LocalDateTime.now();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM");
		int month = cal.get(Calendar.MONTH);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		TimePeriod timePeriod = new TimePeriod();
		
		Timestamp startDate = new Timestamp(cal.getTime().getTime());
		
		timePeriod.setStartDate(startDate);
		
		String sDate = sdf.format(startDate);
		cal.add(Calendar.MONTH, 2);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		Timestamp endDate = new Timestamp(cal.getTime().getTime());
		String eDate = sdf.format(endDate);
		
		timePeriod.setEndDate(endDate);
		
		timePeriod.setPeriodicity("3"); //for periodicity
		timePeriod.setTimePeriod(sDate.equals(eDate)? sDate : sDate+"-"+eDate);
		timePeriod.setYear(time.getYear()); // for year
		int preYear =0, nextYear =0;
		if(month > 2){
			preYear = cal.get(Calendar.YEAR);
			cal.add(Calendar.YEAR, 1);
			nextYear = cal.get(Calendar.YEAR);
		} else {
			cal.add(Calendar.YEAR, -1);
			preYear = cal.get(Calendar.YEAR);
			cal.add(Calendar.YEAR, 1);
			nextYear = cal.get(Calendar.YEAR);
		}
		timePeriod.setFinancialYear(preYear+"-"+nextYear);
		timePeriodRepository.save(timePeriod);
	}
	
	/**
	 * @author Subrata
	 * 
	 *  @Description On first day of every 12 month this method will execute, 
	 *  creating time period in TimePeriod Table. (in every 12 month for a periodicity 12).
	 *   
	 */
	@Scheduled(cron="0 4 0 1 4/12 ?")
	@Override
	public void createYearlyTimePeriod() {
		
		LocalDateTime time = LocalDateTime.now();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM");
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		TimePeriod timePeriod = new TimePeriod();
		
		Timestamp startDate = new Timestamp(cal.getTime().getTime());
		
		timePeriod.setStartDate(startDate);
		
		String sDate = sdf.format(startDate);
		cal.add(Calendar.MONTH, 11);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		Timestamp endDate = new Timestamp(cal.getTime().getTime());
		String eDate = sdf.format(endDate);
		
		timePeriod.setEndDate(endDate);
		
		timePeriod.setPeriodicity("12"); //for periodicity
		timePeriod.setTimePeriod(sDate.equals(eDate) ? sDate : sDate+"-"+eDate);
		timePeriod.setYear(time.getYear()); // for year
		int preYear =0, nextYear =0;
		cal.add(Calendar.YEAR, -1);
		preYear = cal.get(Calendar.YEAR);
		cal.add(Calendar.YEAR, 1);
		nextYear = cal.get(Calendar.YEAR);
		timePeriod.setFinancialYear(preYear+"-"+nextYear);
		timePeriodRepository.save(timePeriod);
	}
	
	/**
	 * @author Subrata
	 * 
	 * @param user
	 * @param collectUser
	 * 
	 * @Description saving user credentials while changing a password.
	 */

	@Transactional
	@Override
	public CustomErrorMessageModel changePassword(ChangePasswordModel changePasswordModel) {
		CollectUser collectUser = collectUserRepository.findByUsernameAndIsLiveTrue(changePasswordModel.getUserName());
		
		if(changePasswordModel.getNewPassword().equals(changePasswordModel.getConfirmPassword())){
			if(collectUser != null){
				if(changePasswordModel.getOldPassword() == null){
					collectUser.setPassword(passwordEncoder.encodePassword(collectUser.getUsername(), changePasswordModel.getNewPassword()));
					CollectUser user = collectUserRepository.save(collectUser); 
					
					CollectUserModel collectUserModel = ((CollectUserModel)stateManager.getValue(Constants.USER_PRINCIPAL));
					saveTXNChangePassword(new CollectUser(collectUserModel.getUserId()), collectUserModel.getUsername(),user); 
					
					return getCustomErrorMessages(messageSourceNotification.getMessage("password.updated", null, null), 
							HttpServletResponse.SC_OK);
				}else{
					if(collectUser.getPassword().equals(passwordEncoder.encodePassword(collectUser.getUsername(), 
							changePasswordModel.getOldPassword()))){
						collectUser.setPassword(passwordEncoder.encodePassword(collectUser.getUsername(),
								changePasswordModel.getNewPassword()));
						CollectUser user = collectUserRepository.save(collectUser); 
						
						saveTXNChangePassword(new CollectUser(user.getUserId()), user.getUsername(), collectUser); 
						
						return getCustomErrorMessages(messageSourceNotification.getMessage("password.updated", null, null), 
								HttpServletResponse.SC_OK);
					}else{
						return getCustomErrorMessages(messageSourceNotification.getMessage("password.not.matching", null, null), 
								HttpServletResponse.SC_FORBIDDEN);
					}
				}
			}else{
				return getCustomErrorMessages(messageSourceNotification.getMessage("user.not.found", null, null), 
						HttpServletResponse.SC_FORBIDDEN);
			}
		}else{
			return getCustomErrorMessages(messageSourceNotification.getMessage("new.password.confirm.password.not.matching", null, null), 
					HttpServletResponse.SC_FORBIDDEN);
		}
	}
	
	private CustomErrorMessageModel getCustomErrorMessages(String message,	int ststusCode) {
		CustomErrorMessageModel customErrorMessageModel = new CustomErrorMessageModel();
		customErrorMessageModel.setMessage(message);
		customErrorMessageModel.setStatusCode(ststusCode);
		return customErrorMessageModel;
	}
	

	/**
	 * @author Subrata
	 * 
	 * @param user
	 * @param collectUser
	 * 
	 * @Description When a user changes his/her password or a user changes some other users password, 
	 * then we are saving user details in the database.
	 */
	private void saveTXNChangePassword(CollectUser collectUser, String createdBy, CollectUser user) {
		
		TXNChangePassword txnChangePassword = new TXNChangePassword();
		txnChangePassword.setUser(collectUser); 
		txnChangePassword.setCreatedDate(new Timestamp(new Date().getTime()));
		txnChangePassword.setCreatedBy(createdBy);
		txnChangePassword.setLive(true);
		txnChangePassword.setPasswordUpdatedDate(new Timestamp(new Date().getTime()));
		txnChangePassword.setCollectUser(user); 
		
		ServletRequestAttributes attr=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request=attr.getRequest();
		String ipAddress=getIpAddr(request);
		
		txnChangePassword.setUserIp(ipAddress);	
		txnChangePasswordRepository.save(txnChangePassword);
		
	}

	/**
	 * 
	 * @author Subrata
	 * 
	 * @param HttpServletRequest
	 * 
	 * @Description returns the IP Address
	 */
	@Override
	public String getIpAddr(HttpServletRequest request) {      
		   String ip = request.getHeader("x-forwarded-for");      
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))       
		       ip = request.getHeader("Proxy-Client-IP");      
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))       
		       ip = request.getHeader("WL-Proxy-Client-IP");      
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))       
		       ip = request.getRemoteAddr();      
		   return ip;      
	}
	
	/**
	 * 
	 * @author Subrata
	 * 
	 * @param email
	 * @param userId
	 * 
	 * @Description In case of a user(only Admin/District web users) forgot his/her password, 
	 * then we are sending a system generated password to his/her email. 
	 * 
	 */
	
	@Override
	public CustomErrorMessageModel forgotPassword(ChangePasswordModel changePasswordModel) {
		
		CollectUser collectUser=collectUserRepository.findByUsernameAndEmailId(changePasswordModel.getEmail(), 
				changePasswordModel.getUserName());
		try {
			if(collectUser != null && collectUser.isLive()) {
				String newPassword=RandomStringUtils.randomAlphanumeric(8);
				collectUser.setPassword(passwordEncoder.encodePassword(collectUser.getUsername(),newPassword));
				collectUserRepository.save(collectUser); 
				
				List<String> emailId=new ArrayList<String>();
				emailId.add(collectUser.getEmailId());
				
				Mail mail=new Mail();
				mail.setToEmailIds(emailId);
				mail.setToUserName(collectUser.getName());
				mail.setSubject("Forgot Password");
				mail.setMessage(messageSourceNotification.getMessage("user.forgot.password", null,null)+newPassword);
				mail.setFromUserName("Administrator");
				mail.setRegards("Regards,");
				mailService.sendMail(mail);
				
				saveTXNChangePassword(collectUser, collectUser.getUsername(), collectUser); 
				
				return getCustomErrorMessages(messageSourceNotification.getMessage("password.reset.success", null, null), HttpServletResponse.SC_OK);
			}
			return getCustomErrorMessages(messageSourceNotification.getMessage("password.reset.failure", null, null), HttpServletResponse.SC_NOT_FOUND);
		} catch (Exception e) {
			return getCustomErrorMessages(messageSourceNotification.getMessage("password.reset.failure", null, null), HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	/**
	 * @author Subrata
	 * @Description Fetching child-users according to the user login.
	 */
	@Override
	public List<ValueObject> getUsers(){
		CollectUserModel collectUserModel = ((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL));
		
		List<CollectUser> collectUser=collectUserRepository.getUsersList(collectUserModel.getUserId());
		List<ValueObject> users = new ArrayList<ValueObject>();
		if(collectUser != null){
			for (CollectUser user : collectUser) {
				users.add(addUsers(user.getUserId().toString(), user.getUsername(), null));
			}
			users.add(addUsers(collectUserModel.getUserId().toString(), collectUserModel.getUsername(), collectUserModel.getParentId().toString()));
			
			return users;
		}else{
			return null;
		}
	}

	private ValueObject addUsers(String userId, String userName, String parentId) {
		ValueObject valueObject = new ValueObject();
		valueObject.setKey(userId);
		valueObject.setDescription(userName);
		valueObject.setGroupName(parentId);
		
		return valueObject;
	}
	
	/**
	 * 
	 * @author Subrata
	 * 
	 * @param ipAddress
	 * @param userId
	 * @param userAgent
	 * @Description Saving user login Meta data
	 */
	
	@Override
	public Long saveUserLoginMeta(String ipAddress, Integer userId,	String userAgent) {
		UserLoginMeta userLoginMeta = new UserLoginMeta();
		userLoginMeta.setUserIpAddress(ipAddress);
		userLoginMeta.setCollectUser(new CollectUser(userId));
		userLoginMeta.setLoggedInDateTime(new Timestamp(new Date().getTime()));
		userLoginMeta.setUserAgent(userAgent);
		userLoginMeta.setLoggedIn(true);
		return userLoginMetaRepository.save(userLoginMeta).getUserLogInMetaId();
	}
	
	/**
	 * @author Subrata
	 * 
	 * @param userLoginMetaId
	 * @Description updating user login meta data
	 * 
	 */
	@Override
	@Transactional
	public void updateLoggedOutStatus(long userLoginMetaId) {

		// while server start up parameter is -1
		if (userLoginMetaId == -1) {
			userLoginMetaRepository.updateStatusForAll(new Timestamp(new Date().getTime()));
		} else //while user clicks on log out or while session destroys
			userLoginMetaRepository.updateStatus(new Timestamp(new Date().getTime()), userLoginMetaId);
	}

}
