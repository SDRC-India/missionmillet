package org.sdrc.missionmillet.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.sdrc.missionmillet.domain.CollectUser;
import org.sdrc.missionmillet.domain.Configuration;
import org.sdrc.missionmillet.domain.NGO;
import org.sdrc.missionmillet.domain.NGOSoEReport;
import org.sdrc.missionmillet.domain.NGOSoEUploadsStatus;
import org.sdrc.missionmillet.domain.NgoReports;
import org.sdrc.missionmillet.domain.NgoReportsFile;
import org.sdrc.missionmillet.domain.SoETransaction;
import org.sdrc.missionmillet.domain.TimePeriod;
import org.sdrc.missionmillet.domain.TypeDetails;
import org.sdrc.missionmillet.domain.UUIdGenerator;
import org.sdrc.missionmillet.model.CollectUserModel;
import org.sdrc.missionmillet.model.DropdownModel;
import org.sdrc.missionmillet.model.NGOSoEReportModel;
import org.sdrc.missionmillet.model.NGOSoEUploadsStatusModel;
import org.sdrc.missionmillet.model.NGOSoEViewModel;
import org.sdrc.missionmillet.model.NgoReportsModel;
import org.sdrc.missionmillet.model.TypeDetailsModel;
import org.sdrc.missionmillet.model.ValueObject;
import org.sdrc.missionmillet.repository.CollectUserRepository;
import org.sdrc.missionmillet.repository.ConfigurationRepository;
import org.sdrc.missionmillet.repository.NGORepository;
import org.sdrc.missionmillet.repository.NGOSoEReportRepository;
import org.sdrc.missionmillet.repository.NGOSoEUploadsStatusRepository;
import org.sdrc.missionmillet.repository.NgoReportsFileRepository;
import org.sdrc.missionmillet.repository.NgoReportsRepository;
import org.sdrc.missionmillet.repository.SoETemplateRepository;
import org.sdrc.missionmillet.repository.SoETransactionRepository;
import org.sdrc.missionmillet.repository.TimePeriodRepository;
import org.sdrc.missionmillet.repository.TypeDetailsRepository;
import org.sdrc.missionmillet.repository.UUIdGeneratorRepository;
import org.sdrc.missionmillet.util.Constants;
import org.sdrc.missionmillet.util.LockWorkbook;
import org.sdrc.missionmillet.util.MissionMilletUtil;
import org.sdrc.missionmillet.util.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Abhisheka Mishra
 * 
 * @Description This is the Implementation class of NgoReportsService interface.
 */
@Service
public class NgoReportsServiceImpl implements NgoReportsService {

	@Autowired
	private NgoReportsRepository ngoReportsRepository;
	
	@Autowired
	private NgoReportsFileRepository ngoReportsFileRepository;
	
	@Autowired
	private NGOSoEReportRepository ngoSoEReportRepository;
	
	@Autowired
	private TypeDetailsRepository typeDetailsRepository;
	
	@Autowired
	private TimePeriodRepository timePeriodRepository;
	
	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;
	
	@Autowired
	private NGORepository ngoRepository;
	
	@Autowired
	private NGOSoEUploadsStatusRepository ngoSoEUploadsStatusRepository;
	
	@Autowired
	private ConfigurationRepository configurationRepository;
	
	@Autowired
	private SoETransactionRepository soETransactionRepository;
	
	@Autowired
	private CollectUserRepository collectUserRepository;
	
	@Autowired
	private SoETemplateRepository soETemplateRepository;
	
	@Autowired
	private UUIdGeneratorRepository uuIdGeneratorRepository;
	
	@Autowired
	private StateManager stateManager;
	
	@Autowired
	private StateUserService stateUserService;
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat timestampFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
	private SimpleDateFormat dateFormatNew = new SimpleDateFormat("MMM");
	private SimpleDateFormat monthFormats = new SimpleDateFormat("MMMM");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final Logger	LOGGER	= LoggerFactory.getLogger( "LOGGER" );
	
	/**
	 * @Description While uploading a report file, this method saves metadata in NgoReports table and 
	 * the file in NgoReportsFile table for a particular NGO user. 
	 *  
	 * @author Abhisheka Mishra
	 *  
	 * @Param: NgoReportsModel
	 * @throws Exception
	 */
	@Override
	public String upload(NgoReportsModel ngoReportsModel) throws Exception {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel)stateManager.getValue(Constants.USER_PRINCIPAL));
		
		if(null==collectUserModel){
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null)+" "+fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		String email = collectUserModel.getEmailId()==null?"":collectUserModel.getEmailId();
		CollectUser collectUser = new CollectUser();
		collectUser.setUserId(collectUserModel.getUserId());
		NGO ngo = ngoRepository.findById(Integer.valueOf(collectUserModel.getNgo().getId()));
		
		int ngoReport = Integer.valueOf(messageSourceNotification.getMessage("ngo.upload.report", null, null));
		String fileDir = messageSourceNotification.getMessage("ngo.upload.dir", null, null);
		
		TimePeriod period = timePeriodRepository.findByTimePeriodId(Integer.valueOf(ngoReportsModel.getMonth()));
		
		String result = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(period.getStartDate());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		
		List<NGOSoEReport> ngoSoEReport = ngoSoEReportRepository.findByCollectUserAndNgoAndMonthAndYear(collectUser, ngo, month, year);
		// Checking SoE file available or not 
		if(ngoSoEReport.size() > 0 ){ 
			NgoReports ngoReports = new NgoReports();	
			ngoReports.setYear(period != null ? period.getYear() : null);
			ngoReports.setMonth(period != null ? month : null);
			ngoReports.setCreatedDate(new Timestamp(new Date().getTime()));
			ngoReports.setRemarks(ngoReportsModel.getRemarks() != null ? ngoReportsModel.getRemarks() : null);
			ngoReports.setReportType(ngoReportsModel.getReportType() != null ? new TypeDetails(Integer.valueOf(ngoReportsModel.getReportType())) : null );
			ngoReports.setCollectUser(collectUser);
			ngoReports.setNgo(ngo);
			ngoReports.setLive(true);
			// saving the reports in NgoReports table.
			NgoReports reports = ngoReportsRepository.save(ngoReports);
			
			NgoReportsFile ngoReportsFile = null; 
			String filePath = null;
			List<NgoReportsFile> files = new ArrayList<NgoReportsFile>();
			ngoReportsFile = new NgoReportsFile();
			ngoReportsFile.setNgoReports(reports);
			// saveFile is called to save the file and return the file path
			filePath = MissionMilletUtil.saveFile(ngoReportsModel.getUploadedFile().getBytes(), 
					fileDir+ngo.getName()+"/"+year+"/"+monthFormats.format(period.getStartDate())+"/",
					FilenameUtils.removeExtension(ngoReportsModel.getUploadedFile().getOriginalFilename())+ "_"
							+ new SimpleDateFormat("ddMMyyyyHHmmssSSSSS").format(new java.util.Date()) + "."
							+ (FilenameUtils.getExtension(ngoReportsModel.getUploadedFile().getOriginalFilename())));
			ngoReportsFile.setFilePath(filePath);
			ngoReportsFile.setLive(true);
			ngoReportsFile.setStatus(new TypeDetails(ngoReport));
			ngoReportsFile.setCollectUser(collectUser);
			
			files.add(ngoReportsFile);
			// Saving the file in NgoReportsFile table
			ngoReportsFileRepository.save(files);
			LOGGER.info("Description : "+collectUserModel.getUsername()+ "("
					+ email +")" +" : Action : "+ 
					messageSourceNotification.getMessage("file.upload.success", null,null)+" for "+
					new DateFormatSymbols().getMonths()[month] +" and year "+ year +" : "+fullDateFormat.format(new Date()));
			
			result = messageSourceNotification.getMessage("upload.status.success",null, null);
		}else{
			result = messageSourceNotification.getMessage("no.data.available.for.this.month",null, null);
			
			LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("
					+ email +")" +" : Action :"+ 
					messageSourceNotification.getMessage("no.data.available.for.this.month", null,null)+" for "
							+ new DateFormatSymbols().getMonths()[month] +" : "+fullDateFormat.format(new Date()));
		}
		return result;
	}

	/**
	 * @Description Returns all the report file for a NGO to be displayed in the history
	 * 
	 * @author Abhisheka Mishra
	 *   
	 */
	public Map<String, List<NgoReportsModel>> getNgoReportsDetails() {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel)stateManager.getValue(Constants.USER_PRINCIPAL));
		if(null==collectUserModel){
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null)+" "+fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		
		CollectUser collectUser = new CollectUser();
		collectUser.setUserId(collectUserModel.getUserId());
		
		List<NgoReports> ngoReports = ngoReportsRepository.findByCollectUserAndIsLiveTrue(collectUser);
		List<NgoReportsFile> ngoReportsFiles = ngoReportsFileRepository.findAll();
		List<NgoReportsModel> list = new ArrayList<NgoReportsModel>();
		Calendar cal = Calendar.getInstance();
		int ngoReport = Integer.valueOf(messageSourceNotification.getMessage("ngo.upload.report", null, null));
		String monthlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.monthly", null, null);
		List<TimePeriod> timePeriods = timePeriodRepository.findByPeriodicity(monthlyPeriodicity);
		
		//Iterating ngoReports list
		for(int i = 0 ; i < ngoReports.size(); i ++){
			NgoReportsModel ngoReportsModel = new NgoReportsModel();
			
			ngoReportsModel.setCreatedDate(ngoReports.get(i).getCreatedDate() != null ? simpleDateFormat.format(ngoReports.get(i).getCreatedDate()) : null);
			if((Integer)ngoReports.get(i).getMonth() != null){
				cal.set(Calendar.MONTH,ngoReports.get(i).getMonth());
			}
			ngoReportsModel.setMonth((Integer)ngoReports.get(i).getMonth() != null ? monthFormats.format(cal.getTime()).toUpperCase() : null);
			ngoReportsModel.setReportsId(ngoReports.get(i).getReportsId() != null ? ngoReports.get(i).getReportsId() : null);
			ngoReportsModel.setYear((Integer)ngoReports.get(i).getYear() != null ? ngoReports.get(i).getYear() : null);
			ngoReportsModel.setRemarks(ngoReports.get(i).getRemarks() != null ? ngoReports.get(i).getRemarks() : null);
			ngoReportsModel.setReportTypeId(ngoReports.get(i).getReportType().getTypeDetailId() != null ? ngoReports.get(i).getReportType().getTypeDetailId() : null);
			ngoReportsModel.setReportType(ngoReports.get(i).getReportType() != null ? ngoReports.get(i).getReportType().getTypeDetailName() : null);
			
			List<NgoReportsFile> ngoReportsFileValue= NgoReportsServiceImpl.getReportTypeDetails(ngoReportsFiles,ngoReports.get(i).getReportsId())
					!= null ? NgoReportsServiceImpl.getReportTypeDetails(ngoReportsFiles,ngoReports.get(i).getReportsId()) : null;
			
			if (ngoReportsFileValue != null) {
				for (int j = 0; j < ngoReportsFileValue.size(); j++) {
					if (ngoReportsFileValue.get(j).getStatus().getTypeDetailId() == ngoReport) {	
						ngoReportsModel.setDownloadUploadedFile(ngoReportsFileValue.get(j).getStatus().getTypeDetailId());
					} 
				}
			}
			
			TimePeriod timePeriod = getTimePeriodByMonthAndYear(timePeriods,dateFormatNew.format(cal.getTime()), ngoReports.get(i).getYear());
			ngoReportsModel.setFinancialYear(timePeriod != null ? timePeriod.getFinancialYear() : null);
			
			list.add(ngoReportsModel);
		}
		// returns NgoReports table data according to the financial year
		List<NgoReportsModel> reportsModels = null;
		Map<String, List<NgoReportsModel>> map = new TreeMap<String, List<NgoReportsModel>>(Collections.reverseOrder());
		for (int i = 0; i < list.size(); i++) {
			if(map.containsKey(list.get(i).getFinancialYear())){
				reportsModels = map.get(list.get(i).getFinancialYear());
				reportsModels.add(list.get(i));
				map.put(list.get(i).getFinancialYear(), reportsModels);
			}else{
				reportsModels = new ArrayList<NgoReportsModel>();
				reportsModels.add(list.get(i));
				map.put(list.get(i).getFinancialYear(), reportsModels);
			}
		}	
			
		return map;
	}

	/**
	 * @Description Download a report for an NGO 
	 *  
	 * @author Abhisheka Mishra
	 * 
	 * @param reportId
	 * @param typeId
	 * @param reportTypeId
	 * 
	 */
	@Override
	@Transactional(readOnly=true)
	public String downloadReportData(int reportId, int typeId,int reportTypeId) throws Exception {
		// Getting the logged in user credentials and if not found throw an error.
		CollectUserModel collectUserModel = ((CollectUserModel)stateManager.getValue(Constants.USER_PRINCIPAL));

		if(null==collectUserModel){
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null)+" "+fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		
		NgoReportsFile ngoReportsFile = ngoReportsFileRepository.getReportFile(
				reportId, typeId,reportTypeId,collectUserModel.getUserId());
		
		return MissionMilletUtil.getPdfFile(ngoReportsFile.getFilePath(), 
				messageSourceNotification.getMessage("ngo.upload.report.path", null, null));
	}
	
	/**
	 * @Description Save the SoE template metadata NGOSoEReport table and the file in NgoReportsFile table. 
	 * 
	 * @author Abhisheka Mishra(abhisheka@sdrc.co.in)
	 * @param ngoSoEReportModel
	 * 
	 */
	@Override
	@Transactional
	public String uploadSoe(NGOSoEReportModel ngoSoEReportModel) throws Exception {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel)stateManager.getValue(Constants.USER_PRINCIPAL));
		if(null==collectUserModel){
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null)+" "+fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		String email = collectUserModel.getEmailId()==null?"":collectUserModel.getEmailId();
		if (!FilenameUtils.getExtension(ngoSoEReportModel.getSoeFile()[0].getOriginalFilename()).equals("xls")) {
			
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
                    + " : Action :"+ messageSourceNotification.getMessage("upload.anexcel.file", null, null) + " : "
		            + fullDateFormat.format(new Date()));
			return messageSourceNotification.getMessage("upload.anexcel.file", null, null)
					+ fullDateFormat.format(new Date());
		}
		HSSFWorkbook workbook = new HSSFWorkbook(MissionMilletUtil.getByteArrayStream(ngoSoEReportModel.getSoeFile()[0].getBytes()));
		HSSFSheet sheet = null;
		
		sheet = workbook.getSheet("Detailed SOE");
		if(sheet==null) {
			workbook.close();
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
                    + " : Action :"+ messageSourceNotification.getMessage("excel.sheet.not.matching", null, null) + " : "
		            + fullDateFormat.format(new Date()));
			return messageSourceNotification.getMessage("excel.sheet.not.matching", null, null);
		}
		sheet =null;
		sheet = workbook.getSheet("uuid");
		if(sheet==null) {
			workbook.close();
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
                    + " : Action :"+ messageSourceNotification.getMessage("excel.sheet.not.matching", null, null) + " : "
		            + fullDateFormat.format(new Date()));
			return messageSourceNotification.getMessage("excel.sheet.not.matching", null, null);
		}
		sheet =null;
		sheet = workbook.getSheet("mm_check");
		if(sheet==null) {
			workbook.close();
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
                    + " : Action :"+ messageSourceNotification.getMessage("excel.sheet.not.matching", null, null) + " : "
		            + fullDateFormat.format(new Date()));
			return messageSourceNotification.getMessage("excel.sheet.not.matching", null, null);
		}
		Row row = sheet.getRow(0);
		Cell macroCell = row.getCell(0);
		/**
		 * Checking macro is enabled in the uploading excel or not
		 */
		if(macroCell.getCellType() == 3 || macroCell.getBooleanCellValue()==false){
			workbook.close();
			LOGGER.error(messageSourceNotification.getMessage("macro.not.enable", null, null)
								+" :  "+fullDateFormat.format(new Date()));
			return messageSourceNotification.getMessage("macro.not.enable", null, null);
		}else{
			macroCell.setCellValue(false);
			
			CollectUser collectUser = new CollectUser();
			collectUser.setUserId(collectUserModel.getUserId());
			NGO ngo = ngoRepository.findById(Integer.valueOf(collectUserModel.getNgo().getId()));

			int ngoSoeReport = Integer.valueOf(messageSourceNotification.getMessage("ngo.soe.report", null, null));
			int rejectCountLimit = Integer.valueOf(messageSourceNotification.getMessage("rejectcount.limit", null, null));
			// Getting the reject count value from database.
			Integer count = ngoSoEReportRepository.getRejectedValue(collectUserModel.getUserId(), ngo.getId(), 
					ngoSoEReportModel.getMonth(), ngoSoEReportModel.getYear());
			
			String yearlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.yearly", null, null);
			/**
			 * If the reject count is less than 2 then the NGO user can upload the SoE template 	
			 */
			String monthlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.monthly", null, null);
			int reUpload = Integer.valueOf(messageSourceNotification.getMessage("typedetails.re.upload", null, null));
			//Checking count is null or less than 2 
			if (count == null || count < rejectCountLimit) {
	
				sheet =null;
				sheet = workbook.getSheet("uuid");
				row = sheet.getRow(0);
				Row uuidRow = sheet.getRow(1);
	
				if (uuidRow == null){
					workbook.close();
					LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("
							+ email +")" +" : Action :"+ 
							messageSourceNotification.getMessage("excel.sheet.not.matching", null,null)+" : "+fullDateFormat.format(new Date()));
					
					return messageSourceNotification.getMessage("upload.correct.file", null, null);
				}
	
				Cell cell = row.getCell(0); 
				Cell uuidCell = uuidRow.getCell(0); 
				Cell biAnuualTpCell = row.getCell(1);	
	
				Timestamp currentDate = new Timestamp(simpleDateFormat.parse("01-" + (ngoSoEReportModel.getMonth() + 1) + "-"
											+ ngoSoEReportModel.getYear()).getTime());
				
				TimePeriod latestTimePeriod = timePeriodRepository.getTimePeriodofSixPeriodicity(monthlyPeriodicity,currentDate);
	
				TimePeriod timePeriod = timePeriodRepository
						.getTimePeriodData(currentDate,yearlyPeriodicity,latestTimePeriod.getFinancialYear());
				
				SoETransaction soETransaction = soETransactionRepository
						.getSoETemplateByMonthAndYear(ngo.getId(), cell.getStringCellValue(),
								timePeriod.getTimePeriodId(),(int)biAnuualTpCell.getNumericCellValue());
				
				UUIdGenerator uuIdGenerator = uuIdGeneratorRepository
						.getUUIDdetailsByMonthAndYear(collectUser.getUserId(), uuidCell.getStringCellValue(),
								ngoSoEReportModel.getMonth(), ngoSoEReportModel.getYear());
				workbook.close();
				// checking the UUID exists  or not 
				if (soETransaction != null && uuIdGenerator != null) {
	
					NGOSoEReport eReport = ngoSoEReportRepository
							.findByCollectUserAndNgoAndMonthAndYearAndIsLiveTrue(
									collectUser, ngo, ngoSoEReportModel.getMonth(),	ngoSoEReportModel.getYear());
					
					// if no record for the user in the Ngo_Soe_Report available then insert new record
					if (eReport == null) { 
						NGOSoEReport ngoSoEReport = new NGOSoEReport();
						ngoSoEReport.setCollectUser(collectUser);
						ngoSoEReport.setCreatedBy(collectUser.getUsername() != null ? collectUser.getUsername() : null);
						ngoSoEReport.setCreatedDate(new Timestamp(new Date().getTime()));
						ngoSoEReport.setMonth((Integer) ngoSoEReportModel.getMonth() != null ? ngoSoEReportModel.getMonth(): null);
						ngoSoEReport.setYear((Integer) ngoSoEReportModel.getYear() != null ? ngoSoEReportModel.getYear() : null);
						ngoSoEReport.setLive(true);
						ngoSoEReport.setRemarks(ngoSoEReportModel.getRemarks() != null ? ngoSoEReportModel.getRemarks() : null);
						ngoSoEReport.setNgo(ngo);
						NGOSoEUploadsStatus ngSoEUploadsStatus = ngoSoEUploadsStatusRepository
								.getTimePeriodByNGOMonthAndYear(ngo.getId(),ngoSoEReportModel
										.getMonth(),ngoSoEReportModel.getYear());
						int pending = Integer.valueOf(messageSourceNotification.getMessage("typedetails.pending", null, null));
						//update the status to pending, once ngo uploads the SoE
						ngSoEUploadsStatus.setStatus(new TypeDetails(pending));
						ngSoEUploadsStatus.setLastUploadedDate(new Timestamp(new Date().getTime()));
						ngoSoEReport.setnGOSoEUploadsStatus(ngSoEUploadsStatus);
						ngoSoEReport.setDeadlineDate(ngSoEUploadsStatus.getDeadlineDate());
						ngoSoEReport.setFirstDeadlineDate(ngSoEUploadsStatus.getDeadlineDate());
						ngoSoEReport.setLatest(true);
						ngoSoEReport.setTimePeriod(latestTimePeriod);
						ngoSoEUploadsStatusRepository.save(ngSoEUploadsStatus);
						NGOSoEReport soEReport = ngoSoEReportRepository.save(ngoSoEReport);
	
						NgoReportsFile ngoReportsFile = new NgoReportsFile();
						ngoReportsFile.setNgoSoeReport(soEReport);
						ngoReportsFile.setReportsFile(ngoSoEReportModel.getSoeFile()[0].getBytes());
						ngoReportsFile.setLive(true);
						ngoReportsFile.setStatus(new TypeDetails(ngoSoeReport));
						ngoReportsFile.setCollectUser(collectUser);
	
						ngoReportsFileRepository.save(ngoReportsFile);
						LOGGER.info("Description : "+collectUserModel.getUsername()+ "("
								+ email +")" +" : Action :"+ 
								messageSourceNotification.getMessage("file.upload.success", null,null)+" : for "
										+new DateFormatSymbols().getMonths()[ngoSoEReportModel.getMonth()]+" : "
										+fullDateFormat.format(new Date()));
						
						return "success";
					} else { 
						// If the SoE template was uploaded for that month, update isLive status to false and insert new record
						eReport.setLive(false);
						eReport.setLatest(false);
						eReport.setReEntryStatus(new TypeDetails(reUpload));
						NGOSoEReport ngoSoEReport = new NGOSoEReport();
						ngoSoEReport.setCollectUser(collectUser);
						ngoSoEReport.setCreatedBy(collectUser.getUsername() != null ? collectUser.getUsername() : null);
						ngoSoEReport.setCreatedDate(new Timestamp(new Date().getTime()));
						ngoSoEReport.setMonth((Integer) ngoSoEReportModel.getMonth() != null ? ngoSoEReportModel.getMonth(): null);
						ngoSoEReport.setYear((Integer) ngoSoEReportModel.getYear() != null ? ngoSoEReportModel.getYear() : null);
						ngoSoEReport.setLive(true);
						ngoSoEReport.setRemarks(ngoSoEReportModel.getRemarks() != null ? ngoSoEReportModel.getRemarks() : null);
						ngoSoEReport.setNgo(ngo);
						ngoSoEReport.setReEntryStatus(new TypeDetails(reUpload));
						NGOSoEUploadsStatus ngSoEUploadsStatus = ngoSoEUploadsStatusRepository
								.getTimePeriodByNGOMonthAndYear(ngo.getId(),ngoSoEReportModel.getMonth(), ngoSoEReportModel.getYear());
						ngSoEUploadsStatus.setLastUploadedDate(new Timestamp(new Date().getTime()));
						ngoSoEUploadsStatusRepository.save(ngSoEUploadsStatus);
						ngoSoEReport.setnGOSoEUploadsStatus(ngSoEUploadsStatus);
						ngoSoEReport.setDeadlineDate(ngSoEUploadsStatus.getDeadlineDate());
						ngoSoEReport.setFirstDeadlineDate(ngSoEUploadsStatus.getDeadlineDate());
						ngoSoEReport.setLatest(true);
						ngoSoEReport.setTimePeriod(latestTimePeriod);
						NGOSoEReport soEReport = ngoSoEReportRepository.save(ngoSoEReport);
	
						NgoReportsFile ngoReportsFile = new NgoReportsFile();
						ngoReportsFile.setNgoSoeReport(soEReport);
						ngoReportsFile.setReportsFile(ngoSoEReportModel.getSoeFile()[0].getBytes());
						ngoReportsFile.setLive(true);
						ngoReportsFile.setStatus(new TypeDetails(ngoSoeReport));
						ngoReportsFile.setCollectUser(collectUser);
	
						ngoReportsFileRepository.save(ngoReportsFile);
						LOGGER.info("Description : "+collectUserModel.getUsername()+ "("
								+ email +")" +" : Action :"+ 
								messageSourceNotification.getMessage("file.upload.success", null,null)+" : for month "
										+new DateFormatSymbols().getMonths()[ngoSoEReportModel.getMonth()]+" : "
										+fullDateFormat.format(new Date()));
						
						return "success";
					}
				} else {
					LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("
							+ email +")" +" : Action :"+ 
							messageSourceNotification.getMessage("sheet.not.match", null,null)+" : for month "
									+new DateFormatSymbols().getMonths()[ngoSoEReportModel.getMonth()]+" : "+fullDateFormat.format(new Date()));
					
					return messageSourceNotification.getMessage("sheet.not.match", null, null);
				}}else{
					workbook.close();
					LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("
							+ email +")" +" : Action :"+ 
							messageSourceNotification.getMessage("report.is.rejected", null,null)+" : for month "
									+new DateFormatSymbols().getMonths()[ngoSoEReportModel.getMonth()]+" : "
											+" : "+fullDateFormat.format(new Date()));
					
					return messageSourceNotification.getMessage("report.is.rejected", null, null);
			}
		}
	}

	/**
	 * @Description Returns all the Report for a NGO User for the  
	 * current financial year including the reporting month. 
	 * 
	 * @author Abhisheka Mishra(abhisheka@sdrc.co.in)
	 * 
	 */
	@Override
	public Map<String, List<DropdownModel>> dropdownValues() throws Exception {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel)stateManager.getValue(Constants.USER_PRINCIPAL));
		
		if(null==collectUserModel){
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null)+" "+fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		NGO ngo = ngoRepository.findById(Integer.valueOf(collectUserModel.getNgo().getId()));
		
		Integer value = null;
		int ngoLevelUser = Integer.valueOf(messageSourceNotification.getMessage("ngo.level.user", null, null));
		int ngoReportType = Integer.valueOf(messageSourceNotification.getMessage("ngo.report.type", null, null));
		if (collectUserModel.getUserAreaModels().get(0)
				.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getRole().getRoleId() == ngoLevelUser)	
		{
			value = ngoReportType;	
		}
		// Current Date
		Timestamp currentTime = new Timestamp(new Date().getTime());	
		String yearlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.yearly", null, null);
		String monthlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.monthly", null, null);
		// Current TimePeriod with periodicity '12'
		TimePeriod latestTimePeriod = timePeriodRepository.getTimePeriodofSixPeriodicity(yearlyPeriodicity,currentTime);
		// Current TimePeriod with periodicity '1'
		TimePeriod monthlyLatestTimePeriod = timePeriodRepository.getTimePeriodofSixPeriodicity(monthlyPeriodicity,currentTime);
		
		// Fetches all Yearly timeperiod upto current financial year
		List<TimePeriod> timePeriodList = timePeriodRepository.getAllYearlyDataUptoCurrentFinancialYear
				(yearlyPeriodicity,latestTimePeriod.getTimePeriodId());
		int aprilMonth =Integer.valueOf(messageSourceNotification.getMessage("april.month", null, null));
		
		// Fetches data from Ngo Upload Status table for 'April' Month
		NGOSoEUploadsStatus ngoSoEUploadsStatus = ngoSoEUploadsStatusRepository.findByNgoAndMonthAndYear
				(ngo,aprilMonth,monthlyLatestTimePeriod.getYear()); 
		List<NGOSoEUploadsStatus> ngoSoEUploadsStatusList = ngoSoEUploadsStatusRepository.findAll();
		
		List<TypeDetails> typeDetailsModels = typeDetailsRepository.findByTypeTypeIdOrderByTypeDetailIdAsc(value); 
		List<TypeDetailsModel> detailsModels = new ArrayList<TypeDetailsModel>();
		Map<String, List<DropdownModel>> map = new HashMap<String, List<DropdownModel>>();
		
		if (!typeDetailsModels.isEmpty()) {
			for (int i = 0; i < typeDetailsModels.size(); i++) {
				TypeDetailsModel typeDetailsModel = new TypeDetailsModel();
				typeDetailsModel.setTypeDetailName(typeDetailsModels.get(i)
						.getTypeDetailName() != null ? typeDetailsModels.get(i)
						.getTypeDetailName() : null);
				typeDetailsModel.setTypeDetailId(typeDetailsModels.get(i)
						.getTypeDetailId() != null ? typeDetailsModels.get(i)
						.getTypeDetailId() : null);
				typeDetailsModel.setTypeId((Integer) typeDetailsModels.get(i)
						.getType().getTypeId() != null ? typeDetailsModels
						.get(i).getType().getTypeId() : null);
				typeDetailsModel.setCreatedBy(typeDetailsModels.get(i)
						.getCreatedBy() != null ? typeDetailsModels.get(i)
						.getCreatedBy() : null);
				typeDetailsModel.setCreatedDate(typeDetailsModels.get(i)
						.getCreatedDate() != null ? typeDetailsModels.get(i)
						.getCreatedDate() : null);
				typeDetailsModel.setMonthlyAvailability(true);

				detailsModels.add(typeDetailsModel);
			}
		}
		
		/*
		 * Retrieve the data upto current month
		 */
		NGOSoEReport ngoSoEReport = ngoSoEReportRepository.findTop1ByNgoOrderByNgoSoEReportsIdDesc(ngo);
		Map<String, List<DropdownModel>> mapData = new TreeMap<String, List<DropdownModel>>(Collections.reverseOrder());
		DropdownModel dropdownModel = null;
		
		if(ngoSoEReport == null){
			
			dropdownModel = new DropdownModel();
			
			if(ngoSoEUploadsStatus == null && ngoSoEUploadsStatusList.isEmpty()){
				map = null;
			}else{
				for (int i = 0; i < timePeriodList.size(); i++) {
					List<DropdownModel> dropdownModelList = new ArrayList<DropdownModel>();
					dropdownModel.setTypeDetailsModel(detailsModels);
					dropdownModelList.add(dropdownModel);
					map.put(timePeriodList.get(i).getFinancialYear(), dropdownModelList);
					mapData.put(timePeriodList.get(i).getFinancialYear(), map.get(timePeriodList.get(i).getFinancialYear()));
				}
			}
		}else{
			Timestamp currentDate = new Timestamp(dateFormat.parse(ngoSoEReport.getYear()+"-"+
					(ngoSoEReport.getMonth()+2)+"-01").getTime());
			String monthly = messageSourceNotification.getMessage("timeperiod.periodicity.monthly", null,null);
			List<TimePeriod> timePeriods = timePeriodRepository.getAllTimePeriods(currentDate,Arrays.asList(monthly));
			
			List<ValueObject> timeperiodModels = new ArrayList<ValueObject>();
			
			if(!timePeriods.isEmpty()){
				// Adding all the time periods in a list
				for (int i = 0; i < timePeriods.size(); i++) {
					ValueObject valueObject = new ValueObject();
					valueObject.setKey(String.valueOf(timePeriods.get(i).getTimePeriodId()));
					
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(timePeriods.get(i).getStartDate());
					
					valueObject.setDescription(monthFormats.format(calendar.getTime()).toUpperCase());
					valueObject.setGroupName(timePeriods.get(i).getFinancialYear());
					timeperiodModels.add(valueObject);
				}
			}	
			
			List<DropdownModel> dropdownModels = null;
			// Iterating timeperiodModels and putting in a map  
			for (int i = 0; i < timeperiodModels.size(); i++) {
				if(map.containsKey(timeperiodModels.get(i).getGroupName())){
					dropdownModels = map.get(timeperiodModels.get(i).getGroupName());
					dropdownModels.get(dropdownModels.size()-1).getTimePeriodList().add(timeperiodModels.get(i));
				}else{
					dropdownModel = new DropdownModel();
					List<ValueObject> list2 = new ArrayList<ValueObject>();
					list2.add(timeperiodModels.get(i));
					dropdownModel.setTimePeriodList(list2);
					dropdownModel.setTypeDetailsModel(detailsModels);
					
					dropdownModels = new ArrayList<DropdownModel>();
					dropdownModels.add(dropdownModel);
					map.put(timeperiodModels.get(i).getGroupName(), dropdownModels);
				}
			}
			// Here we'll set value in the financial key-value pair
			for (int i = 0; i < timePeriodList.size(); i++) {
				mapData.put(timePeriodList.get(i).getFinancialYear(), map.get(timePeriodList.get(i).getFinancialYear()));
			}
		}
		return mapData;
	}

	/**
	 * @Description Returns SoE history detail for a NGO user. 
	 * @author Abhisheka Mishra(abhisheka@sdrc.co.in)
	 * 
	 */
	@Override
	public Map<String, List<NGOSoEViewModel>> viewSoeDetails() {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel)stateManager.getValue(Constants.USER_PRINCIPAL));
		
		if(null==collectUserModel){
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null)+" "+fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		
		NGO ngo = ngoRepository.findById(Integer.valueOf(collectUserModel.getNgo().getId()));
		String monthlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.monthly", null, null);
		int approved = Integer.valueOf(messageSourceNotification.getMessage("typedetails.approved", null, null));
		int autoApproved = Integer.valueOf(messageSourceNotification.getMessage("typedetails.auto.approved", null, null));
		int rejected = Integer.valueOf(messageSourceNotification.getMessage("typedetails.rejected", null, null));
		int noActionTaken = Integer.valueOf(messageSourceNotification.getMessage("typedetails.no.action.taken", null, null));
		int reUpload = Integer.valueOf(messageSourceNotification.getMessage("typedetails.re.upload", null, null));
		List<TimePeriod> timePeriods = timePeriodRepository.findByPeriodicity(monthlyPeriodicity);
		Calendar cal = Calendar.getInstance();
		
		/**
		 * Retrieves all the historical data for a NGO
		 */
		List<Object[]> objects =  ngoSoEReportRepository.viewSoeDetails
					(Arrays.asList(approved,autoApproved,rejected,noActionTaken,reUpload),ngo.getId()
							,new Timestamp(new Date().getTime()),reUpload);

		List<NGOSoEViewModel> viewModels = new ArrayList<NGOSoEViewModel>();
		List<TypeDetails> details = typeDetailsRepository.findAll();
		
		List<CollectUser> collectUsers = null;
		CollectUser collectUser = new CollectUser();
		
		Map<Integer, CollectUser> userMap = new HashMap<Integer, CollectUser>();
		List<CollectUser> users = collectUserRepository.findAll();
		// Iterating all the users and storing in a map
		for (int i = 0; i < users.size(); i++) {
			if(userMap.containsKey(users.get(i).getUserId())){
				collectUser = userMap.get(users.get(i).getUserId());
				collectUsers.add(collectUser);
				collectUsers.add(users.get(i));
				userMap.put(users.get(i).getUserId(), users.get(i));
			}else{
				collectUsers = new ArrayList<CollectUser>();
				collectUsers.add(users.get(i));
				userMap.put(users.get(i).getUserId(), users.get(i));
			}
		}
		
		if (!objects.isEmpty()) {
			// Iterating all the NGO SoE reports 
			for (Object[] object : objects) {
				NGOSoEViewModel ngoSoEViewModel = new NGOSoEViewModel();
				ngoSoEViewModel.setNgoSoEReportsId(object[0] != null ?Integer.valueOf(object[0].toString()) : null);
				
				if(object[1] != null){
				cal.set(Calendar.MONTH,Integer.valueOf(object[1].toString()));
				}
				ngoSoEViewModel.setMonth(object[1] != null ? monthFormats.format(cal.getTime()).toUpperCase() : null);
				ngoSoEViewModel.setYear(object[2] != null ? Integer.valueOf(object[2].toString()) : null);
				ngoSoEViewModel.setCreatedDate(object[3] != null ? simpleDateFormat.format(object[3]) : null);
				ngoSoEViewModel.setNgoSoEUploadsStatusId(object[4] != null ? Integer.valueOf(object[4].toString()) : null);
				ngoSoEViewModel.setTypeDetailId(object[5] != null ? Integer.valueOf(object[5].toString()) : null);
				ngoSoEViewModel.setStatus(
						(object[11] != null && Integer.valueOf(object[11].toString()) != reUpload) ? getTypeDetailsByTypeDetailsId(details, Integer.valueOf(object[11].toString())).getTypeDetailName() 
								: getTypeDetailsByTypeDetailsId(details, Integer.valueOf(object[6].toString())).getTypeDetailName());
				ngoSoEViewModel.setUserId(object[7] != null ? Integer.valueOf(object[7].toString()) : null);
				ngoSoEViewModel.setRemarks(object[8] != null ? object[8].toString() : null);
				ngoSoEViewModel.setRejectCount(object[9] != null ? Integer.valueOf(object[9].toString()) : null);
				ngoSoEViewModel.setActionTakenUser(object[10] != null ? userMap.get(Integer.valueOf(object[10].toString())).getUsername() : null);
				ngoSoEViewModel.setActionTakenDate(object[12] != null ? simpleDateFormat.format(object[12]) : null);
				
				TimePeriod timePeriod = getTimePeriodByMonthAndYear(timePeriods,dateFormatNew.format(cal.getTime()),Integer.valueOf(object[2].toString()));
				ngoSoEViewModel.setFinancialYear(timePeriod.getFinancialYear());
				
				viewModels.add(ngoSoEViewModel);
			}
		}
		// populate the model into a map based on the financial year
		List<NGOSoEViewModel> reportsModels = null;
		Map<String, List<NGOSoEViewModel>> mapData = new TreeMap<String, List<NGOSoEViewModel>>(Collections.reverseOrder());
		for (int i = 0; i < viewModels.size(); i++) {
			if(mapData.containsKey(viewModels.get(i).getFinancialYear())){
				reportsModels = mapData.get(viewModels.get(i).getFinancialYear());
				reportsModels.add(viewModels.get(i));
				mapData.put(viewModels.get(i).getFinancialYear(), reportsModels);
			}else{
				reportsModels = new ArrayList<NGOSoEViewModel>();
				reportsModels.add(viewModels.get(i));
				mapData.put(viewModels.get(i).getFinancialYear(), reportsModels);
			}
		}	
		
		return mapData;
	}

	/**
	 * @Description This job will executes on 11th of every month and saves data in NGOSoEUploadsStatus table.
	 *  It sets the Deadline date for uploading the SoE for each NGO and set the status of each NGO to 'Pending Upload'.
	 *  
	 * @author Abhisheka Mishra(abhisheka@sdrc.co.in)
	 *  
	 */
	@Transactional
	@Scheduled(cron="0 5 0 11 1/1 ?")
	@Override
	public void insertIntoSoeUploadStatus() {
		Calendar cale = Calendar.getInstance();
		cale.setTime(new Date());
		
		String yearlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.yearly", null, null);
		String monthlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.monthly", null, null);
		TimePeriod latestTimePeriod = timePeriodRepository.getTimePeriodofSixPeriodicity(monthlyPeriodicity,new Timestamp(new Date().getTime()));
		TimePeriod timePeriod =  timePeriodRepository.getTimePeriodData(new Timestamp(cale.getTime().getTime()),
				yearlyPeriodicity,latestTimePeriod.getFinancialYear());
		
		List<Integer> ngos = null;
		try {
			ngos = soETemplateRepository.getNgoDetailsFromSoeTemplate(timePeriod.getTimePeriodId());
		} catch (Exception e) {
			LOGGER.error(messageSourceNotification.getMessage("unable.to.update", null, null)
					+" for month "+timePeriod.getTimePeriod()+" : "+fullDateFormat.format(new Date())+" : "+e);
		}
	
		int typeDetailsId = Integer.valueOf(messageSourceNotification.getMessage("ngo.soe.soe.id", null, null));
		int defaultConfigCutOffDay = Integer.valueOf(messageSourceNotification.getMessage("ngo.default.config", null, null));
		/**
		 *  Setting default deadline Date for NGO user  
		 */
		Configuration configuration= configurationRepository.findByTypeDetailsTypeDetailIdAndIsLiveTrue(typeDetailsId);
		// if SoE configuration not found in database then set the default cut-off days. 
		if(configuration == null){
			stateUserService.setConfiguration(defaultConfigCutOffDay, typeDetailsId);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		Integer monthval = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, monthval+1);
		calendar.set(Calendar.DAY_OF_MONTH,(configuration != null ? configuration.getNgoCutOffDays() : defaultConfigCutOffDay));
			
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		Integer year = cal.get(Calendar.YEAR);
		Integer month = cal.get(Calendar.MONTH);
		
		int pendingUpload = Integer.valueOf(messageSourceNotification.getMessage("ngo.soe.pending.upload", null, null));
		List<NGOSoEUploadsStatus> list = new ArrayList<NGOSoEUploadsStatus>();
		
		if(!ngos.isEmpty()){
		for (int i = 0; i < ngos.size(); i++) {
			NGOSoEUploadsStatus ngoSoEUploadsStatus = new NGOSoEUploadsStatus();
			
			ngoSoEUploadsStatus.setMonth(month);
			ngoSoEUploadsStatus.setYear(year);
			try {
				ngoSoEUploadsStatus.setDeadlineDate(new Timestamp(fullDateFormat.parse(dateFormat.format(calendar.getTime())+" 23:59:59").getTime()));
			} catch (ParseException e) {
				LOGGER.error(messageSourceNotification.getMessage("date.parse", null, null)+fullDateFormat.format(new Date())+" : "+e);
			}
			ngoSoEUploadsStatus.setLastUploadedDate(new Timestamp(new Date().getTime()));
			ngoSoEUploadsStatus.setCreatedDate(new Timestamp(new Date().getTime()));
			ngoSoEUploadsStatus.setNgo(new NGO(ngos.get(i)));
			ngoSoEUploadsStatus.setStatus(new TypeDetails(pendingUpload));
			ngoSoEUploadsStatus.setTimePeriod(latestTimePeriod);
			ngoSoEUploadsStatus.setNewEntry(true);
			
			list.add(ngoSoEUploadsStatus);
		}
		ngoSoEUploadsStatusRepository.save(list);
		LOGGER.info("Description : " + messageSourceNotification.getMessage("file.updated.for.district", null, null)
				+" for the period "+timePeriod.getTimePeriod()+" : "+fullDateFormat.format(new Date()));
		}
	}
	
	/**
	 * @Description Returns the SoE template detail for a NGO level user
	 * 
	 * @author Abhisheka Mishra
	 * 
	 * @throws Exception 
	 */
	@Override
	public Map<String, List<NGOSoEUploadsStatusModel>> getUploadTableDetails() throws Exception {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel)stateManager.getValue(Constants.USER_PRINCIPAL));
		
		if(null==collectUserModel){
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null)+" "+fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		
		CollectUser collectUser = new CollectUser();
		collectUser.setUserId(collectUserModel.getUserId());
		NGO ngo = ngoRepository.findById(Integer.valueOf(collectUserModel.getNgo().getId()));
		Calendar cal = Calendar.getInstance();
		
		// Pending for Upload, Reject and Pending Values have been taken from typeDetails
		int rejected = Integer.valueOf(messageSourceNotification.getMessage("typedetails.rejected", null, null));
		int pendingUpload = Integer.valueOf(messageSourceNotification.getMessage("typedetails.pending.upload", null, null));
		int pending = Integer.valueOf(messageSourceNotification.getMessage("typedetails.pending", null, null));
		String monthlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.monthly", null, null);
		int approved = Integer.valueOf(messageSourceNotification.getMessage("typedetails.approved", null, null));
		int autoApproved = Integer.valueOf(messageSourceNotification.getMessage("typedetails.auto.approved", null, null));
		int rejectcount = Integer.valueOf(messageSourceNotification.getMessage("rejectcount.limit", null, null));
		int january = Integer.valueOf(messageSourceNotification.getMessage("january.month", null, null));
		int april = Integer.valueOf(messageSourceNotification.getMessage("april.month", null, null));
		int october = Integer.valueOf(messageSourceNotification.getMessage("oct.month", null, null));
		int december = Integer.valueOf(messageSourceNotification.getMessage("december.month", null, null));
		int ngoBlockPeriod = Integer.valueOf(messageSourceNotification.getMessage("ngo.blockperiod", null, null));
		
		List<TimePeriod> timePeriods = timePeriodRepository.findByPeriodicity(monthlyPeriodicity);
		List<NGOSoEUploadsStatus> uploadsStatus = ngoSoEUploadsStatusRepository.getUploadTableDetails(ngo.getId(),
				new Timestamp(new Date().getTime()),Arrays.asList(rejected,pendingUpload,pending),rejectcount);
		List<NGOSoEUploadsStatusModel> ngoSoEUploadsStatusModels = new ArrayList<NGOSoEUploadsStatusModel>();
		Timestamp timestamp = new Timestamp(new Date().getTime());
		
		List<NGOSoEReport> ngoSoEReports = ngoSoEReportRepository.getApprovedOrAutoApprovedData(ngo.getId(),Arrays.asList(approved,autoApproved));
		Map<String, Boolean> map = new LinkedHashMap<String, Boolean>();
		for (int i = 0; i < ngoSoEReports.size(); i++) {
			map.put(ngoSoEReports.get(i).getMonth()+"_"+ngoSoEReports.get(i).getTimePeriod().getFinancialYear(), true);
		}
		
		if(!uploadsStatus.isEmpty()){
			for (int i = 0; i < uploadsStatus.size(); i++) {
				if(uploadsStatus.get(i).getRejectCount() != null ?
						(uploadsStatus.get(i).getRejectCount() <= rejectcount)
						: uploadsStatus.get(i).getRejectCount() == null){
				
				NGOSoEUploadsStatusModel ngoSoEUploadsStatusModel = new NGOSoEUploadsStatusModel();
				
				ngoSoEUploadsStatusModel.setStatusId(uploadsStatus.get(i).getNgoSoEUploadsStatusId() != null ? uploadsStatus.get(i).getNgoSoEUploadsStatusId() : null);
				ngoSoEUploadsStatusModel.setDeadlineDate(uploadsStatus.get(i).getDeadlineDate() != null ? simpleDateFormat.format(uploadsStatus.get(i).getDeadlineDate()) : null);
				
				if(uploadsStatus.get(i).getStatus().getTypeDetailId() == pending){
				ngoSoEUploadsStatusModel.setLastUploadedDate(simpleDateFormat.format(
						uploadsStatus.get(i).getLastUploadedDate() != null ? uploadsStatus.get(i).getLastUploadedDate() : null));
				}else{
				ngoSoEUploadsStatusModel.setLastUploadedDate(null);	
				}
				
				if((Integer)uploadsStatus.get(i).getMonth() != null){
					cal.set(Calendar.MONTH,Integer.valueOf(uploadsStatus.get(i).getMonth()));
					}
				
				ngoSoEUploadsStatusModel.setMonth((Integer)uploadsStatus.get(i).getMonth() != null ? monthFormats.format(cal.getTime()).toUpperCase() : null);
				ngoSoEUploadsStatusModel.setMonthValue((Integer)uploadsStatus.get(i).getMonth() != null ? uploadsStatus.get(i).getMonth() : null);
				ngoSoEUploadsStatusModel.setYear((Integer)uploadsStatus.get(i).getYear() != null ? uploadsStatus.get(i).getYear() : null);
				ngoSoEUploadsStatusModel.setUserId(collectUserModel.getUserId());
				ngoSoEUploadsStatusModel.setNgo_id_fk(uploadsStatus.get(i).getNgo().getId() != null ? uploadsStatus.get(i).getNgo().getId() : null);
				ngoSoEUploadsStatusModel.setNgoSoEUploadsStatusId(uploadsStatus.get(i).getNgoSoEUploadsStatusId() != null ? uploadsStatus.get(i).getNgoSoEUploadsStatusId() : null);
				
				TimePeriod timePeriod = getTimePeriodByMonthAndYear(timePeriods,dateFormatNew.format(cal.getTime()),uploadsStatus.get(i).getYear());
				ngoSoEUploadsStatusModel.setFinancialYear(timePeriod.getFinancialYear());
				
				Integer currentMonth = null;
				Integer previousMonth = null;
				Integer year = null;
				Timestamp tp= null;
				/**
				 * Fetching the SoE template file status for every month 
				 * based on approve/auto-approve/pendingUpload/pending/rejected status of the SoE template
				 */
				if(uploadsStatus.get(i).getMonth() ==april && uploadsStatus.get(i).getDeadlineDate().after(timestamp)){
					
					previousMonth = (uploadsStatus.get(i).getMonth()-1);
					List<Object[]> objects = ngoSoEUploadsStatusRepository.getDataForPreviousMonth
							(ngo.getId(),previousMonth,uploadsStatus.get(i).getYear());
					if(objects.isEmpty() || objects == null)
					{
						ngoSoEUploadsStatusModel.setSubmitted(true);
					}
					else{
						int status = Integer.valueOf(objects.get(0)[3].toString());
						if(status == approved || status == autoApproved){//Approved(2) or Auto-Approved(3)
								ngoSoEUploadsStatusModel.setSubmitted(true);
						}else if (status == pendingUpload && timestamp.after((Timestamp)objects.get(0)[4])) {// pendingUpload(44)
								ngoSoEUploadsStatusModel.setSubmitted(true);
						}else if (status == pendingUpload && timestamp.before((Timestamp)objects.get(0)[4])) {
							ngoSoEUploadsStatusModel.setSubmitted(false);
						}else if (status == pending && timestamp.before((Timestamp)objects.get(0)[6])) {// pending (1)
							ngoSoEUploadsStatusModel.setSubmitted(false);
						}else if (status == pending && timestamp.after((Timestamp)objects.get(0)[6])) {
									ngoSoEUploadsStatusModel.setSubmitted(false);
						}else if (status == rejected && timestamp.before((Timestamp)objects.get(0)[4])) {// rejected(4)
							ngoSoEUploadsStatusModel.setSubmitted(false);
						}else if (status == rejected && timestamp.after((Timestamp)objects.get(0)[4])) {
							ngoSoEUploadsStatusModel.setSubmitted(true);
						}else {
							ngoSoEUploadsStatusModel.setSubmitted(true);
						}
					} 
				}
				// If month is April and the current date is after the cut-off date then setting the isSubmitted to true 
				else if(uploadsStatus.get(i).getMonth() ==april && uploadsStatus.get(i).getDeadlineDate().after(timestamp)){
					
					previousMonth = (uploadsStatus.get(i).getMonth()-1);
					List<NGOSoEReport> reports = ngoSoEReportRepository.findByCollectUserAndNgoAndMonthAndYear(collectUser,ngo,
							(int)previousMonth,uploadsStatus.get(i).getYear());
					
					if(!reports.isEmpty() || reports == null){
						ngoSoEUploadsStatusModel.setSubmitted(getValue(map,previousMonth,uploadsStatus.get(i).getTimePeriod().getFinancialYear()));
					}else{
						ngoSoEUploadsStatusModel.setSubmitted(true);
					}
				}
				// In October, for an NGO user, if the budget was not uploaded in April setting the isSubmitted to false otherwise true 
				else if(uploadsStatus.get(i).getMonth() == october){
					previousMonth = (uploadsStatus.get(i).getMonth()-1);
					List<Object[]> objects = ngoSoEUploadsStatusRepository.getDataForPreviousMonth
							(ngo.getId(),previousMonth,uploadsStatus.get(i).getYear());
					Integer status = !objects.isEmpty() ?  Integer.valueOf(objects.get(0)[3].toString()) : null;
					
					if(objects.isEmpty() || objects == null){
						if(map.isEmpty()){
							if(uploadsStatus.get(i).isNewEntry()==true){
								tp= Timestamp.valueOf((uploadsStatus.get(i).getCreatedDate()).toLocalDateTime().
										toLocalDate().plusDays(ngoBlockPeriod).atStartOfDay());
								if(timestamp.before(tp)){
									ngoSoEUploadsStatusModel.setSubmitted(false);
									ngoSoEUploadsStatusModel.setOnHalt(true);
								}else{
									ngoSoEUploadsStatusModel.setSubmitted(true);
								}
							}
						}else{
							ngoSoEUploadsStatusModel.setSubmitted(getValue(map,uploadsStatus.get(i).getMonth(),
									uploadsStatus.get(i).getTimePeriod().getFinancialYear()));
						}
					}
					//If the SoE templates status is approved/auto-approved and current date is before the deadline date 
					//then set the isSubmitted to false   
					else if(status == approved || status == autoApproved){
						tp= Timestamp.valueOf(((Timestamp)objects.get(0)[5]).toLocalDateTime().
								toLocalDate().plusDays(ngoBlockPeriod).atStartOfDay());
						if(timestamp.before(tp)){
							ngoSoEUploadsStatusModel.setSubmitted(false);
							ngoSoEUploadsStatusModel.setOnHalt(true);
						}else{
							ngoSoEUploadsStatusModel.setSubmitted(true);
						}
					}
					// If the SoE status is "pending upload" and current date is after the cut-off date then set the isSubmitted to false 
					else if (status == pendingUpload && timestamp.after((Timestamp)objects.get(0)[4])) { 
						tp= Timestamp.valueOf(((Timestamp)objects.get(0)[4]).toLocalDateTime().
								toLocalDate().plusDays(ngoBlockPeriod).atStartOfDay());
						if(timestamp.before(tp)){
							ngoSoEUploadsStatusModel.setSubmitted(false);
							ngoSoEUploadsStatusModel.setOnHalt(true);
						}else{
							ngoSoEUploadsStatusModel.setSubmitted(true);
						}
					}
					// If the SoE status is "pending upload" and current date is before the cut-off date then set the isSubmitted to false
					else if (status == pendingUpload && timestamp.before((Timestamp)objects.get(0)[4])) {
						ngoSoEUploadsStatusModel.setSubmitted(false);
					}
					// If the SoE status is "pending upload" and current date is before the cut-off date then set the isSubmitted to false
					else if (status == pending && timestamp.before((Timestamp)objects.get(0)[6])) {
						ngoSoEUploadsStatusModel.setSubmitted(false);
					}
					// If the SoE status is "pending upload" and current date is after the cut-off date then set the isSubmitted to false
					else if (status == pending && timestamp.after((Timestamp)objects.get(0)[6])) {
						if(objects.get(0)[2] == null){
							tp= Timestamp.valueOf(((Timestamp)objects.get(0)[4]).toLocalDateTime().
									toLocalDate().plusDays(ngoBlockPeriod).atStartOfDay());
							if(timestamp.before(tp)){
								ngoSoEUploadsStatusModel.setSubmitted(false);
								ngoSoEUploadsStatusModel.setOnHalt(true);
								} else {
									ngoSoEUploadsStatusModel.setSubmitted(false);
								}
						 }else if(objects.get(0)[2] != null && Integer.valueOf(objects.get(0)[2].toString()) <= 2){
							tp= Timestamp.valueOf(((Timestamp)objects.get(0)[5]).toLocalDateTime().
									toLocalDate().plusDays(ngoBlockPeriod).atStartOfDay());
							if(timestamp.before(tp)){
								ngoSoEUploadsStatusModel.setSubmitted(false);
								ngoSoEUploadsStatusModel.setOnHalt(true);
								} else {
									ngoSoEUploadsStatusModel.setSubmitted(false);
								}
						 }else{
							 ngoSoEUploadsStatusModel.setSubmitted(false);
						}
					}
					// If the SoE status is rejected and current date is before the cut-off date then set the isSubmitted to false
					else if (status == rejected && timestamp.before((Timestamp)objects.get(0)[5])) {
						ngoSoEUploadsStatusModel.setSubmitted(false);
					}
					// If the SoE status is rejected and current date is after the cut-off date then set the isSubmitted to false
					else if (status == rejected && timestamp.after((Timestamp)objects.get(0)[5])) {
						if(objects.get(0)[2] == null || Integer.valueOf(objects.get(0)[2].toString()) <= 2){
							tp= Timestamp.valueOf(((Timestamp)objects.get(0)[5]).toLocalDateTime().
									toLocalDate().plusDays(ngoBlockPeriod).atStartOfDay());
							if(timestamp.before(tp)){
								ngoSoEUploadsStatusModel.setSubmitted(false);
								ngoSoEUploadsStatusModel.setOnHalt(true);
								} else {
									ngoSoEUploadsStatusModel.setSubmitted(true);
								}
						 }else{
							 ngoSoEUploadsStatusModel.setSubmitted(true);
						}
					}else {
						ngoSoEUploadsStatusModel.setSubmitted(true);
					}
				}
				// If the SoE status is pending upload, rejected and current date is before the cut-off date then set the isSubmitted to false
				else if((uploadsStatus.get(i).getStatus().getTypeDetailId()== pendingUpload || 
						uploadsStatus.get(i).getStatus().getTypeDetailId()==rejected) 
						&& uploadsStatus.get(i).getDeadlineDate().before(timestamp)){
					
					ngoSoEUploadsStatusModel.setSubmitted(false);
					
				}
				// If the SoE status is pending upload,rejected and current date is after the cut-off date then set the isSubmitted to false
				else if((uploadsStatus.get(i).getStatus().getTypeDetailId()== pendingUpload || 
						uploadsStatus.get(i).getStatus().getTypeDetailId()==rejected) 
						&& uploadsStatus.get(i).getDeadlineDate().after(timestamp)){
					
					previousMonth = uploadsStatus.get(i).getMonth()  == january ? december : (uploadsStatus.get(i).getMonth()-1);
					year = uploadsStatus.get(i).getMonth() == january ? (uploadsStatus.get(i).getYear()-1) : uploadsStatus.get(i).getYear();
					NGOSoEUploadsStatus ngoSoEUploadsStatus = ngoSoEUploadsStatusRepository.
							getTimePeriodByNGOMonthAndYear(ngo.getId(), previousMonth, year);
					// If the SoE status not found then set the isSubmitted to true
					if(ngoSoEUploadsStatus == null){
						if(ngoSoEReports.isEmpty() || ngoSoEReports == null){
							ngoSoEUploadsStatusModel.setSubmitted(true);
						}else{
							currentMonth = ngoSoEReports.get(ngoSoEReports.size()-1).getMonth();
							ngoSoEUploadsStatusModel.setSubmitted(getValue(map,currentMonth,uploadsStatus.get(i).getTimePeriod().getFinancialYear()));
						}
					}else if(ngoSoEUploadsStatus.getStatus().getTypeDetailId() == pendingUpload && 
							ngoSoEUploadsStatus.getDeadlineDate().before(timestamp)){
						ngoSoEUploadsStatusModel.setSubmitted(true);
					}else if((ngoSoEUploadsStatus.getStatus().getTypeDetailId() == approved || 
							ngoSoEUploadsStatus.getStatus().getTypeDetailId() == autoApproved )
							&& ngoSoEUploadsStatus.getDeadlineDate().before(timestamp)){
						ngoSoEUploadsStatusModel.setSubmitted(true);
					}else if((ngoSoEUploadsStatus.getStatus().getTypeDetailId() == pending) 
							&& ngoSoEUploadsStatus.getDeadlineDate().before(timestamp)){
						ngoSoEUploadsStatusModel.setSubmitted(false); 
					}else if((ngoSoEUploadsStatus.getStatus().getTypeDetailId() == rejected 
							|| ngoSoEUploadsStatus.getStatus().getTypeDetailId() == pendingUpload)
							&& ngoSoEUploadsStatus.getDeadlineDate().before(timestamp)){
						ngoSoEUploadsStatusModel.setSubmitted(true); 
					}else if(ngoSoEUploadsStatus.getStatus().getTypeDetailId() == rejected && 
							ngoSoEUploadsStatus.getDeadlineDate().after(timestamp)){
						if(ngoSoEUploadsStatus.getRejectCount()==1)         
							ngoSoEUploadsStatusModel.setSubmitted(false);   
						else
							ngoSoEUploadsStatusModel.setSubmitted(true);    
					}
					else if((ngoSoEUploadsStatus.getStatus().getTypeDetailId() == pending || 
							ngoSoEUploadsStatus.getStatus().getTypeDetailId() == pendingUpload)
							&& ngoSoEUploadsStatus.getDeadlineDate().after(timestamp)){
						ngoSoEUploadsStatusModel.setSubmitted(false);
					}
					else{
						currentMonth = uploadsStatus.get(i).getMonth();
						ngoSoEUploadsStatusModel.setSubmitted(getValue(map,currentMonth,uploadsStatus.get(i).getTimePeriod().getFinancialYear()));
					}
					
				}else if(uploadsStatus.get(i).getStatus().getTypeDetailId()== approved || 
						uploadsStatus.get(i).getStatus().getTypeDetailId()==autoApproved){
					ngoSoEUploadsStatusModel.setSubmitted(true);
				}
				// If the SoE status is pending and current date is before the cut-off date then set the isSubmitted to true
				else if(uploadsStatus.get(i).getStatus().getTypeDetailId()== pending 
						&& uploadsStatus.get(i).getDeadlineDate().before(timestamp)){
					previousMonth = (uploadsStatus.get(i).getMonth()-1);
					NGOSoEUploadsStatus ngoSoEUploadsStatus = ngoSoEUploadsStatusRepository.
							getTimePeriodByNGOMonthAndYear(ngo.getId(), previousMonth, uploadsStatus.get(i).getYear());
					// If the SoE status not found then set the isSubmitted to true
					if(ngoSoEUploadsStatus == null){
						currentMonth = ngoSoEReports.get(ngoSoEReports.size()-1).getMonth();
						ngoSoEUploadsStatusModel.setSubmitted(getValue(map,currentMonth,uploadsStatus.get(i).getTimePeriod().getFinancialYear()));
					}else if(ngoSoEUploadsStatus.getStatus().getTypeDetailId() == pendingUpload && 
							ngoSoEUploadsStatus.getDeadlineDate().before(timestamp)){
						ngoSoEUploadsStatusModel.setSubmitted(true);
					}else if((ngoSoEUploadsStatus.getStatus().getTypeDetailId() == approved || 
							ngoSoEUploadsStatus.getStatus().getTypeDetailId() == autoApproved )
							&& ngoSoEUploadsStatus.getDeadlineDate().before(timestamp)){
						ngoSoEUploadsStatusModel.setSubmitted(true);
					}
					else{
						currentMonth = uploadsStatus.get(i).getMonth();
						ngoSoEUploadsStatusModel.setSubmitted(getValue(map,currentMonth,uploadsStatus.get(i).getTimePeriod().getFinancialYear()));
					}
				}else if(uploadsStatus.get(i).getStatus().getTypeDetailId()== pending 
						&& uploadsStatus.get(i).getDeadlineDate().after(timestamp)){
					ngoSoEUploadsStatusModel.setSubmitted(true);
				}
					
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.MONTH,Integer.valueOf(uploadsStatus.get(i).getMonth()-1));
				ngoSoEUploadsStatusModel.setMinMonth(monthFormats.format(calendar.getTime()).toUpperCase()); // Min month = previous month
				
				ngoSoEUploadsStatusModels.add(ngoSoEUploadsStatusModel);
			}
		}
		}
		// Iterating ngoSoEUploadsStatusModels and putting in a map
		if(!ngoSoEUploadsStatusModels.isEmpty()){
			List<NGOSoEUploadsStatusModel> reportsModels = null;
			Map<String, List<NGOSoEUploadsStatusModel>> mapData = new LinkedHashMap<String, List<NGOSoEUploadsStatusModel>>();
			
			for (int i = 0; i < ngoSoEUploadsStatusModels.size(); i++) {
				if(mapData.containsKey(ngoSoEUploadsStatusModels.get(i).getFinancialYear())){
					reportsModels = mapData.get(ngoSoEUploadsStatusModels.get(i).getFinancialYear());
					reportsModels.add(ngoSoEUploadsStatusModels.get(i));
					mapData.put(ngoSoEUploadsStatusModels.get(i).getFinancialYear(), reportsModels);
				}else{
					reportsModels = new ArrayList<NGOSoEUploadsStatusModel>();
					reportsModels.add(ngoSoEUploadsStatusModels.get(i));
					mapData.put(ngoSoEUploadsStatusModels.get(i).getFinancialYear(), reportsModels);
				}
			}	
			return mapData;
		}
		return null;
	}

	/**
	 * @Description Returns true when it will find the available month and financial year
	 * 
	 * @param map
	 * @param currentMonth
	 * @param financialYear
	 */
	private Boolean getValue(Map<String, Boolean> map, Integer currentMonth,
			String financialYear) {

		boolean value = false;
		for (int i = currentMonth; i >= 0; i--) {
			if (map.containsKey(i+"_"+financialYear)){
				value =  true;
				break;
			}else{
				value =  false;
			}
		}
		return value;
	}
	
	/**
	 * 
	 * @Description Here the Ngo level user will be able to download the template that has been uploaded by the district level user.
	 * Here we are passing the year and month for a NGO and getting the SoE file.Process to get the SoE file,
	 * if SoE is not available in NGOReportsFile table then it will check in SOETransaction table 
	 * and fetches the latest SoE template for that NGO User.
	 * 
	 * @author Abhisheka Mishra
	 * 
	 * @param month
	 * @param year
	 * 
	 */
	@Override
	@Transactional(readOnly=true)
	public String downloadSOEreport(int month,int year) throws Exception {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel)stateManager.getValue(Constants.USER_PRINCIPAL));
		if(null==collectUserModel){
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null)+" "+fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		
		CollectUser collectUser = new CollectUser();
		collectUser.setUserId(collectUserModel.getUserId());
		NGO ngo = ngoRepository.findById(Integer.valueOf(collectUserModel.getNgo().getId()));
		
		Timestamp currentDate = new Timestamp(simpleDateFormat.parse("01-"+(month+1) +"-"+year).getTime());
		String yearlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.yearly", null, null);//12
		String monthlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.monthly", null, null);//1
		String halfyearly = messageSourceNotification.getMessage("timeperiod.periodicity.halfyearly", null, null);//6
		TimePeriod latestTimePeriod = timePeriodRepository.getTimePeriodofSixPeriodicity(monthlyPeriodicity,currentDate);
		TimePeriod latestBiAnnual = timePeriodRepository.getTimePeriodofSixPeriodicity(halfyearly,currentDate);
		TimePeriod timePeriod = timePeriodRepository.getTimePeriodData(currentDate,yearlyPeriodicity,latestTimePeriod.getFinancialYear()); 
		int soeUpload = Integer.valueOf(messageSourceNotification.getMessage("ngo.soe.upload", null, null));//16
		int approved = Integer.valueOf(messageSourceNotification.getMessage("typedetails.approved", null, null));//2
		int autoApproved = Integer.valueOf(messageSourceNotification.getMessage("typedetails.auto.approved", null, null));//3
		int october = Integer.valueOf(messageSourceNotification.getMessage("oct.month", null, null));//9
		
		List<NGOSoEReport> ngoSoEReport = ngoSoEReportRepository
				.getDataFile(collectUser.getUserId(),ngo.getId(), new Timestamp(timePeriod.getStartDate().getTime()),
				new Timestamp(timePeriod.getEndDate().getTime()),Arrays.asList(approved,autoApproved));

		List<NGOSoEReport> ngoSoEReports = ngoSoEReportRepository.getApprovedOrAutoApprovedData(ngo.getId(),Arrays.asList(approved,autoApproved));
		Map<String, Boolean> map = new LinkedHashMap<String, Boolean>();
		// putting month_year as key and true as the default value in map
		for (int i = 0; i < ngoSoEReports.size(); i++) {
			map.put(ngoSoEReports.get(i).getMonth()+"_"+ngoSoEReports.get(i).getTimePeriod().getFinancialYear(), true);
		}
		
		SoETransaction soETransaction = null;
		NgoReportsFile ngoReportsFile = null;
		/**
		 *  Getting the last approved/auto-approved SoE file exists or not. 
		 */
		if(!ngoSoEReport.isEmpty()){
				// If month is October return the updated approve/auto-approve SoE file else return the last approve/auto-approve SoE file
				if(month==october){ 
					soETransaction = soETransactionRepository.getUpdatedFile(ngo.getId(),latestBiAnnual.getTimePeriodId());
					if(soETransaction != null){
						soETransaction = soETransactionRepository.getLatestTemplate(timePeriod.getTimePeriodId(),ngo.getId());
						return getFileUpdated(collectUser, timePeriod, month, year,currentDate,ngo,soETransaction.getUploadedTemplate());
					}else{
						ngoReportsFile = ngoReportsFileRepository
								.getCertificateFile(ngoSoEReport.get(ngoSoEReport.size()-1).getNgoSoEReportsId(),
								soeUpload, collectUser.getUserId());
						return getOldFile(collectUser, timePeriod, month, year,
								currentDate,ngo,soeUpload,ngoSoEReport,latestBiAnnual.getTimePeriodId(),ngoReportsFile.getReportsFile(),map);
					}
				}else{
					ngoReportsFile = ngoReportsFileRepository
							.getCertificateFile(ngoSoEReport.get(ngoSoEReport.size()-1).getNgoSoEReportsId(),
							soeUpload, collectUser.getUserId());
					return getOldFile(collectUser, timePeriod, month, year,currentDate,
							ngo,soeUpload,ngoSoEReport,latestBiAnnual.getTimePeriodId(),ngoReportsFile.getReportsFile(),map);
				}
			}else{
				soETransaction = soETransactionRepository.getLatestTemplate(timePeriod.getTimePeriodId(),ngo.getId());
				return getFileUpdated(collectUser, timePeriod, month, year,currentDate,ngo,soETransaction.getUploadedTemplate());
		}
	}
	
	/**
	 * @Description Returns the SoE template file for a NGO user.
	 * 
	 * @param collectUser
	 * @param timePeriod
	 * @param month
	 * @param year
	 * @param currentDate
	 * @param ngo
	 * @param soeUpload
	 * @param ngoSoEReport
	 * @param latestBiAnnualId
	 * @param file
	 * @param map
	 * @throws Exception
	 */

	private String getOldFile(CollectUser collectUser, TimePeriod timePeriod, int month, 
			int year, Timestamp currentDate, NGO ngo,int soeUpload,
			List<NGOSoEReport> ngoSoEReport,Integer latestBiAnnualId,byte[] file,Map<String, Boolean> map) throws Exception {
		// Converting byte[] to HSSFWorkbook
		HSSFWorkbook workbook = new HSSFWorkbook(MissionMilletUtil.getByteArrayStream(file));
		HSSFSheet sheet = workbook.getSheet("uuid");
		HSSFRow row = sheet.getRow(0);
		HSSFCell cell = row.getCell(1);
		
		if((int)cell.getNumericCellValue() == latestBiAnnualId){
			workbook = getWorkBookWithUUID(workbook, collectUser,month,year);
			return templateLock(workbook,currentDate, ngo.getName(), ngo.getId());
		}else {
			// Setting new biAnnual value
			cell.setCellValue(latestBiAnnualId);
			
			// Here we'r taking currentMonth = 11(December) so it will iterate from December to January
			boolean value = getValue(map,11,timePeriod.getFinancialYear());
			
			if(value == true){
				return getFileUpdated(collectUser, timePeriod, month, year,currentDate,ngo,file);
			}else{
				SoETransaction soETransaction = soETransactionRepository.getLatestTemplate(timePeriod.getTimePeriodId(),ngo.getId());
				
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				HSSFWorkbook workbookHssfWorkbook = new HSSFWorkbook(MissionMilletUtil.getByteArrayStream(soETransaction.getUploadedTemplate()));
				workbookHssfWorkbook.write(outputStream);
				workbookHssfWorkbook.close();	
				return getFileUpdated(collectUser, timePeriod, month, year,currentDate,ngo,outputStream.toByteArray());
			}
		}
	}

	/**
	 * @Description Getting the SoE file and locking the SoE template.
	 * 
	 * @param collectuser
	 * @param timeperiod
	 * @param month
	 * @param year
	 * @param current date
	 * @param ngo object
	 * @param file
	 */
	private String getFileUpdated(CollectUser collectUser, TimePeriod timePeriod,int month, int year,
			Timestamp currentDate,NGO ngo,byte[] file) throws Exception {
		// Converting byte[] to HSSFWorkbook
		HSSFWorkbook workbook = new HSSFWorkbook(MissionMilletUtil.getByteArrayStream(file));
		
		workbook = getWorkBookWithUUID(workbook, collectUser,month,year);
		 
		return templateLock(workbook,currentDate, ngo.getName(), ngo.getId());
	}

	/**
	 * @Description This method will create an unique UUID for NGO level user each time while downloading the SoE template  
	 * and create a new entry in UUIdGenerator table.
	 *  
	 * @param workbook
	 * @param collectUser
	 * @param month
	 * @param year
	 * 
	 * @return workbook
	 */
	@Transactional
	private HSSFWorkbook getWorkBookWithUUID(HSSFWorkbook workbook, CollectUser collectUser, int month,int year) {
		
		HSSFSheet sheet = workbook.getSheet("uuid");
		Row row = sheet.createRow(1);
		Cell cell = row.createCell(0);
		String uuid = UUID.randomUUID().toString();
		cell.setCellValue(uuid);
		
		UUIdGenerator uuidGenerator = new UUIdGenerator();
		uuidGenerator.setCollectUser(collectUser);
		uuidGenerator.setCreatedDate(new Timestamp(new Date().getTime()));
		uuidGenerator.setMonth(month);
		uuidGenerator.setUuid(uuid);
		uuidGenerator.setYear(year);
		uuIdGeneratorRepository.save(uuidGenerator);
		
		return workbook;
	}

	/**
	 * @Description While downloading we are locking the excel sheet except the required column.
	 * 
	 * @author Subrata
	 * @param workbook
	 * @param timestamp
	 * 
	 * @return file path
	 * @throws Exception
	 * 
	 */
	
	private String templateLock(HSSFWorkbook workbook,Timestamp time, String ngoName, int ngoId) throws Exception {
		//Reading the ngo_detailed_soe.properties file from messages directory
		Map<Integer, String> map = MissionMilletUtil.getPropertiesValues(getClass().getClassLoader()
				.getResourceAsStream("messages/ngo_detailed_soe.properties"));
		// For Row lock, reading the values from messages.properties file.
		List<Integer> listOfRowToLock = MissionMilletUtil.getLockedList(messageSourceNotification.getMessage("row.lock", null, null));
		HSSFSheet sheet = workbook.getSheet("Detailed SOE");
		
		int approved = Integer.valueOf(messageSourceNotification.getMessage("typedetails.approved", null, null));
		int autoApproved = Integer.valueOf(messageSourceNotification.getMessage("typedetails.auto.approved", null, null));
		int rejected = Integer.valueOf(messageSourceNotification.getMessage("typedetails.rejected", null, null));
		int noActionTaken = Integer.valueOf(messageSourceNotification.getMessage("typedetails.no.action.taken", null, null));
		int reUpload = Integer.valueOf(messageSourceNotification.getMessage("typedetails.re.upload", null, null));
		
//		List<Object[]> soeSubmitted =  ngoSoEReportRepository.viewSoeDetails
//				(Arrays.asList(approved,autoApproved,rejected,noActionTaken,reUpload), ngoId, new Timestamp(new Date().getTime()),reUpload);
		List<Object[]> soeSubmitted =  ngoSoEReportRepository.viewSoeDetails
				(Arrays.asList(approved,autoApproved), ngoId, new Timestamp(new Date().getTime()),reUpload);
		
		// Getting style sheet for unlocking
		HSSFCellStyle styleForUnLocking = LockWorkbook.getStyleForUnLocking(workbook);
		
		// Getting style sheet for locking
		HSSFCellStyle styleForLocking = LockWorkbook.getStyleForLocking(workbook);
		// Getting style sheet for left align
		HSSFCellStyle styleForLeftAlign = LockWorkbook.getStyleForLeftAlign(workbook);

		HSSFRow row = null;
		Cell cell = null;
		//Unlocking the cell to enter FA name
		row = sheet.getRow(3);
		cell = row.getCell(23);
		cell.setCellStyle(styleForLeftAlign);
		// To check the Month value
		row = sheet.getRow(4); 
		int colNum = 0;
		// Getting all the month cell from map
		List<Integer> list = new ArrayList<Integer>(map.keySet());
		Collections.sort(list);
		
		Date dateFromExcel = new Date(time.getTime());
		LocalDate localDateFromExcel = null;
		Date dateFromDb = null;
		// Converting current date to LocalDate object type
		LocalDate localDateFromDb = dateFromExcel.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		// Iterating the column values which contains the month.
		for (Integer in : list) {
			cell = row.getCell(in);
			
			dateFromDb = cell.getDateCellValue();
			// Converting date which is in excel to LocalDate object type
			localDateFromExcel = dateFromDb.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			// If localDateFromDb and localDateFromExcel are same then, we got the month for unlocking
			if(localDateFromDb.getMonthValue() == localDateFromExcel.getMonthValue()){
				colNum = in;
				break;
			}
		}
		cell = row.getCell(colNum);
		cell.setCellValue(time);
		// Getting column values from map to unlock. 
		String str = map.get(colNum);
		
		int startIndex=Integer.parseInt(messageSourceNotification.getMessage("soe.start.row.index", null, null));
		int endIndex=Integer.parseInt(messageSourceNotification.getMessage("soe.end.row.index", null, null));
		int obIndex=Integer.parseInt(messageSourceNotification.getMessage("ngo.ob.col.index", null, null));
		int asupmIndex=Integer.parseInt(messageSourceNotification.getMessage("ngo.asupm.col.index", null, null));
		
		row = null;
		cell = null;
		for (int i = startIndex; i <= endIndex; i++) {
			row = sheet.getRow(i);

			for (int j = 0; j < row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				//Applying the style for locking for all column 
				if (cell.getCellStyle().getLocked() == false)
					cell.setCellStyle(styleForLocking);
				
				//Applying the style for unlocking for the required column for that month
				if(!listOfRowToLock.contains(i)){
					if((localDateFromDb.getMonthValue()==4) ||(localDateFromDb.getMonthValue()==10 && soeSubmitted.isEmpty())){
						if ((cell.getColumnIndex() == Integer.valueOf((str.split(",")[0]).trim()))
								|| (cell.getColumnIndex() == Integer.valueOf((str.split(",")[1]).trim()))
								|| (cell.getColumnIndex() == Integer.valueOf((str.split(",")[2]).trim()))
								|| cell.getColumnIndex() == obIndex || cell.getColumnIndex() == asupmIndex ) {
							cell.setCellStyle(styleForUnLocking);
						}
					} else{
						if ((cell.getColumnIndex() == Integer.valueOf((str.split(",")[0]).trim()))
								|| (cell.getColumnIndex() == Integer.valueOf((str.split(",")[1]).trim()))
								|| (cell.getColumnIndex() == Integer.valueOf((str.split(",")[2]).trim()))) {
							cell.setCellStyle(styleForUnLocking);
						}
					}
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

		String outputPath = messageSourceNotification.getMessage("ngo.upload.report.path", null, null)
				+ngoName+"_"+ "Detailed_SOE_Template_" + timestampFormat.format(new Date())	+ ".xls";
		try{
		FileOutputStream fileOutputStream = new FileOutputStream(new File(outputPath));
		workbook.write(fileOutputStream);
		fileOutputStream.flush();
		fileOutputStream.close();
		}catch(Exception e){
			LOGGER.error("Error description : "+ fullDateFormat.format(new Date()), e);
		}
		return outputPath;
	}
	
	/**
	 * @Description Converting the date cell value to Date object type
	 * @author Subrata
	 * 
	 * @param cellDate
	 * 
	 * @return date
	 * 
	 */
	protected static Object getDateValue(Cell cellDate) {
		double numericDateValue = cellDate.getNumericCellValue();
		Date date = HSSFDateUtil.getJavaDate(numericDateValue);
		return date;
	}

	/**
	 * @Description downloads the SoE file for a NGO level user by passing the parameters  
	 * 
	 * @author Abhisheka Mishra(abhisheka@sdrc.co.in)
	 * @param reportId
	 * @param typeId
	 * @return outputPath
	 * 
	 */
	@Override
	@Transactional(readOnly=true)
	public String downloadSOEData(int reportId, int typeId) {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel)stateManager.getValue(Constants.USER_PRINCIPAL));
		
		if(null==collectUserModel){
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null)+" "+fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		
		NgoReportsFile ngoReportsFile = ngoReportsFileRepository.getCertificateFile(
				reportId, typeId,collectUserModel.getUserId());
		
		String outputPath = messageSourceNotification.getMessage("ngo.upload.report.path", null, null)
				+collectUserModel.getUsername().toUpperCase()+"_"+ "Detailed_SOE_" + timestampFormat.format(new Date())	+ ".xls";
		
		ByteArrayInputStream in = new ByteArrayInputStream(ngoReportsFile.getReportsFile());
		try {	
			// Locking the excel template.
			 HSSFWorkbook workbook =  LockWorkbook.workbookLock(new HSSFWorkbook(in));
			 
			FileOutputStream fileOutputStream = new FileOutputStream(new File(outputPath));
			workbook.write(fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
		 }catch(IOException e){
			 LOGGER.error(messageSourceNotification.getMessage("unhandled.exception.while.reading.workbook", null, null)); 
		 }
		 return outputPath;
	}
	
	/**
	 * @Description Returns a map of timeperiod by passing the parameters
	 * 
	 * @author Abhisheka Mishra 
	 * 
	 * @param timePeriods
	 * @param month
	 * @param year
	 * 
	 * @return TimePeriod object
	 */
	public static TimePeriod getTimePeriodByMonthAndYear(List<TimePeriod> timePeriods,String month,int year){
		Map<String, TimePeriod> map = new HashMap<String, TimePeriod>();
		TimePeriod timePeriod = null;
		for (int i = 0; i < timePeriods.size(); i++) {
			if(map.containsKey(timePeriods.get(i).getTimePeriod()+"_"+timePeriods.get(i).getYear())){
				timePeriod = map.get(timePeriods.get(i).getTimePeriod()+"_"+timePeriods.get(i).getYear());
				map.put(timePeriods.get(i).getTimePeriod()+"_"+timePeriods.get(i).getYear(), timePeriod);
			}else{
				map.put(timePeriods.get(i).getTimePeriod()+"_"+timePeriods.get(i).getYear(), timePeriods.get(i));
			}
		}
		return map.get(month+"_"+year);
	}

	/**
	 * @Description Getting a TypeDetails
	 * 
	 * @param typeDetails
	 * @param id
	 */
	public static TypeDetails getTypeDetailsByTypeDetailsId(List<TypeDetails> typeDetails,int id){
		Map<Integer, TypeDetails> map = new HashMap<Integer, TypeDetails>();
		TypeDetails details = null;
		for (int i = 0; i < typeDetails.size(); i++) {
			if(map.containsKey(typeDetails.get(i).getTypeDetailId())){
				details = map.get(typeDetails.get(i).getTypeDetailId());
				map.put(typeDetails.get(i).getTypeDetailId(), details);
			}else{
				map.put(typeDetails.get(i).getTypeDetailId(), typeDetails.get(i));
				
			}
		}
		return map.get(id);
	}
	
	/**
	 * @Description Getting a list of NgoReportsFile.
	 * 
	 * @param ngoReportsFiles
	 * @param id
	 * @return List<NgoReportsFile>
	 */
	
	public static List<NgoReportsFile> getReportTypeDetails(List<NgoReportsFile> ngoReportsFiles,int id){
		List<NgoReportsFile> reportsFiles = null;
		Map<Integer, List<NgoReportsFile>> reportsFilesMap = new HashMap<Integer, List<NgoReportsFile>>();
		Integer val = null;
		for (int i = 0; i < ngoReportsFiles.size(); i++) {
			val = ngoReportsFiles.get(i).getNgoReports() != null ? ngoReportsFiles.get(i).getNgoReports().getReportsId() : null;
			if(val != null){
				if(reportsFilesMap.containsKey(ngoReportsFiles.get(i).getNgoReports().getReportsId())){
					reportsFiles = reportsFilesMap.get(ngoReportsFiles.get(i).getNgoReports().getReportsId());
					reportsFiles.add(ngoReportsFiles.get(i));
					reportsFilesMap.put(ngoReportsFiles.get(i).getNgoReports().getReportsId(), reportsFiles);
				}else{
					reportsFiles = new ArrayList<NgoReportsFile>();
					reportsFiles.add(ngoReportsFiles.get(i));
					reportsFilesMap.put(ngoReportsFiles.get(i).getNgoReports().getReportsId(), reportsFiles);
				}
			}
		}
		return reportsFilesMap.get(id);
	}

	/**
	 * @Description NGO level user can download the latest SoE template by passing the parameters.
	 * 
	 * @param month
	 * @param year
	 * 
	 * @return filepath
	 */
	@Override
	@Transactional(readOnly=true)
	public String downloadLatestSOE(int month, int year) {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel)stateManager.getValue(Constants.USER_PRINCIPAL));
		
		if(null==collectUserModel){
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null)+" "+fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		String email = collectUserModel.getEmailId()==null?"":collectUserModel.getEmailId();
		NGO ngo = ngoRepository.findById(Integer.valueOf(collectUserModel.getNgo().getId()));
		// fetching the latest SOE 
		byte[] reportFile = ngoSoEReportRepository.getLatestSoeReport(collectUserModel.getUserId(),ngo.getId(),month,year);
		String outputPath = messageSourceNotification.getMessage("ngo.upload.report.path", null, null)
				+ngo.getName().toUpperCase()+"_" +"Detailed_SOE_" + timestampFormat.format(new Date())	+ ".xls";
		
		ByteArrayInputStream in = new ByteArrayInputStream(reportFile);
		try {	
			// Locking the excel template.
			HSSFWorkbook workbook =  LockWorkbook.workbookLock(new HSSFWorkbook(in));
			 
			FileOutputStream fileOutputStream = new FileOutputStream(new File(outputPath));
			workbook.write(fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
		 }catch(IOException e){
			 LOGGER.error("Error description : "+collectUserModel.getUsername()+ "("
					 + email +")" +" : Action :"+ 
					 messageSourceNotification.getMessage("unhandled.exception.while.reading.workbook", null,null)+" : "+fullDateFormat.format(new Date()));
		 }
		 return outputPath;
	}

}
