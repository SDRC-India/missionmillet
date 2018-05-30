package org.sdrc.missionmillet.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.sdrc.missionmillet.domain.CollectUser;
import org.sdrc.missionmillet.domain.Configuration;
import org.sdrc.missionmillet.domain.TimePeriod;
import org.sdrc.missionmillet.domain.Type;
import org.sdrc.missionmillet.domain.TypeDetails;
import org.sdrc.missionmillet.domain.UUIdGenerator;
import org.sdrc.missionmillet.domain.WASSANReports;
import org.sdrc.missionmillet.domain.WASSANReportsFile;
import org.sdrc.missionmillet.domain.WASSANSoEReport;
import org.sdrc.missionmillet.model.CollectUserModel;
import org.sdrc.missionmillet.model.StateDropDownModel;
import org.sdrc.missionmillet.model.TypeDetailsModel;
import org.sdrc.missionmillet.model.ValueObject;
import org.sdrc.missionmillet.model.WASSANReportsModel;
import org.sdrc.missionmillet.model.WASSANSoEReportModel;
import org.sdrc.missionmillet.model.YearAndMonthModel;
import org.sdrc.missionmillet.repository.AggregationRepository;
import org.sdrc.missionmillet.repository.ConfigurationRepository;
import org.sdrc.missionmillet.repository.TimePeriodRepository;
import org.sdrc.missionmillet.repository.TypeDetailsRepository;
import org.sdrc.missionmillet.repository.UUIdGeneratorRepository;
import org.sdrc.missionmillet.repository.WassanReportsFileRepository;
import org.sdrc.missionmillet.repository.WassanReportsRepository;
import org.sdrc.missionmillet.repository.WassanSoeReportRepository;
import org.sdrc.missionmillet.util.Constants;
import org.sdrc.missionmillet.util.LockWorkbook;
import org.sdrc.missionmillet.util.MissionMilletUtil;
import org.sdrc.missionmillet.util.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Subham Ashish (subham@sdrc.co.in)
 * 
 */
@Service
public class StateUserServiceImpl implements StateUserService {

	@Autowired
	private WassanSoeReportRepository wassanSoeReportRepository;

	@Autowired
	private WassanReportsFileRepository wassanReportsFileRepository;

	@Autowired
	private WassanReportsRepository wassanReportsRepository;

	@Autowired
	private TimePeriodRepository timePeriodRepository;

	@Autowired
	private ServletContext context;

	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;

	@Autowired
	private UUIdGeneratorRepository uuidGeneratorRepository;

	@Autowired
	private TypeDetailsRepository typeDetailsRepository;

	@Autowired
	private StateManager stateManager;

	@Autowired
	private ConfigurationRepository configurationRepository;

	@Autowired
	private AggregationRepository aggregationRepository;

	private SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
	private SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final Logger	LOGGER	= LoggerFactory.getLogger( "LOGGER" );
	

/**
 * @author subham (subham@sdrc.co.in)
 * 
 * @Description This method save the SoE details in WASSANSoEReport table and 
 * SoE file in WASSANReportsFile table.
 * 
 * It also validates the uuid for the uploaded sheet, if uuid value matches it allows to upload.
 * 
 * @param file
 * 
 */
	@Override
	@Transactional
	public ResponseEntity<String> wassanSoEReportSave(MultipartFile file) {

		HSSFWorkbook workbook;
		HSSFSheet spreadsheet;
		
		//Checking valid user login 
		CollectUserModel collectUserModel = (CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL);

		if (collectUserModel == null) {
			LOGGER.error("Error description : "+messageSourceNotification.getMessage("invalid.user", null, null)+"tries to acess wassanSoEReportSave() method : " 
					+fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		
		String email=collectUserModel.getEmailId()==null ? "" : collectUserModel.getEmailId();
		
		try {
			
			workbook = new HSSFWorkbook(file.getInputStream());

			Row row;
			Cell cell;
			
			//checking uuid sheet exist or not
			spreadsheet = workbook.getSheet("UUID_Verify");
			
			//validating uuid sheet present or not
			if (spreadsheet == null) {
				workbook.close();
				LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("+ email +")" +"Action :"+ 
						messageSourceNotification.getMessage("invalid.file.message", null,null)+" : "+fullDateFormat.format(new Date()));
				return new ResponseEntity<>(messageSourceNotification.getMessage("excel.sheet.not.matching", null,null), HttpStatus.BAD_REQUEST);
			}

			
			// checking user has enabled macros or not
			spreadsheet=null;
			spreadsheet = workbook.getSheet("mm_check");
			
			if(spreadsheet==null){
				LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("+collectUserModel.getEmailId()==null ? "" : collectUserModel.getEmailId()+")" +"Action :"+ 
						messageSourceNotification.getMessage("invalid.macro.message", null,null)+" : "+fullDateFormat.format(new Date()));
				workbook.close();
				return new ResponseEntity<>(messageSourceNotification.getMessage("macro.not.enable.message", null,null), HttpStatus.BAD_REQUEST);
			}
				
			row = spreadsheet.getRow(0);
			cell = row.getCell(0);

			
			if (cell.getBooleanCellValue() == false || cell.getCellType() == 3) {
				LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("+ email +")" +"Action :"+ 
						messageSourceNotification.getMessage("invalid.macro.message", null,null)+" : "+fullDateFormat.format(new Date()));
				workbook.close();
				return new ResponseEntity<>(messageSourceNotification.getMessage("macro.not.enable.message", null,null), HttpStatus.BAD_REQUEST);
				
			} else {

				cell.setCellValue(false);

				spreadsheet = null;
				spreadsheet = workbook.getSheet("UUID_Verify");
				row = spreadsheet.getRow(0);
				cell = row.getCell(1);

				boolean budgetCheck=true;
				
				String cellValue = cell.getStringCellValue();

				Timestamp timestamp = new Timestamp(new java.util.Date().getTime());

				//fetching monthly periodicity value i.e 1 from properties file
				String monthlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.monthly", null,null);
				
				//fetching financial year 
				TimePeriod latestTimePeriod = timePeriodRepository.getTimePeriodofSixPeriodicity(monthlyPeriodicity,new Timestamp(new Date().getTime()));
				
				//fetching final time-period value by passing financial year ,currentdate from latestTimePeriod and yearly periodicity value as 1
				TimePeriod timePeriod = timePeriodRepository.getTimePeriodData(timestamp, messageSourceNotification.getMessage(
								"timeperiod.periodicity.yearly", null, null),latestTimePeriod.getFinancialYear());

				int year = timePeriod.getYear();

				CollectUser collectUser = new CollectUser();
				collectUser.setUserId(collectUserModel.getUserId());

				// fetching currentDate,startDate,endDate,month,year
				YearAndMonthModel yearAndMonthModel = startDateEndDateDetails();
				
				// current date value  should be in between start date and end date(both inclusive)
				if (DateUtils.isSameDay(yearAndMonthModel.getCurrentDate(), yearAndMonthModel.getStartDate())
						|| yearAndMonthModel.getCurrentDate().after(yearAndMonthModel.getStartDate()) && DateUtils
								.isSameDay(yearAndMonthModel.getCurrentDate(), yearAndMonthModel.getEndDate())
						|| yearAndMonthModel.getCurrentDate().before(yearAndMonthModel.getEndDate())) {

					
					//checking uuid value for reporting month and year if found allowed to upload
					UUIdGenerator uuidGenerator = uuidGeneratorRepository.findByUuidAndMonthAndYear(cellValue,
							yearAndMonthModel.getMonth(), yearAndMonthModel.getYear());

					if (uuidGenerator != null) {
						
						//budget column check
						Calendar cal = Calendar.getInstance();
						cal.set(Calendar.MONTH, Calendar.OCTOBER);
						cal.set(Calendar.DATE, 16);
						
						Date octStartDate;
						Date octEndDate;
						octStartDate=cal.getTime();
						
						cal.set(Calendar.MONTH, Calendar.NOVEMBER);
						cal.set(Calendar.DATE, 15);
						octEndDate=cal.getTime();
						
						//current date should between octStartDate and octEndDate as budget column should be verified in the month of oct only
						if((DateUtils.isSameDay(yearAndMonthModel.getCurrentDate(), octStartDate) || yearAndMonthModel.getCurrentDate()
								.after(octStartDate)) && (DateUtils.isSameDay(yearAndMonthModel.getCurrentDate(), octEndDate) || 
								yearAndMonthModel.getCurrentDate()
								.before(octEndDate))){
								
						//budget column should match only in the month of october
						WASSANSoEReport newWASSANSoEReport = wassanSoeReportRepository.findByMaxCreatedDateAndYear(year);
						
						if(newWASSANSoEReport!=null)
							budgetCheck=wassanSoEBudgetCheck(file,newWASSANSoEReport.getWASSANSoEReportsId(),collectUserModel);
						
						}

						if(budgetCheck==false){
							workbook.close();
							LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("+ email +")" +"Action :"+ 
									messageSourceNotification.getMessage("budget.not.matching", null,null)+" : "+fullDateFormat.format(new Date()));
							return new ResponseEntity<>(messageSourceNotification.getMessage("budget.not.matching", null,null), HttpStatus.BAD_REQUEST);
						}
						
						byte[] fileByte = file.getBytes();
						WASSANReportsFile wassanReportsFile = new WASSANReportsFile();
						WASSANSoEReport wassanSoeReport = new WASSANSoEReport();
						Timestamp currentTime = new Timestamp(new java.util.Date().getTime());
						wassanSoeReport.setMonth(yearAndMonthModel.getMonth());
						wassanSoeReport.setYear(year);
						wassanSoeReport.setCreatedDate(currentTime);
						wassanSoeReport.setCollectUser(collectUser);
						wassanSoeReport.setUUIdGenerator(uuidGenerator);

						WASSANSoEReport newwassanSoeReport = wassanSoeReportRepository.save(wassanSoeReport);

						wassanReportsFile.setReportsFile(fileByte);
						wassanReportsFile.setLive(true);
						wassanReportsFile.setWassanSoeReport(newwassanSoeReport == null ? null : newwassanSoeReport);
						wassanReportsFile.setStatus(new TypeDetails(15));
						wassanReportsFile.setCollectUser(collectUser);

						wassanReportsFileRepository.save(wassanReportsFile);

						workbook.close();
						LOGGER.info("Description : "+collectUserModel.getUsername()+ "("+ email +")" +"Action :"+ messageSourceNotification.getMessage
								("wassan.soe.save", null,null)+ "for "+Month.of(yearAndMonthModel.getMonth()).name()+" : "+fullDateFormat.format(new Date()));
						return new ResponseEntity<>(messageSourceNotification.getMessage("soe.upload.success", null,null), HttpStatus.OK);
					} else {
						LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("+ email +")" +"Action :"+ messageSourceNotification
								.getMessage("cut.off.date", null,null)+"for "+Month.of(yearAndMonthModel.getMonth()).name()+" : "+fullDateFormat.format(new Date()));
						workbook.close();
						return new ResponseEntity<>(messageSourceNotification.getMessage("upload.correct.file", null,null), HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
			}
			workbook.close();
			return null;
		} catch (Exception e) {
			LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("+ email +")" +"Action :"+ messageSourceNotification
					.getMessage("upload.soe.message.error", null,null)+" : "+fullDateFormat.format(new Date()),e);
			return  new ResponseEntity<>(messageSourceNotification.getMessage("invalid.excel", null, null),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	private boolean wassanSoEBudgetCheck(MultipartFile file, Integer wassanSoEReportsId,CollectUserModel collectUserModel) {

		HSSFWorkbook workbook = null;
		HSSFSheet spreadsheet=null;
		String email=collectUserModel.getEmailId()==null ? "" : collectUserModel.getEmailId();
		try {

			WASSANReportsFile wassanReportFile = wassanReportsFileRepository.findBywassanSoeId(wassanSoEReportsId);

			workbook = new HSSFWorkbook(file.getInputStream());

			Row row;
			Cell cell;

			spreadsheet = workbook.getSheet("wassan SOE");
			row = spreadsheet.getRow(13);
			
			cell = row.getCell(5);
			
			Double prevBudgetValue=cell.getNumericCellValue();
			
			spreadsheet=null;
			
			row=null;
			cell=null;
			
			InputStream inps = new ByteArrayInputStream(wassanReportFile.getReportsFile());
			
			workbook = new HSSFWorkbook(inps);
			
			spreadsheet = workbook.getSheet("wassan SOE");
			row = spreadsheet.getRow(13);
			
			cell = row.getCell(5);
			
			Double newBudgetValue=cell.getNumericCellValue();
			
			
			if(prevBudgetValue.equals(newBudgetValue)){
				workbook.close();
				return true;
			}else{
				workbook.close();
				return false;
			}
			

		} catch (Exception e) {
			LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("+ email +")" +"Action :"+ messageSourceNotification
					.getMessage("upload.soe.message.error", null,null)+" : "+fullDateFormat.format(new Date()),e);
			throw new RuntimeException();
		} finally {
			try {
				workbook.close();
			} catch (Exception e) {
				LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("+ email +")" +"Action :"+ messageSourceNotification
						.getMessage("upload.soe.message.error", null,null)+" : "+fullDateFormat.format(new Date()),e);
				throw new RuntimeException();
			}

		}

	}

	/**
	 * @author subham(subham@sdrc.co.in)
	 * 
	 * @Description This method calculates month,start Date end date,year  for reporting month 
	 *	example:
	 * month=April 
	 * startDate:1april
	 * endDate:15thMay
	 * 
	 */
	private YearAndMonthModel startDateEndDateDetails() {
		
		Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
		
		String monthlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.monthly", null, null);
		
		//fetching financial year
		TimePeriod latestTimePeriod = timePeriodRepository.getTimePeriodofSixPeriodicity(monthlyPeriodicity,
				new Timestamp(new Date().getTime()));

		TimePeriod timePeriod = timePeriodRepository.getTimePeriodData(timestamp,messageSourceNotification.getMessage("timeperiod.periodicity.yearly", null, null),
				latestTimePeriod.getFinancialYear());

		String Financialyear = timePeriod.getFinancialYear();

		Integer month = 0;
		Date startDate = new Date();
		Date endDate = new Date();
		Integer year = 0;
		Date currentDate = new Date();
		Date startCompare = new Date();

		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.MONTH) == 3 | cal.get(Calendar.MONTH) == 4) {

			currentDate = new Date();
			cal.set(Calendar.MONTH, Calendar.MAY);
			cal.set(Calendar.DATE, 16);
			startCompare = cal.getTime();

			if (currentDate.before(startCompare)) {

				cal.set(Calendar.MONTH, Calendar.APRIL);
				year = cal.get(Calendar.YEAR);
				month = cal.get(Calendar.MONTH) + 1;
				cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
				startDate = cal.getTime();

				cal.add(Calendar.MONTH, 1);
				cal.set(Calendar.DATE, 15);
				endDate = cal.getTime();

			} else {

				currentDate = new Date();
				cal.set(Calendar.MONTH, Calendar.MAY);
				cal.set(Calendar.DATE, 16);
				startCompare = cal.getTime();

				if (DateUtils.isSameDay(currentDate, startDate) || currentDate.after(startDate)) {

					month = cal.get(Calendar.MONTH) + 1;
					year = cal.get(Calendar.YEAR);
					cal.set(Calendar.DATE, 16);
					startDate = cal.getTime();

					cal.add(Calendar.MONTH, 1);
					cal.set(Calendar.DATE, 15);
					endDate = cal.getTime();
				}
			}

		} else if (cal.get(Calendar.MONTH) == 0) {

			currentDate = new Date();
			cal.set(Calendar.DATE, 16);
			startCompare = cal.getTime();
			if (currentDate.before(startCompare)) {

				year = cal.get(Calendar.YEAR) - 1;
				month = cal.get(Calendar.MONTH) + 12;

				Calendar previousMonthDay = (Calendar) cal.clone();
				previousMonthDay.add(Calendar.MONTH, -1);
				startDate = previousMonthDay.getTime();

				previousMonthDay.add(Calendar.MONTH, 1);
				previousMonthDay.set(Calendar.DATE, 15);
				endDate = previousMonthDay.getTime();

			} else {

				currentDate = new Date();
				cal.set(Calendar.DATE, 16);
				startCompare = cal.getTime();

				if (DateUtils.isSameDay(currentDate, startDate) || currentDate.after(startDate)) {
					month = cal.get(Calendar.MONTH) + 1;
					year = cal.get(Calendar.YEAR);
					cal.set(Calendar.DATE, 16);
					startDate = cal.getTime();

					cal.add(Calendar.MONTH, 1);
					cal.set(Calendar.DATE, 15);
					endDate = cal.getTime();
				}
			}

		} else {

			currentDate = new Date();
			cal.set(Calendar.DATE, 16);
			startCompare = cal.getTime();
			if (currentDate.before(startCompare)) {

				year = cal.get(Calendar.YEAR);
				month = cal.get(Calendar.MONTH);

				Calendar previousMonthDay = (Calendar) cal.clone();
				previousMonthDay.add(Calendar.MONTH, -1);
				startDate = previousMonthDay.getTime();

				previousMonthDay.add(Calendar.MONTH, 1);
				previousMonthDay.set(Calendar.DATE, 15);
				endDate = previousMonthDay.getTime();

			} else {

				currentDate = new Date();
				cal.set(Calendar.DATE, 16);
				startCompare = cal.getTime();

				if (DateUtils.isSameDay(currentDate, startDate) || currentDate.after(startDate)) {

					month = cal.get(Calendar.MONTH) + 1;
					year = cal.get(Calendar.YEAR);
					cal.set(Calendar.DATE, 16);
					startDate = cal.getTime();

					cal.add(Calendar.MONTH, 1);
					cal.set(Calendar.DATE, 15);
					endDate = cal.getTime();
				}
			}
		}

		YearAndMonthModel model = new YearAndMonthModel();

		model.setCurrentMonth(Month.of(month).name());
		model.setFinancialYear(Financialyear);
		model.setCurrentDate(currentDate);
		model.setEndDate(endDate);
		model.setMonth(month);
		model.setYear(year);
		model.setStartDate(startDate);
		return model;

	}

	/**
	 * @author subham (subham@sdrc.co.in)
	 * 
	 * @Description Returns the file path of the WASSAN SoE template which is to be downloaded for the reporting month.
	 * 
	 */

	@Override
	@Transactional
	public String downloadStateTemplate() {

		Calendar cal = Calendar.getInstance();
		
		//Checking valid user login
		CollectUserModel collectUserModel = (CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL);

		if (collectUserModel == null){
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null)+" tries to access downloadStateTemplate() method"
					+fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		String email = collectUserModel.getEmailId()==null ? "" : collectUserModel.getEmailId();
		CollectUser collectUser = new CollectUser();
		collectUser.setUserId(collectUserModel.getUserId());

		Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
		String monthlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.monthly", null, null);
		TimePeriod latestTimePeriod = timePeriodRepository.getTimePeriodofSixPeriodicity(monthlyPeriodicity,new Timestamp(new Date().getTime()));

		TimePeriod timePeriod = timePeriodRepository.getTimePeriodData(timestamp,messageSourceNotification.getMessage("timeperiod.periodicity.yearly", null, null),
				latestTimePeriod.getFinancialYear());

		int year = timePeriod.getYear();

		// for january upto 15th it should take previous year
		if (cal.get(Calendar.MONTH) == 0 && cal.get(Calendar.DATE) <= 15) {
			year = cal.get(Calendar.YEAR) - 1;
		}

		Integer month = null;
		boolean isLock = false;
		Date currentDate = new Date();
		Date startCompare = new Date();
		// fetching currentDate,startDate,endDate,month
		YearAndMonthModel yearAndMonthModel = startDateEndDateDetails();
		try {

			// current date value  should be in between start date and end date(including start date and end date)
			if (DateUtils.isSameDay(yearAndMonthModel.getCurrentDate(), yearAndMonthModel.getStartDate())
					|| yearAndMonthModel.getCurrentDate().after(yearAndMonthModel.getStartDate())
							&& DateUtils.isSameDay(yearAndMonthModel.getCurrentDate(), yearAndMonthModel.getEndDate())
					|| yearAndMonthModel.getCurrentDate().before(yearAndMonthModel.getEndDate())) {

				// if it is april month download from resource folder 
				cal = Calendar.getInstance();

				if (cal.get(Calendar.MONTH) == 3 | cal.get(Calendar.MONTH) == 4) {

					currentDate = new Date();
					cal.set(Calendar.MONTH, Calendar.MAY);
					cal.set(Calendar.DATE, 16);
					startCompare = cal.getTime();

					if (currentDate.before(startCompare)) {

						//fetching last updated record present(for the current month and year)
						WASSANSoEReport wASSANSoEReport = wassanSoeReportRepository.findByMaxCreatedDateAndMonthAndYear(yearAndMonthModel.getMonth(), year);

						if (wASSANSoEReport == null) {

							// download from resource folder
							String path = downloadFromResourceFolder();
							return path;

						} else {

							// download last updated record of that month
							Integer reportId = wASSANSoEReport.getWASSANSoEReportsId();
							isLock = false;
							String path = lastUpdatedRecordDownload(reportId, isLock);
							return path;

						}

					} else if (cal.get(Calendar.MONTH) == 4 && cal.get(Calendar.DATE) >= 16) {

						/*
						 * download from database and write UUID for that month in file finding last month record which is last
						 * inserted of that financial year only
						 */
						cal = Calendar.getInstance();
						currentDate = new Date();
						cal.set(Calendar.MONTH, Calendar.JUNE);
						cal.set(Calendar.DATE, 16);
						startCompare = cal.getTime();

						// for the month of may
						if (currentDate.before(startCompare)) {

							month = cal.get(Calendar.MONTH);
							
							WASSANSoEReport wASSANSoEReport = wassanSoeReportRepository.findByMaxCreatedDateAndMonthAndYear(month, year);

							if (wASSANSoEReport != null) {

								Integer reportId = wASSANSoEReport.getWASSANSoEReportsId();
								isLock = false;
								String path = lastUpdatedRecordDownload(reportId, isLock);

								return path;

							} else if (wASSANSoEReport == null) {

								/*
								 * if not found than download last month ie.april record
								 */
								month = cal.get(Calendar.MONTH);

								WASSANSoEReport newWASSANSoEReport = wassanSoeReportRepository.findByMaxCreatedDateAndMonthAndYear(month - 1, year);

								if (newWASSANSoEReport != null) {

									Integer reportId = newWASSANSoEReport.getWASSANSoEReportsId();
									isLock = true;
									String path = lastUpdatedRecordDownload(reportId, isLock);
									return path;

								} else {
									/*
									 * suppose user has not downloaded the template in the month of april than
									 * download from resource folder and lock april month completely
									 */
									String path = downloadFromResourceFolder();
									return path;
								}
							}
						}
					}
				} else {
					/*
					  fetching current month(after may) record here, if found
					  in db than show that file else download last inserted records.*/
					 
					WASSANSoEReport wASSANSoEReport = wassanSoeReportRepository.findByMaxCreatedDateAndMonthAndYear(yearAndMonthModel.getMonth(), year);

					if (wASSANSoEReport != null) {

						Integer reportId = wASSANSoEReport.getWASSANSoEReportsId();
						isLock = false;
						String path = lastUpdatedRecordDownload(reportId, isLock);
						return path;

					} else if (wASSANSoEReport == null) {

						/* if not found than find last inserted record of current financial year 
						finding last updated record of current year by passing year as parameter*/
						WASSANSoEReport newWASSANSoEReport = wassanSoeReportRepository.findByMaxCreatedDateAndYear(year);

						if (newWASSANSoEReport != null) {

							Integer reportId = newWASSANSoEReport.getWASSANSoEReportsId();
							isLock = true;
							String path = lastUpdatedRecordDownload(reportId, isLock);
							return path;

						} else {

							// download from resource folder
							String path = downloadFromResourceFolder();
							return path;
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("+ email +")" +"Action :"+ messageSourceNotification.getMessage
					("download.soe.message.error", null,null)+" : "+fullDateFormat.format(new Date()),e);
			throw new RuntimeException();

		}
		return null;
	}
	
	/**
	 * @author subham (subham@sdrc.co.in)
	 * 
	 * @Description Returns the file path of last updated WASSAN SoE template.
	 * 
	 * @param reportId
	 * @param isLock
	 * 
	 */

	private String lastUpdatedRecordDownload(Integer reportId, boolean isLock) {

		YearAndMonthModel yearAndMonthModel = startDateEndDateDetails();

		try {

			WASSANReportsFile wassanReportFile = wassanReportsFileRepository.findBywassanSoeId(reportId);

			ByteArrayInputStream bis = new ByteArrayInputStream(wassanReportFile.getReportsFile());

			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			for (int readNum; (readNum = bis.read(wassanReportFile.getReportsFile())) != -1;) {
				bos.write(wassanReportFile.getReportsFile(), 0, readNum);
			}

			byte[] getFileBytes = bos.toByteArray();

			File path = new File(messageSourceNotification.getMessage("wassan.upload.report.path", null, null)
					+ "wassan_SOE" + dateFormat.format(new Date()) + ".xls");

			String filePath = path.getAbsolutePath();
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(getFileBytes);
			fos.flush();
			fos.close();

			// read that excel file again and them write uuid for present month
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);

			// generating UUID
			String uuidValue = uuidGenerator();

			HSSFWorkbook workbook = new HSSFWorkbook(fis);

			HSSFSheet sheet;
			sheet = workbook.getSheet("UUID_Verify");
			HSSFCell cell1;
			HSSFRow row1 = sheet.getRow(0);
			cell1 = row1.getCell(0);
			cell1.setCellValue("uuid");
			cell1 = row1.getCell(1);
			cell1.setCellValue(uuidValue);

			UUIdGenerator uuidGenerator = saveInDb(uuidValue, yearAndMonthModel.getMonth(), yearAndMonthModel.getYear(),
					new CollectUser(((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL)).getUserId()));

			uuidGeneratorRepository.save(uuidGenerator);
			
			if (isLock == true) {
				//Reading the wassan_excel.properties file from messages directory
				Map<Integer, String> map = MissionMilletUtil.getPropertiesValues(
						getClass().getClassLoader().getResourceAsStream("messages/wassan_excel.properties"));
				//Reading the state_month_values.properties file from messages directory
				Map<Integer, String> mapMonth = MissionMilletUtil.getPropertiesValues(
						getClass().getClassLoader().getResourceAsStream("messages/state_month_values.properties"));

				workbook = excelLock(workbook, sheet, map, yearAndMonthModel.getStartDate(), mapMonth);
			}
			sheet = null;
			row1 = null;
			cell1 = null;
			sheet = workbook.getSheet("mm_check");
			row1 = sheet.getRow(0);
			cell1 = row1.getCell(0);
			if(cell1.getBooleanCellValue() != false || cell1.getCellType() != 3)
				cell1.setCellValue(false);
			
			File newPath = new File(filePath);
			FileOutputStream newfos = new FileOutputStream(newPath);
			workbook.write(newfos);
			newfos.flush();
			newfos.close();
			workbook.close();
			return newPath.toString();

		} catch (Exception e) {
			LOGGER.error("Error description : "+"while access lastUpdatedRecordDownload() method : "
					+fullDateFormat.format(new Date()), e);
			throw new RuntimeException();
		}
	}

	/**
	 *@author subham (subham@sdrc.co.in)
	 *
	 *@Description This method returns the file path of SoE from resource folder(fresh template)
	 * 
	 */
	private String downloadFromResourceFolder() {

		YearAndMonthModel yearAndMonthModel = startDateEndDateDetails();

		String uuidValue = uuidGenerator();

		File file = new File(context.getRealPath(messageSourceNotification.getMessage("wassan.upload.template", null, null)));

		FileInputStream fis;

		try {

			fis = new FileInputStream(file);
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet;
			
			//creating sheet and writing uuid value in that
			sheet = workbook.createSheet("UUID_Verify");
			HSSFCell cell;
			HSSFRow row = sheet.createRow(0);
			cell = row.createCell(0);
			cell.setCellValue("uuid");
			cell = row.createCell(1);
			cell.setCellValue(uuidValue);

			UUIdGenerator uuidGenerator = saveInDb(uuidValue, yearAndMonthModel.getMonth(), yearAndMonthModel.getYear(),
					new CollectUser(((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL)).getUserId()));

			uuidGeneratorRepository.save(uuidGenerator);

			Map<Integer, String> map = MissionMilletUtil.getPropertiesValues(
					getClass().getClassLoader().getResourceAsStream("messages/wassan_excel.properties"));

			Map<Integer, String> mapMonth = MissionMilletUtil.getPropertiesValues(
					getClass().getClassLoader().getResourceAsStream("messages/state_month_values.properties"));

			workbook = excelLock(workbook, sheet, map, yearAndMonthModel.getStartDate(), mapMonth);

			String path = messageSourceNotification.getMessage("wassan.upload.report.path", null, null) + "wassan_SOE"
					+ dateFormat.format(new Date()) + ".xls";

			FileOutputStream fileOut = new FileOutputStream(path);
			workbook.write(fileOut);

			fileOut.close();
			workbook.close();
			return path;

		} catch (FileNotFoundException e) {

			LOGGER.error("Error description : "+"Action :"+ "while access downloadFromResourceFolder() method : "+fullDateFormat.format(new Date()),e);
			throw new RuntimeException();

		} catch (Exception e) {

			LOGGER.error("Error description : "+"Action :"+ "while access downloadFromResourceFolder() method : "+fullDateFormat.format(new Date()),e);
			throw new RuntimeException();
		}
	}

	/**
	 * @author Subham Ashish (subham@sdrc.co.in)
	 * 
	 * @Description- This method save uuid value,month,year and user in uuidUUIdGenerator table
	 * 
	 * @param uuidValue
	 * @param month
	 * @param year
	 * @param collectUser
	 *
	 */
	private UUIdGenerator saveInDb(String uuidValue, Integer month, int year, CollectUser collectUser) {

		UUIdGenerator uuidGenerator = new UUIdGenerator();
		uuidGenerator.setUuid(uuidValue);
		uuidGenerator.setMonth(month);
		uuidGenerator.setYear(year);
		Timestamp createdDate = new Timestamp(new java.util.Date().getTime());
		uuidGenerator.setCreatedDate(createdDate);
		uuidGenerator.setCollectUser(collectUser);

		return uuidGenerator;
	}

	/**
	 * @author Subham Ashish (subham@sdrc.co.in)
	 * 
	 * @Dedcription it generates uuid
	 */
	private String uuidGenerator() {
		UUID uuid = UUID.randomUUID();
		String uuidValue = uuid.toString();
		return uuidValue;
	}

	/**
	 * @author Subrata 
	 * @Description Enabling excel lock or unlock
	 */
	private HSSFWorkbook excelLock(HSSFWorkbook workbook, HSSFSheet sheet, Map<Integer, String> map, Date startDate,
			Map<Integer, String> mapMonth) {
		try {
			TimePeriod latestTimePeriod = timePeriodRepository
					.getTimePeriodofSixPeriodicity(messageSourceNotification.getMessage("timeperiod.periodicity.yearly", null, null), 
							new Timestamp(new java.util.Date().getTime()));
			sheet = workbook.getSheet("wassan SOE");
			HSSFRow row = sheet.getRow(2); 
			int colNum = 0;
			Cell cell = null;
			cell = row.getCell(4);
			cell.setCellValue("FY ("+latestTimePeriod.getFinancialYear()+")");
			cell = null;
			List<Integer> allKeyList = new ArrayList<Integer>(map.keySet());
			List<Integer> listOfMonth = new ArrayList<Integer>(mapMonth.keySet());

			Collections.sort(allKeyList);

			cell = row.getCell(6);
			// Checking if the cell is blank or not
			if (cell.getCellType() == 3) { 
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH);
				if (month < 3)
					cal.add(Calendar.YEAR, -1);
				SimpleDateFormat sdf = new SimpleDateFormat("MM");

				Date currentDate = null;
				int k = 0;
				boolean beforeApril = true;
				boolean afterApril = true;
				for (int i = 0; i <= listOfMonth.size(); i++) {

					if (k < 3 && beforeApril == true) {
						currentDate = new SimpleDateFormat("dd-MM-yyyy").parse("01-" + 1 + "-" + (year + 1));
						cal.setTime(currentDate);
						beforeApril = false;
					}
					if (k >= 3 && afterApril == true) {
						currentDate = new SimpleDateFormat("dd-MM-yyyy").parse("01-" + 4 + "-" + year);
						cal.setTime(currentDate);
						afterApril = false;
					}
					Date date = cal.getTime();
					Integer colValues = Integer.valueOf(mapMonth.get(Integer.valueOf(sdf.format(date).trim())));

					cell = row.getCell(colValues);
					cell.setCellValue(date);

					k++;
					cal.set(Calendar.MONTH, k);
				}
			}
			Date date = null;
			LocalDate localDateFromExcel = null;
			// Converting current date to LocalDate object type
			LocalDate localDateFromDb = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			// Iterating the column values which contains the month.
			for (Integer in : allKeyList) {
				cell = row.getCell(in);

				date = cell.getDateCellValue();
				// Converting date which is in excel to LocalDate object type
				localDateFromExcel = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				// If localDateFromDb and localDateFromExcel are same then, we got the month for unlocking
				if (localDateFromDb.getMonthValue() == localDateFromExcel.getMonthValue()) {
					colNum = in;
					break;
				}
			}

			// Getting style sheet for unlocking
			HSSFCellStyle styleForUnLocking = LockWorkbook.getStyleForUnLocking(workbook);
			// Getting style sheet for locking
			HSSFCellStyle styleForLocking = LockWorkbook.getStyleForLocking(workbook);
			// Getting column values from map to unlock. 
			String str = map.get(colNum);
			row = null;
			cell = null;
			
			int unitIndex=Integer.parseInt(messageSourceNotification.getMessage("wassan.unit.col.index", null, null));
			int rateIndex=Integer.parseInt(messageSourceNotification.getMessage("wassan.rate.col.index", null, null));
			int nosIndex=Integer.parseInt(messageSourceNotification.getMessage("wassan.nos.col.index", null, null));
			int startIndex=Integer.parseInt(messageSourceNotification.getMessage("wassan.start.row.index", null, null));
			int endIndex=Integer.parseInt(messageSourceNotification.getMessage("wassan.end.row.index", null, null));
			int obIndex=Integer.parseInt(messageSourceNotification.getMessage("wassan.ob.col.index", null, null));
			int asupmIndex=Integer.parseInt(messageSourceNotification.getMessage("wassan.asupm.col.index", null, null));
			Calendar cal = Calendar.getInstance();
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			
			boolean flag = getColumnType(sheet, startIndex, endIndex, unitIndex, rateIndex, nosIndex);
			
			for (int i = startIndex; i <= endIndex; i++) {
				row = sheet.getRow(i);
				for (int j = 0; j < row.getLastCellNum(); j++) {
					cell = row.getCell(j);
					
					//Applying the style for locking for all column 
					if (cell.getCellStyle().getLocked() == false)
						cell.setCellStyle(styleForLocking);
					//Applying the style for unlocking for the required column
					if (flag) {
						if((month == 9 && day >= 16) || (month == 10 && day < 16)){
							if ((cell.getColumnIndex() == unitIndex || cell.getColumnIndex() == rateIndex 
									|| cell.getColumnIndex() == nosIndex)) {
								cell.setCellStyle(styleForUnLocking);
							}
						}else{
							if ((cell.getColumnIndex() == unitIndex || cell.getColumnIndex() == rateIndex 
									|| cell.getColumnIndex() == nosIndex
									|| cell.getColumnIndex() == obIndex || cell.getColumnIndex() == asupmIndex )) {
								cell.setCellStyle(styleForUnLocking);
							}
						}
						
					} 
					//Applying the style for unlocking for the required column
					if ((cell.getColumnIndex() == Integer.valueOf((str.split(",")[0]).trim()))
							|| (cell.getColumnIndex() == Integer.valueOf((str.split(",")[1]).trim()))
							|| (cell.getColumnIndex() == Integer.valueOf((str.split(",")[2]).trim()))) {

						cell.setCellStyle(styleForUnLocking);
					}
				}
			}

			sheet = null;
			sheet = workbook.getSheet("mm_check");
			Row macroRow = null;
			Cell macroCell = null;
			if (sheet.getRow(0) == null) {
				macroRow = sheet.createRow(0);
				macroCell = macroRow.createCell(0);
			} else {
				macroRow = sheet.getRow(0);
				macroCell = macroRow.getCell(0);
			}
			macroCell.setCellValue(false);
			// Hiding the UUID and macro sheet in the SoE template
			workbook.setSheetHidden(2, HSSFWorkbook.SHEET_STATE_VERY_HIDDEN);
			workbook.setSheetHidden(3, HSSFWorkbook.SHEET_STATE_VERY_HIDDEN);

			return workbook;
		} catch (ParseException e) {
			LOGGER.error("Error description : " +"Action :"+ messageSourceNotification.getMessage
					("excel.error.message", null,null)+" : "+fullDateFormat.format(new Date()),e);
			return null;
		}

	}

	/**
	 * @Description Checking budget is entered or not  
	 * 
	 * @author Subrata
	 * 
	 * @param sheet
	 */

	private boolean getColumnType(HSSFSheet sheet, int startIndex, int endIndex, int unitIndex, int rateIndex, int nosIndex) {

		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		List<Integer> listColumn = new ArrayList<Integer>(Arrays.asList(unitIndex, rateIndex, nosIndex));
		boolean flag = true;
		for (int i = startIndex; i <= endIndex;i++) {
			for (Integer colNum : listColumn) {
				HSSFRow row = sheet.getRow(i);
				Cell cell = row.getCell(colNum);
				if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
					flag = true;
				} else {
					flag = false;
					break;
				}
			}
			if(flag==false)
				break;
			
		}
		if (flag==false && (month == 9 && day >= 16) || (month == 10 && day < 16)) {
			return true;
		}
		return flag;
	}

	/**
	 * @author Subham Ashish (subham@sdrc.co.in)
	 * 
	 * @Description This method fetch all the SoE details uploaded by admin to display in history
	 * section, financial year wise
	 * 
	 */
	@Override
	@Transactional
	public Map<String, List<WASSANSoEReportModel>> wassonSoEHistoryDetails() {

		//fetching last updated record of every month,  as in one month multiple SoE is being uploaded 
		List<WASSANSoEReport> wassanSoeReportList = wassanSoeReportRepository.findByuniqueMonthAndMaxCreatedDate();

		//fetching all the monthly timeperiod List available
		List<TimePeriod> timePeriodsList = timePeriodRepository.findByPeriodicityOrderByFinancialYearDesc(
				messageSourceNotification.getMessage("timeperiod.periodicity.monthly", null, null));

		Map<String, TimePeriod> timePeriodmap = new LinkedHashMap<String, TimePeriod>();

		//making year and month as a key to identify which timeperiod value should be used to set in WASSANSoEReportModel
		for (TimePeriod timePeriod : timePeriodsList) {

			Integer year = timePeriod.getYear();

			if (timePeriod.getTimePeriod().equals("Jan") || timePeriod.getTimePeriod().equals("Feb")
					|| timePeriod.getTimePeriod().equals("Mar")) {

				year = year - 1;

			}

			timePeriodmap.put(year + "_" + timePeriod.getTimePeriod(), timePeriod);
		}

		List<WASSANSoEReportModel> wassanSoeReportModelList = new ArrayList<WASSANSoEReportModel>();

		for (WASSANSoEReport wassanSoEReport : wassanSoeReportList) {

			WASSANSoEReportModel wassanSoEReportModel = new WASSANSoEReportModel();
			Month month = Month.of(wassanSoEReport.getMonth());
			wassanSoEReportModel.setReportsFileId(wassanSoEReport.getWASSANSoEReportsId());
			wassanSoEReportModel.setMonth(month.getDisplayName(TextStyle.SHORT, Locale.UK));
			wassanSoEReportModel.setYear(wassanSoEReport.getYear() != 0 ? wassanSoEReport.getYear() : null);
			wassanSoEReportModel.setCreatedDate(newFormat.format(wassanSoEReport.getCreatedDate()));

			TimePeriod timePeriod = timePeriodmap.get(wassanSoEReport.getYear() + "_" + month.getDisplayName(TextStyle.SHORT, Locale.UK));
			wassanSoEReportModel.setFinancialYear(timePeriod.getFinancialYear() != null ? timePeriod.getFinancialYear() : null);
			wassanSoeReportModelList.add(wassanSoEReportModel);
		}

		List<WASSANSoEReportModel> list = null;
		Map<String, List<WASSANSoEReportModel>> map = new LinkedHashMap<String, List<WASSANSoEReportModel>>();
		
		//putting financial year as a key
		for (int i = 0; i < wassanSoeReportList.size(); i++) {

			if (map.containsKey(wassanSoeReportModelList.get(i).getFinancialYear())) {

				list = map.get(wassanSoeReportModelList.get(i).getFinancialYear());
				list.add(wassanSoeReportModelList.get(i));
				map.put(wassanSoeReportModelList.get(i).getFinancialYear(), list);

			} else {
				list = new ArrayList<WASSANSoEReportModel>();
				list.add(wassanSoeReportModelList.get(i));
				map.put(wassanSoeReportModelList.get(i).getFinancialYear(), list);
			}
		}

		return map;
	}

	/**
	 * @author Subham Ashish (subham@sdrc.co.in) 
	 * 
	 * @Description Returns the file path, which is to be downloaded in the history section of SoE
	 * 
	 * @param id
	 */
	@Override
	@Transactional
	public String wassanMonthlySoEDownload(Integer id) {

		CollectUserModel collectUserModel = (CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		if (collectUserModel == null){
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null)+" tries to access wassanMonthlySoEDownload() "
					+ "method : "+fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		
		String email = collectUserModel.getEmailId()==null ? "" : collectUserModel.getEmailId();
		WASSANReportsFile wassanReportFile = wassanReportsFileRepository.findFileByWassanSoeReportId(id,collectUserModel.getUserId());

		try {

			if (wassanReportFile != null) {

				byte[] file = (byte[]) wassanReportFile.getReportsFile();
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file);
				
				//locking excel file
				HSSFWorkbook workbook = LockWorkbook.workbookLock(new HSSFWorkbook(byteArrayInputStream));
				
				String date = new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date());
				String filepath = messageSourceNotification.getMessage("wassan.upload.report.path", null, null)
						+ "Monthly_SoE_Template" + date + ".xls";
				FileOutputStream fileOut = new FileOutputStream(filepath);
				workbook.write(fileOut);
				fileOut.close();
				return filepath;
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("+ email +")"
					+"Action :"+ messageSourceNotification.getMessage("download.soe.history.error", null,null)+ "for "+Month.of(wassanReportFile.getWassanReports()
							.getMonth()).name()+" : "+fullDateFormat.format(new Date()),e);
					
			throw new RuntimeException();
		} catch (IOException e) {
			LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("+ email +")"
					+"Action :"+ messageSourceNotification.getMessage("download.soe.history.error", null,null)+ "for "+Month.of(wassanReportFile.getWassanReports()
							.getMonth()).name()+" : "+fullDateFormat.format(new Date()),e);
					
			throw new RuntimeException();
		}
		return messageSourceNotification.getMessage("not.found", null,null);
	}

	/**
	 * @author subham (subham@sdrc.co.in)
	 * 
	 * @Description This method changes Month and year dynamically in SoE section
	 */
	@Override
	public YearAndMonthModel monthAndFinancialYearDetail() {

		YearAndMonthModel model = startDateEndDateDetails();

		return model;
	}


	/**
	 * 
	 * @author subham (subham@sdrc.co.in)
	 * 
	 * @Description This method save the state report details in WASSANReports table, and report file path in WASSANReportsFile table
	 * 
	 * @param wassanReportsModel
	 */
	@Override
	@Transactional
	public ResponseEntity<String> wassanReportSave(WASSANReportsModel wassanReportsModel) {

		Calendar cal = Calendar.getInstance();
		
		//Checking valid user login
		CollectUserModel collectUserModel = (CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL);

		if (collectUserModel == null){
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null)+" tries to access wassanReportSave() method "
					+ " : "+fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		
		String email = collectUserModel.getEmailId()==null ? null : collectUserModel.getEmailId();
		CollectUser collectUser = new CollectUser();
		collectUser.setUserId(collectUserModel.getUserId());
		String reportType = wassanReportsModel.getReportType();

		//fetching timeperiod value
		TimePeriod timePeriod = timePeriodRepository.findByFinancialYearAndPeriodicity(wassanReportsModel.getFinancialYear(),
				messageSourceNotification.getMessage("timeperiod.periodicity.yearly", null, null));

		try {
			
			Date newdate = monthFormat.parse(wassanReportsModel.getMonth());
			cal = Calendar.getInstance();
			cal.setTime(newdate);
			Integer month = cal.get(Calendar.MONTH) + 1;
			

				Type type = new Type(Integer.parseInt(messageSourceNotification.getMessage("type", null,null)));
				TypeDetails typeDetails = typeDetailsRepository.findBytypeDetailNameAndType(reportType, type);

				Timestamp createdDate = new Timestamp(new java.util.Date().getTime());

				//saving report details in WASSANReports table
				WASSANReports wassanReports = new WASSANReports();
				wassanReports.setMonth(month);
				wassanReports.setYear(timePeriod.getYear());
				wassanReports.setRemarks(wassanReportsModel.getRemarks() != null ? wassanReportsModel.getRemarks() : null);
				wassanReports.setCreatedDate(createdDate);
				wassanReports.setLive(true);
				wassanReports.setReportType(typeDetails);
				wassanReports.setReportName(wassanReportsModel.getReportName());

				WASSANReports saveWassanReports = wassanReportsRepository.save(wassanReports);

				//saving report file in file system and updating its path in WASSANReportsFile table
				WASSANReportsFile wassanReportsFile = new WASSANReportsFile();

				wassanReportsFile.setWassanReports(saveWassanReports);
				wassanReportsFile.setLive(true);
				
				Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(wassanReportsModel.getMonth());
				cal = Calendar.getInstance();
				cal.setTime(date);
				
				String reportPathDir = messageSourceNotification.getMessage("wassan.report.save", null, null)+"/"+timePeriod.getYear()
					+"/"+Month.of(cal.get(Calendar.MONTH)+1).name()+"/";

				String reportFileName = FilenameUtils.removeExtension(wassanReportsModel.getUploadedFile().getOriginalFilename())+ "_"
								+ new SimpleDateFormat("ddMMyyyyHHmmssSSSSS").format(new java.util.Date()) + "."+ 
								(FilenameUtils.getExtension(wassanReportsModel.getUploadedFile().getOriginalFilename()));
				
				String resultPath = MissionMilletUtil.saveFile(wassanReportsModel.getUploadedFile().getBytes(), reportPathDir, reportFileName);
				
				//saving file path in WassanReportsFile table
				wassanReportsFile.setFilePath(resultPath);
				wassanReportsFile.setCollectUser(collectUser);
				wassanReportsFile.setStatus(new TypeDetails(Integer.valueOf(messageSourceNotification.getMessage("wassan.upload.report", null, null))));

				WASSANReportsFile saveFile = wassanReportsFileRepository.save(wassanReportsFile);
				
				if(saveFile!=null){
					LOGGER.info("Description : "+collectUserModel.getUsername()+ "("+ email +")" +"Action :"+ messageSourceNotification.getMessage
							("wassan.report.save.message", null,null)+ "for "+Month.of(month).name()+" : "+fullDateFormat.format(new Date()));
					return new ResponseEntity<>(messageSourceNotification.getMessage("soe.upload.success", null,null),HttpStatus.OK);
				}
				else
					return new ResponseEntity<>(messageSourceNotification.getMessage("upload.status.failed", null,null),HttpStatus.BAD_REQUEST); 
					
		} catch (ParseException e) {
			LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("+ email +")" +"Action :"+ messageSourceNotification.getMessage
					("upload.report.message.error", null,null)+"for "+wassanReportsModel.getMonth()+" : "+fullDateFormat.format(new Date()),e);
			throw new RuntimeException();
		} catch (IOException e) {
			LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("+ email +")" +"Action :"+ messageSourceNotification.getMessage
					("upload.report.message.error", null,null)+" : "+fullDateFormat.format(new Date()),e);
			throw new RuntimeException();
		}
	}

	/**
	 * @author subham (subham@sdrc.co.in) 
	 * 
	 * @Description This method fetch all the Report details uploaded by admin to display in history
	 * section, financial year wise
	 */
	@Override
	@Transactional
	public Map<String, List<WASSANReportsModel>> wassonReportHistoryDetails() {

		//fetching  all report records whose islive is true
		List<WASSANReports> wassanReportsList = wassanReportsRepository.findByIsLiveTrueOrderByYearDesc();

		String fCheck = StateUserNgoWorkSpaceServiceImpl.financialYearCheck();

		if (wassanReportsList != null && !wassanReportsList.isEmpty()) {

			List<Integer> reportIdList = new ArrayList<Integer>();

			// saving all Report id in list
			for (WASSANReports wassanReports : wassanReportsList) {

				reportIdList.add(wassanReports.getReportsId());
			}

			// getting list of file here
			List<WASSANReportsFile> wassanReportFileList = wassanReportsFileRepository.findByReportId(reportIdList);

			List<WASSANReportsFile> filelist = new ArrayList<WASSANReportsFile>();

			// putting it into map making reportId as key
			Map<Integer, List<WASSANReportsFile>> fileMap = new LinkedHashMap<Integer, List<WASSANReportsFile>>();

			for (WASSANReportsFile file : wassanReportFileList) {

				if (fileMap.containsKey(file.getWassanReports().getReportsId())) {

					filelist.add(file);

				} else {

					filelist = new ArrayList<WASSANReportsFile>();
					filelist.add(file);
					fileMap.put(file.getWassanReports().getReportsId(), filelist);
				}

			}

			List<WASSANReportsModel> wassanReportsModelsList = new ArrayList<WASSANReportsModel>();

			//fetching all the monthly timeperiod value form database
			List<TimePeriod> timePeriodList = timePeriodRepository.findByPeriodicity(messageSourceNotification.getMessage("timeperiod.periodicity.monthly", null, null));

			Map<String, TimePeriod> timePeriodMap = new LinkedHashMap<String, TimePeriod>();

			//making year and month as a key to identify which timeperiod value should be used 
			for (TimePeriod timePeriod : timePeriodList) {

				Integer year = timePeriod.getYear();

				if (timePeriod.getTimePeriod().equals("Jan") || timePeriod.getTimePeriod().equals("Feb")
						|| timePeriod.getTimePeriod().equals("Mar")) {

					year = year - 1;

				}

				timePeriodMap.put(year + "_" + timePeriod.getTimePeriod(), timePeriod);
			}

			Map<String, List<WASSANReportsModel>> map = new LinkedHashMap<String, List<WASSANReportsModel>>();
			
			//iterating loop to set WASSANReportsModel values
			for (WASSANReports wassanReports : wassanReportsList) {

				WASSANReportsModel wassanReportsModel = new WASSANReportsModel();

				wassanReportsModel.setReportsId(wassanReports.getReportsId() != null ? wassanReports.getReportsId() : null);
				wassanReportsModel.setMonth(Month.of(wassanReports.getMonth()).name());

				wassanReportsModel.setYear(String.valueOf(wassanReports.getYear()) != null? String.valueOf(wassanReports.getYear()) : null);

				wassanReportsModel.setCreatedDate(newFormat.format(wassanReports.getCreatedDate()));
				wassanReportsModel.setRemarks(wassanReports.getRemarks() != null ? wassanReports.getRemarks() : null);

				wassanReportsModel.setReportType(wassanReports.getReportType().getTypeDetailName());
				wassanReportsModel.setReportName(wassanReports.getReportName());

				List<WASSANReportsFile> newfileList = fileMap.get(wassanReports.getReportsId());

				for (WASSANReportsFile value : newfileList) {

					if (value.getStatus().getTypeDetailId() == Integer.valueOf(messageSourceNotification.getMessage("wassan.upload.report", null, null))) {

						wassanReportsModel.setDownloadUploadReport(value.getStatus().getTypeDetailId());

				}

				Month month = Month.of(wassanReports.getMonth());
				month.getDisplayName(TextStyle.SHORT, Locale.UK);

				TimePeriod timePeriod = timePeriodMap.get(wassanReports.getYear() + "_" + month.getDisplayName(TextStyle.SHORT, Locale.UK));

				wassanReportsModel.setFinancialYear(timePeriod.getFinancialYear() != null ? timePeriod.getFinancialYear() : null);

				//only current financial year report can be deleted so checking which record lies in current financial year
				//and udating its check value to true
				if (fCheck.equals(timePeriod.getFinancialYear())) {

					wassanReportsModel.setCheck(true);
				} else
					wassanReportsModel.setCheck(false);

					wassanReportsModelsList.add(wassanReportsModel);
				}

			}
			
			List<WASSANReportsModel> list = null;

			//making financial year as key
			for (int i = 0; i < wassanReportsModelsList.size(); i++) {

				if (map.containsKey(wassanReportsModelsList.get(i).getFinancialYear())) {

					list = map.get(wassanReportsModelsList.get(i).getFinancialYear());
					list.add(wassanReportsModelsList.get(i));
					map.put(wassanReportsModelsList.get(i).getFinancialYear(), list);

				} else {

					list = new ArrayList<WASSANReportsModel>();
					list.add(wassanReportsModelsList.get(i));
					map.put(wassanReportsModelsList.get(i).getFinancialYear(), list);

				}
			}
			
			return map;
		}
		return null;
	}

	/**
	 * @author Subham Ashish (subham@sdrc.co.in)
	 * 
	 * @Description This method populates the ReportType and Month dropdown list in report section of admin 
	 *  
	 */
	@Override
	@Transactional
	public Map<String, List<StateDropDownModel>> dropDownList() {

		List<TypeDetails> typeDetailsList = typeDetailsRepository.findByTypeTypeId(Integer.parseInt(messageSourceNotification.getMessage("type", null,null)));

		List<TypeDetailsModel> typeDetailsModelList = new ArrayList<TypeDetailsModel>();

		Map<String, List<StateDropDownModel>> map = new LinkedHashMap<String, List<StateDropDownModel>>();

		//it populates report type
		if (typeDetailsList != null) {

			for (TypeDetails details : typeDetailsList) {

				TypeDetailsModel typeDetailsModel = new TypeDetailsModel();

				typeDetailsModel.setCreatedDate(details.getCreatedDate() != null ? details.getCreatedDate() : null);
				typeDetailsModel.setTypeDetailName(details.getTypeDetailName() != null ? details.getTypeDetailName() : null);
				typeDetailsModel.setTypeDetailId(details.getTypeDetailId() != null ? details.getTypeDetailId() : null);
				typeDetailsModel.setCreatedBy(details.getCreatedBy() != null ? details.getCreatedBy() : null);
				typeDetailsModel.setTypeId((Integer) details.getType().getTypeId() != null ? details.getType().getTypeId() : null);
				typeDetailsModelList.add(typeDetailsModel);
			}
		}

		Calendar cal = Calendar.getInstance();
		Date startDate = new Date();
		Date endDate = new Date();

		int year;

		year = cal.get(Calendar.YEAR);

		if (cal.get(Calendar.MONTH) <= 2) {
			year = year - 1;
		}

		List<TimePeriod> timePeriodValue = timePeriodRepository.findByPeriodicityOrderByFinancialYearDesc(
				messageSourceNotification.getMessage("timeperiod.periodicity.yearly", null, null));

		List<StateDropDownModel> dropDownList = null;

		for (TimePeriod timePeriod : timePeriodValue) {

			startDate = timePeriod.getStartDate();

			endDate = timePeriod.getEndDate();

			try {
				Timestamp startDateTimestamp = new Timestamp(
						fullDateFormat.parse(dateTimeFormat.format(startDate.getTime()) + " 00:00:00").getTime());

				Timestamp endDateTimestamp = new Timestamp(
						fullDateFormat.parse(dateTimeFormat.format(endDate.getTime()) + " 00:00:00").getTime());

				List<String> monthList = new ArrayList<String>();

				// getting data from wassanSoe table, for one financial year
				List<WASSANSoEReport> wassanSoeReportList = wassanSoeReportRepository.findDataByFinancialYear(startDateTimestamp, endDateTimestamp);

				if (wassanSoeReportList != null) {
					for (WASSANSoEReport report : wassanSoeReportList) {

						Month month = Month.of(report.getMonth());
						monthList.add(month.getDisplayName(TextStyle.SHORT, Locale.UK));

					}
				}

				StateDropDownModel model = new StateDropDownModel();

				//making financial year as a key
				if (map.containsKey(timePeriod.getFinancialYear())) {

					dropDownList.add(model);

				} else {

					dropDownList = new ArrayList<StateDropDownModel>();

					model.setTypeDetailsModel(typeDetailsModelList);
					model.setMonthDetails(monthList);
					dropDownList.add(model);

					map.put(timePeriod.getFinancialYear(), dropDownList);
				}

			} catch (ParseException e) {
				LOGGER.error("Error description :" +"Action :"+ messageSourceNotification.getMessage
						("dropdown.value.error", null,null)+" : "+fullDateFormat.format(new Date()),e);
				throw new RuntimeException();
			}
		}
		return map;

	}

	/**
	 * @author Subham Ashish (subham@sdrc.co.in) 
	 * 
	 * @Description This method deletes the report uploaded by admin by setting isLive value as false
	 * 
	 * @param wassanReportId
	 */

	@Override
	@Transactional
	public  ResponseEntity<String> deleteReport(Integer wassanReportId) {
		
		CollectUserModel collectUserModel = (CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		if (collectUserModel == null){
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null)+" tries to access deleteReport() "
					+ "method : "+fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		
		String email = collectUserModel.getEmailId();

		//setting isLive value false in WASSANReports table
		WASSANReports wassanReports = wassanReportsRepository.findByReportsId(wassanReportId);
		wassanReports.setLive(false);
		WASSANReports newwassanReports = wassanReportsRepository.save(wassanReports);

		//setting isLive value false in WASSANReportsFile table
		List<WASSANReportsFile> wassanReportFileList = wassanReportsFileRepository.findByWassanReports(wassanReports);

		for (WASSANReportsFile file : wassanReportFileList) {

			file.setLive(false);
		}
		
		Iterable<WASSANReportsFile> newWassanReportFile = wassanReportsFileRepository.save(wassanReportFileList);

		if (newWassanReportFile != null && newwassanReports != null) {
			
			LOGGER.info("Description : "+collectUserModel.getUsername()+ "("+ email +")"+"Action :"+ messageSourceNotification.getMessage
					("report.delete.success", null,null)+ "for "+Month.of(wassanReports.getMonth()).name()+" : "+fullDateFormat.format(new Date()));
			return new ResponseEntity<>(messageSourceNotification.getMessage("delete.report.message", null,null),HttpStatus.OK);

		}
		LOGGER.error("Description : "+collectUserModel.getUsername()+ "("+ email +")"+"Action :"+ messageSourceNotification.getMessage
				("report.delete.error", null,null)+ "for "+Month.of(wassanReports.getMonth()).name()+" : "+fullDateFormat.format(new Date()));
		return new ResponseEntity<>(messageSourceNotification.getMessage("file.not.founds", null,null),HttpStatus.BAD_REQUEST);

	}

	/**
	 * @author Subham Ashish (subham@sdrc.co.in) 
	 * 
	 * @Description Returns the file path which is to be downloaded in history section of Report
	 * 
	 * @param reportId 
	 * @param typeId
	 * 
	 */

	@Override
	@Transactional
	public String wassanReportDownload(Integer reportId, Integer typeId) {
		
		CollectUserModel collectUserModel = (CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		
		if (collectUserModel == null){
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null)+messageSourceNotification.getMessage("download.report.message.error", null,null)
					+ fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		
		String email = collectUserModel.getEmailId();
		
		WASSANReportsFile wassanReportFile = wassanReportsFileRepository.getFile(reportId, typeId);

		String path = MissionMilletUtil.getPdfFile(wassanReportFile.getFilePath(),
				messageSourceNotification.getMessage("wassan.upload.report.path", null, null));

		if (path != null) {
			
			
			LOGGER.info("Description : "+collectUserModel.getUsername()+ "("+ email +")"+"Action :"+ messageSourceNotification.getMessage
					("download.report.message.success", null,null)+ "for "+Month.of(wassanReportFile.getWassanReports().getMonth()).name()+" : "+fullDateFormat.format(new Date()));
			return path.toString();
		}
		LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("+ email +")"+"Action :"+ messageSourceNotification.getMessage
				("download.report.message.error", null,null)+ "for "+Month.of(wassanReportFile.getWassanReports().getMonth()).name()+" : "+fullDateFormat.format(new Date()));
		return messageSourceNotification.getMessage("file.not.founds", null, null);
	}

	/**
	 * @author Subrata
	 * 
	 * @Description Fetching periodicity for aggregation.
	 * 
	 * @return listOfPeriodicity
	 */

	@Override
	public List<ValueObject> getPeriodicity() {

		List<TypeDetails> getPeriodicity = typeDetailsRepository.findByTypeTypeId(Integer.valueOf(messageSourceNotification.
				getMessage("state.periodicity", null, null)));//for periodicity 1

		List<ValueObject> listOfPeriodicity = new ArrayList<ValueObject>();
		for (TypeDetails typeDetails : getPeriodicity) {

			ValueObject valueObject = new ValueObject();
			valueObject.setKey(typeDetails.getTypeDetailId().toString());
			valueObject.setDescription(typeDetails.getTypeDetailName());

			listOfPeriodicity.add(valueObject);
		}
		return listOfPeriodicity;
	}

	/**
	 * @author Subrata
	 * 
	 * @Description Fetching type of configuration for state level user.
	 * 
	 * @return listOfConfigurationType
	 */
	@Override
	public List<ValueObject> getTypeOfConfiguration() {
		List<TypeDetails> getListOfConfigurationType = typeDetailsRepository.findByTypeTypeId(
				Integer.valueOf(messageSourceNotification.getMessage("state.configuration", null, null))); 

		List<Configuration> listOfCongif = configurationRepository
				.getAllIsLiveTypeData(new ArrayList<Integer>(Arrays.asList(39, 38)));
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (Configuration configuration : listOfCongif) {

			map.put(Integer.valueOf(configuration.getTypeDetails().getTypeDetailId()),
					Integer.valueOf(configuration.getNgoCutOffDays()));

		}

		List<ValueObject> listOfConfigurationType = new ArrayList<ValueObject>();
		for (TypeDetails typeDetails : getListOfConfigurationType) {
			if(typeDetails.getTypeDetailId() != 39){
				ValueObject valueObject = new ValueObject();
				valueObject.setKey(typeDetails.getTypeDetailId().toString());
				valueObject.setDescription(typeDetails.getTypeDetailName());
				valueObject.setGroupName(map.get(Integer.valueOf(typeDetails.getTypeDetailId().toString())) != null
						? (map.get(Integer.valueOf(typeDetails.getTypeDetailId().toString()))).toString() : null);
				listOfConfigurationType.add(valueObject);
			}
		}
		return listOfConfigurationType;
	}

	/**
	 * @author Subrata
	 * 
	 * @Description Fetching the time periods for aggregation(i.e. monthly, quarterly,yearly).
	 * 
	 * @return mapOfTp
	 */

	@Override
	public Map<String, Map<String, List<ValueObject>>> getTypeOfAggregation() {

		Calendar cal = Calendar.getInstance();
		Timestamp endDate = new Timestamp(cal.getTime().getTime());
		cal.add(Calendar.YEAR, 0);
		Map<String, Map<String, List<ValueObject>>> map = new HashMap<String, Map<String, List<ValueObject>>>();
		Map<String, List<ValueObject>> mapOfTp = null; 
		List<ValueObject> listOfTPs = null;
		String monthly = messageSourceNotification.getMessage("timeperiod.periodicity.monthly", null, null);
		String quarterly = messageSourceNotification.getMessage("timeperiod.periodicity.quarterly", null, null);
		String yearly = messageSourceNotification.getMessage("timeperiod.periodicity.yearly", null, null);

		List<TimePeriod> listTimePeriods = timePeriodRepository.getAllTimePeriods(endDate,
				Arrays.asList(monthly, quarterly, yearly));

		List<TimePeriod> listTimePeriodsAggregated = aggregationRepository.getAllTimePeriodsAggregated();

		for (TimePeriod timePeriod : listTimePeriods) {
			mapOfTp = null;
			if (map.containsKey(timePeriod.getFinancialYear())) {
				mapOfTp = map.get(timePeriod.getFinancialYear());
				setMonthData(map, mapOfTp, listOfTPs, listTimePeriodsAggregated, timePeriod, monthly, quarterly,
						yearly);
			} else {
				mapOfTp = new HashMap<String, List<ValueObject>>();
				mapOfTp = setMonthData(map, mapOfTp, listOfTPs, listTimePeriodsAggregated, timePeriod, monthly,
						quarterly, yearly);
				map.put(timePeriod.getFinancialYear(), mapOfTp);
			}
		}
		return map;
	}

	private Map<String, List<ValueObject>> setMonthData(Map<String, Map<String, List<ValueObject>>> map,
			Map<String, List<ValueObject>> mapOfTp, List<ValueObject> listOfTPs,
			List<TimePeriod> listTimePeriodsAggregated, TimePeriod timePeriod, String monthly, String quarterly,
			String yearly) {

		if (Integer.valueOf(timePeriod.getPeriodicity()) == Integer.valueOf(monthly)) { // i.e
																						// 1
			if (!mapOfTp.containsKey("monthly")) {
				listOfTPs = new ArrayList<ValueObject>();
				listOfTPs.add(getMonthlyAggregatedTp(timePeriod, listTimePeriodsAggregated));
				mapOfTp.put("monthly", listOfTPs);
			} else {
				mapOfTp.get("monthly").add(getMonthlyAggregatedTp(timePeriod, listTimePeriodsAggregated));
			}
		} else if (Integer.valueOf(timePeriod.getPeriodicity()) == Integer.valueOf(quarterly)) { // i.e
																									// 3
			if (!mapOfTp.containsKey("quarterly")) {
				listOfTPs = new ArrayList<ValueObject>();
				listOfTPs.add(getMonthlyAggregatedTp(timePeriod, listTimePeriodsAggregated));
				mapOfTp.put("quarterly", listOfTPs);
			} else {
				mapOfTp.get("quarterly").add(getMonthlyAggregatedTp(timePeriod, listTimePeriodsAggregated));
			}
		} else if (Integer.valueOf(timePeriod.getPeriodicity()) == Integer.valueOf(yearly)) { // i.e.
																								// 12
			if (!mapOfTp.containsKey(("yearly"))) {
				listOfTPs = new ArrayList<ValueObject>();
				listOfTPs.add(getMonthlyAggregatedTp(timePeriod, listTimePeriodsAggregated));
				mapOfTp.put("yearly", listOfTPs);
			} else {
				mapOfTp.get("yearly").add(getMonthlyAggregatedTp(timePeriod, listTimePeriodsAggregated));
			}
		}
		return mapOfTp;
	}

	private ValueObject getMonthlyAggregatedTp(TimePeriod timePeriod, List<TimePeriod> listTimePeriodsAggregated) {

		ValueObject valueObject = new ValueObject();
		valueObject.setKey(timePeriod.getTimePeriodId().toString());
		valueObject.setShortNmae(timePeriod.getTimePeriod());
		valueObject.setDescription(timePeriod.getPeriodicity());
		valueObject.setGroupName(timePeriod.getFinancialYear());
		valueObject.setIsSelected(listTimePeriodsAggregated.contains(timePeriod.getTimePeriodId()) ? true : false);

		return valueObject;
	}

	/**
	 * @author Subrata 
	 * 
	 * @Description State level user set the cut-off days for NGO level user .
	 * 
	 * @param cutOffDays
	 * @param typeDetailsId
	 */

	@Transactional
	@Override
	public String setConfiguration(Integer cutOffDays, Integer typeDetailsId) {
		
		Integer lastCutOffDays = Integer
				.valueOf(messageSourceNotification.getMessage("config.lastCutOffDays", null, null));

		try {
			Configuration configuration = configurationRepository
					.findByTypeDetailsTypeDetailIdAndIsLiveTrue(typeDetailsId);
			Integer day = null;

			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			day = cal.get(Calendar.DAY_OF_MONTH);

			if (configuration != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Timestamp startDate = configuration.getCreatedDate();
				int yearInDb = Integer.valueOf(sdf.format(startDate.getTime()));
				int monthInDb = configuration.getCreatedDate().getMonth();
				if (year == yearInDb && month == monthInDb)
					return "alreadyConfigured";
				else
					configuration.setLive(false);
			}

			configuration = new Configuration();
			configuration.setLive(true);
			configuration.setNgoCutOffDays(cutOffDays);
			configuration.setTypeDetails(new TypeDetails(typeDetailsId));

			configurationRepository.save(configuration);
			return (day <= lastCutOffDays ? "success" : "fromNextMonth");
		} catch (Exception e) {
			LOGGER.error("Error description : " + " : Action :"+ messageSourceNotification.getMessage
					("failed", null,null)+" : "+fullDateFormat.format(new Date()),e);
			return messageSourceNotification.getMessage("failed", null,null);
		}
	}

}
