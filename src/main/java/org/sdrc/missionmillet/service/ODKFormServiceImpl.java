package org.sdrc.missionmillet.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.sdrc.missionmillet.domain.CollectUser;
import org.sdrc.missionmillet.domain.TypeDetails;
import org.sdrc.missionmillet.model.Mail;
import org.sdrc.missionmillet.odk.repository.CRPRegistrationRepository;
import org.sdrc.missionmillet.repository.AreaRepository;
import org.sdrc.missionmillet.repository.CollectUserRepository;
import org.sdrc.missionmillet.repository.TypeDetailsRepository;
import org.sdrc.missionmillet.util.LockWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Checking all the odk form submission status and sending an email to the user. 
 * 
 * @author Subrata
 *
 */
@Service
public class ODKFormServiceImpl implements ODKFormService{
	
	@Autowired
	private CRPRegistrationRepository crpRegistrationRepository;
	
	@Autowired
	private AreaRepository areaRepository;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private CollectUserRepository collectUserRepository;
	
	@Autowired
	private TypeDetailsRepository typeDetailsRepository;
	
	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;
	
	private static final Logger LOGGER = LoggerFactory.getLogger("LOGGER");
	private SimpleDateFormat timestampFormat = new SimpleDateFormat("ddMMyyyyHHmmssSSSSS");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	/**
	 * This method will execute by the scheduler
	 */
	@Scheduled(cron="0 0 2 1/1 * ?")
	@Override
	@Transactional
	public void formSubmissionStatus() {
		
		/**
		 * Getting all the area list
		 */
		List<Object[]> areaList = areaRepository.getArea();
		
		Map<String, String> areaMap = new HashMap<String, String>();
		/**
		 * Storing areaCode and areaName in a map
		 */
		areaList.forEach(area->areaMap.put(String.valueOf(area[0]), String.valueOf(area[1])));
		
		Map<Double, String> farmerMap = new HashMap<Double, String>();
		/**
		 * Getting all farmers list
		 */
		List<Object[]> farmerList = areaRepository.getAllFarmers();
		farmerList.forEach(value->farmerMap.put(Double.parseDouble(value[0].toString()), String.valueOf(value[1])));
		
		Map<Double, String> seedCenterNameMap = new HashMap<Double, String>();
		/**
		 * Getting all seed center name list
		 */
		List<Object[]> seedCenterNameList = areaRepository.getAllSeedCenterName();
		seedCenterNameList.forEach(value->seedCenterNameMap.put(Double.parseDouble(value[0].toString()), String.valueOf(value[1])));
		
		/**
		 * Getting all custom hiring center name list
		 */
		Map<Double, String> customHiringCenterMap = new HashMap<Double, String>();
		List<Object[]> customHiringCenterList = areaRepository.getAllcustomHiringCenterName();
		customHiringCenterList.forEach(value->customHiringCenterMap.put(Double.parseDouble(value[0].toString()), String.valueOf(value[1])));
		
		/**
		 * Getting all entrepreneur name list
		 */
		Map<Double, String> entrepreneurMap = new HashMap<Double, String>();
		List<Object[]> entrepreneurList = areaRepository.getAllEntrepreneurName();
		entrepreneurList.forEach(value->entrepreneurMap.put(Double.parseDouble(value[0].toString()), String.valueOf(value[1])));
		
		/**
		 * Getting month names from mst_type_details
		 */
		Map<Integer, String> monthNames = new HashMap<Integer, String>();
		List<TypeDetails> monthList = typeDetailsRepository
				.findByTypeTypeId(Integer.valueOf(messageSourceNotification.getMessage("month.id", null, null))); 
		/**
		 * Storing description and type_detail_name in a map
		 */
		monthList.forEach(typeDetail->monthNames.put(Integer.parseInt(typeDetail.getDescription()), typeDetail.getTypeDetailName()));
		/**
		 * Getting components names from mst_type_details
		 */
		Map<Integer, String> components = new HashMap<Integer, String>();
		List<TypeDetails> componentList = typeDetailsRepository
				.findByTypeTypeId(Integer.valueOf(messageSourceNotification.getMessage("components.id", null, null))); 
		/**
		 * Storing description and type_detail_name in a map
		 */
		componentList.forEach(typeDetail->components.put(Integer.parseInt(typeDetail.getDescription()), typeDetail.getTypeDetailName()));

		/**
		 * Getting role names from mst_type_details
		 */
		Map<Integer, String> roleMap = new HashMap<Integer, String>();
		List<TypeDetails> roleList = typeDetailsRepository
				.findByTypeTypeId(Integer.valueOf(messageSourceNotification.getMessage("role.id", null, null))); 
		/**
		 * Storing description and type_detail_name in a map
		 */
		roleList.forEach(typeDetail->roleMap.put(Integer.parseInt(typeDetail.getDescription()), typeDetail.getTypeDetailName()));
		/**
		 * Getting visit level names from mst_type_details
		 */
		Map<Integer, String> visitLevelMap = new HashMap<Integer, String>();
		List<TypeDetails> visitLevelList = typeDetailsRepository
				.findByTypeTypeId(Integer.valueOf(messageSourceNotification.getMessage("visit.level.id", null, null))); 
		/**
		 * Storing description and type_detail_name in a map
		 */
		visitLevelList.forEach(typeDetail->visitLevelMap.put(Integer.parseInt(typeDetail.getDescription()), typeDetail.getTypeDetailName()));

		/**
		 * Getting meeting level names from mst_type_details
		 */
		Map<Integer, String> meetingLevelMap = new HashMap<Integer, String>();
		List<TypeDetails> meetingLevelList = typeDetailsRepository
				.findByTypeTypeId(Integer.valueOf(messageSourceNotification.getMessage("meetinglevel.id", null, null))); 
		/**
		 * Storing description and type_detail_name in a map
		 */
		meetingLevelList.forEach(typeDetail->meetingLevelMap.put(Integer.parseInt(typeDetail.getDescription()), typeDetail.getTypeDetailName()));

		/**
		 * Creating an excel workbook.
		 */
		Workbook workbook = new HSSFWorkbook();
		
		/**
		 * If ODK form submission is incomplete found then mailFlag is true otherwise false.  
		 */
		boolean mailFlag = false;
		
		/**
		 * Getting the column names from messages.properties file.
		 */
		List<String> listOfColumnName = Arrays.asList((messageSourceNotification.getMessage("crp.registration", null, null)).split(","));
		
		Map<String, Integer> allFormDetails = new LinkedHashMap<String, Integer>();
		/**
		 *  Specifying the excel sheet name.
		 */
		String odkFormNameSheet = "CRP Registration";
		/**
		 * getting incomplete ODK form submission
		 */
		List<Object[]> crpRegistration = crpRegistrationRepository.getCRPRegistration();
		/**
		 * checking the size of crpRegistration. If it is greater than 0, then mailFlag is true.
		 */
		if(crpRegistration.size()>0)
			mailFlag = true;
		allFormDetails.put(odkFormNameSheet, crpRegistration.size());
		/**
		 * Generating an sheet in the workbook and putting all the crpRegistration values.
		 */
		generateExcel(crpRegistration, workbook, odkFormNameSheet, listOfColumnName, 
				areaMap, components, monthNames, null, null, null, null, null, null, null);
		/**
		 * Getting the column names from messages.properties file.
		 */
		listOfColumnName = Arrays.asList((messageSourceNotification.getMessage("custom.hiring.center.checklist", null, null)).split(","));
		/**
		 *  Specifying the excel sheet name.
		 */
		odkFormNameSheet = "Custom Hiring Center Checklist";
		List<Object[]> hiringCenterChecklist = crpRegistrationRepository.getHiringCenterChecklist();
		/**
		 * checking the size of hiringCenterChecklist. If it is greater than 0, then mailFlag is true.
		 */
		if(hiringCenterChecklist.size()>0)
			mailFlag = true;
		allFormDetails.put(odkFormNameSheet, hiringCenterChecklist.size());
		/**
		 * Generating an sheet in the workbook and putting all the hiringCenterChecklist values.
		 */
		generateExcel(hiringCenterChecklist, workbook, odkFormNameSheet, listOfColumnName, 
				areaMap, components, monthNames, null, null, null, customHiringCenterMap, null, null, null);
		/**
		 * Getting the column names from messages.properties file.
		 */
		listOfColumnName = Arrays.asList((messageSourceNotification.getMessage("farmer.registration", null, null)).split(","));
		/**
		 *  Specifying the excel sheet name.
		 */
		odkFormNameSheet = "Mission Millet Farmer Registration";
		List<Object[]> farmerRegistration = crpRegistrationRepository.getFarmerRegistration();
		/**
		 * checking the size of farmerRegistration. If it is greater than 0, then mailFlag is true.
		 */
		if(farmerRegistration.size()>0)
			mailFlag = true;
		allFormDetails.put(odkFormNameSheet, farmerRegistration.size());
		/**
		 * Generating an sheet in the workbook and putting all the farmerRegistration values.
		 */
		generateExcel(farmerRegistration, workbook, odkFormNameSheet, listOfColumnName, 
				areaMap, components, monthNames, null, null, null, null, null, null, null);
		/**
		 * Getting the column names from messages.properties file.
		 */
		listOfColumnName = Arrays.asList((messageSourceNotification.getMessage("custom.hiring.center.member.registration", null, null)).split(","));
		/**
		 *  Specifying the excel sheet name.
		 */
		odkFormNameSheet = "Custom Hiring Center Member Registration";
		List<Object[]> hiringCenterMemberRegistration = crpRegistrationRepository.getHiringCenterMemberRegistration();
		/**
		 * checking the size of hiringCenterMemberRegistration. If it is greater than 0, then mailFlag is true.
		 */
		if(hiringCenterMemberRegistration.size()>0)
			mailFlag = true;
		allFormDetails.put(odkFormNameSheet, hiringCenterMemberRegistration.size());
		/**
		 * Generating an sheet in the workbook and putting all the hiringCenterMemberRegistration values.
		 */
		generateExcel(hiringCenterMemberRegistration, workbook, odkFormNameSheet, listOfColumnName, 
				areaMap, components, monthNames, null, null, null, null, roleMap, null, null);
		/**
		 * Getting the column names from messages.properties file.
		 */
		listOfColumnName = Arrays.asList((messageSourceNotification.getMessage("custom.hiring.center.registration", null, null)).split(","));
		/**
		 *  Specifying the excel sheet name.
		 */
		odkFormNameSheet = "Custom Hiring Center Registration";
		List<Object[]> hiringCenterRegistration = crpRegistrationRepository.getHiringCenterRegistration();
		/**
		 * checking the size of hiringCenterRegistration. If it is greater than 0, then mailFlag is true.
		 */
		if(hiringCenterRegistration.size()>0)
			mailFlag = true;
		allFormDetails.put(odkFormNameSheet, hiringCenterRegistration.size());
		/**
		 * Generating an sheet in the workbook and putting all the hiringCenterRegistration values.
		 */
		generateExcel(hiringCenterRegistration, workbook, odkFormNameSheet, listOfColumnName, 
				areaMap, components, monthNames, null, null, null, null, null, null, null);
		/**
		 * Getting the column names from messages.properties file.
		 */
		listOfColumnName = Arrays.asList((messageSourceNotification.getMessage("entrepreneurship.registration", null, null)).split(","));
		/**
		 *  Specifying the excel sheet name.
		 */
		odkFormNameSheet = "Entrepreneurship Registration";
		List<Object[]> entrepreneurshipRegistration = crpRegistrationRepository.getEntrepreneurshipRegistration();
		/**
		 * checking the size of entrepreneurshipRegistration. If it is greater than 0, then mailFlag is true.
		 */
		if(entrepreneurshipRegistration.size()>0)
			mailFlag = true;
		allFormDetails.put(odkFormNameSheet, entrepreneurshipRegistration.size());
		/**
		 * Generating an sheet in the workbook and putting all the entrepreneurshipRegistration values.
		 */
		generateExcel(entrepreneurshipRegistration, workbook, odkFormNameSheet, listOfColumnName, 
				areaMap, components, monthNames, null, null, null, null, null, null, null);
		/**
		 * Getting the column names from messages.properties file.
		 */
		listOfColumnName = Arrays.asList((messageSourceNotification.getMessage("field.visit.checklist", null, null)).split(","));
		/**
		 *  Specifying the excel sheet name.
		 */
		odkFormNameSheet = "Field Visit Checklist";
		List<Object[]> fieldVisitChecklist = crpRegistrationRepository.getFieldVisitChecklist();
		/**
		 * checking the size of fieldVisitChecklist. If it is greater than 0, then mailFlag is true.
		 */
		if(fieldVisitChecklist.size()>0)
			mailFlag = true;
		allFormDetails.put(odkFormNameSheet, fieldVisitChecklist.size());
		/**
		 * Generating an sheet in the workbook and putting all the fieldVisitChecklist values.
		 */
		generateExcel(fieldVisitChecklist, workbook, odkFormNameSheet, listOfColumnName, 
				areaMap, components, monthNames, null, null, null, null, null, visitLevelMap, null);
		/**
		 * Getting the column names from messages.properties file.
		 */
		listOfColumnName = Arrays.asList((messageSourceNotification.getMessage("meeting.checklist", null, null)).split(","));
		/**
		 *  Specifying the excel sheet name.
		 */
		odkFormNameSheet = "Meeting Checklist";
		List<Object[]> meetingChecklist = crpRegistrationRepository.getMeetingChecklist();
		/**
		 * checking the size of meetingChecklist. If it is greater than 0, then mailFlag is true.
		 */
		if(meetingChecklist.size()>0)
			mailFlag = true;
		allFormDetails.put(odkFormNameSheet, meetingChecklist.size());
		/**
		 * Generating an sheet in the workbook and putting all the meetingChecklist values.
		 */
		generateExcel(meetingChecklist, workbook, odkFormNameSheet, listOfColumnName, 
				areaMap, components, monthNames, null, null, null, null, null, null, meetingLevelMap);
		/**
		 * Getting the column names from messages.properties file.
		 */
		listOfColumnName = Arrays.asList((messageSourceNotification.getMessage("processing.checklist", null, null)).split(","));
		/**
		 *  Specifying the excel sheet name.
		 */
		odkFormNameSheet = "Processing Checklist";
		List<Object[]> processingChecklist = crpRegistrationRepository.getProcessingChecklist();
		/**
		 * checking the size of processingChecklist. If it is greater than 0, then mailFlag is true.
		 */
		if(processingChecklist.size()>0)
			mailFlag = true;
		allFormDetails.put(odkFormNameSheet, processingChecklist.size());
		/**
		 * Generating an sheet in the workbook and putting all the processingChecklist values.
		 */
		generateExcel(processingChecklist, workbook, odkFormNameSheet, listOfColumnName, 
				areaMap, components, monthNames, null, null, entrepreneurMap, null, null, null, null);
		/**
		 * Getting the column names from messages.properties file.
		 */
		listOfColumnName = Arrays.asList((messageSourceNotification.getMessage("production.checklist", null, null)).split(","));
		/**
		 *  Specifying the excel sheet name.
		 */
		odkFormNameSheet = "Production Checklist";
		List<Object[]> productionChecklist = crpRegistrationRepository.getProductionChecklist();
		/**
		 * checking the size of productionChecklist. If it is greater than 0, then mailFlag is true.
		 */
		if(productionChecklist.size()>0)
			mailFlag = true;
		allFormDetails.put(odkFormNameSheet, productionChecklist.size());
		/**
		 * Generating an sheet in the workbook and putting all the productionChecklist values.
		 */
		generateExcel(productionChecklist, workbook, odkFormNameSheet, listOfColumnName,
				areaMap, components, monthNames, farmerMap, null, null, null, null, null, null);
		/**
		 * Getting the column names from messages.properties file.
		 */
		listOfColumnName = Arrays.asList((messageSourceNotification.getMessage("seed.center.registration", null, null)).split(","));
		/**
		 *  Specifying the excel sheet name.
		 */
		odkFormNameSheet = "Seed Center Registration";
		List<Object[]> seedCenterRegistration = crpRegistrationRepository.getSeedCenterRegistration();
		/**
		 * checking the size of seedCenterRegistration. If it is greater than 0, then mailFlag is true.
		 */
		if(seedCenterRegistration.size()>0)
			mailFlag = true;
		allFormDetails.put(odkFormNameSheet, seedCenterRegistration.size());
		/**
		 * Generating an sheet in the workbook and putting all the seedCenterRegistration values.
		 */
		generateExcel(seedCenterRegistration, workbook, odkFormNameSheet, listOfColumnName, 
				areaMap, components, monthNames, null, null, null, null, null, null, null);
		/**
		 * Getting the column names from messages.properties file.
		 */
		listOfColumnName = Arrays.asList((messageSourceNotification.getMessage("seed.production.in.seed.center.checklist", null, null)).split(","));
		/**
		 *  Specifying the excel sheet name.
		 */
		odkFormNameSheet = "Seed Production In Seed Center Checklist";
		List<Object[]> seedProductionInSeedCenterChecklist = crpRegistrationRepository.getSeedProductionInSeedCenterChecklist();
		/**
		 * checking the size of seedProductionInSeedCenterChecklist. If it is greater than 0, then mailFlag is true.
		 */
		if(seedProductionInSeedCenterChecklist.size()>0)
			mailFlag = true;
		allFormDetails.put(odkFormNameSheet, seedProductionInSeedCenterChecklist.size());
		/**
		 * Generating an sheet in the workbook and putting all the seedProductionInSeedCenterChecklist values.
		 */
		generateExcel(seedProductionInSeedCenterChecklist, workbook, odkFormNameSheet, listOfColumnName, 
				areaMap, components, monthNames, null, seedCenterNameMap, null, null, null, null, null);
		/**
		 * Getting the column names from messages.properties file.
		 */
		listOfColumnName = Arrays.asList((messageSourceNotification.getMessage("seed.producer.checklist", null, null)).split(","));
		/**
		 *  Specifying the excel sheet name.
		 */
		odkFormNameSheet = "Seed Producer Checklist";
		List<Object[]> seedProducerChecklist = crpRegistrationRepository.getSeedProducerChecklist();
		/**
		 * checking the size of seedProducerChecklist. If it is greater than 0, then mailFlag is true.
		 */
		if(seedProducerChecklist.size()>0)
			mailFlag = true;
		allFormDetails.put(odkFormNameSheet, seedProducerChecklist.size());
		/**
		 * Generating an sheet in the workbook and putting all the seedProducerChecklist values.
		 */
		generateExcel(seedProducerChecklist, workbook, odkFormNameSheet, listOfColumnName, 
				areaMap, components, monthNames, null, null, null, null, null, null, null);
		/**
		 * Getting the column names from messages.properties file.
		 */
		listOfColumnName = Arrays.asList((messageSourceNotification.getMessage("seed.producer.registration", null, null)).split(","));
		/**
		 *  Specifying the excel sheet name.
		 */
		odkFormNameSheet = "Seed Producer Registration";
		List<Object[]> seedProducerRegistration = crpRegistrationRepository.getSeedProducerRegistration();
		/**
		 * checking the size of seedProducerRegistration. If it is greater than 0, then mailFlag is true.
		 */
		if(seedProducerRegistration.size()>0)
			mailFlag = true;
		allFormDetails.put(odkFormNameSheet, seedProducerRegistration.size());
		/**
		 * Generating an sheet in the workbook and putting all the seedProducerRegistration values.
		 */
		generateExcel(seedProducerRegistration, workbook, odkFormNameSheet, listOfColumnName, 
				areaMap, components, monthNames, null, seedCenterNameMap, null, null, null, null, null);
		/**
		 * Getting the column names from messages.properties file.
		 */
		listOfColumnName = Arrays.asList((messageSourceNotification.getMessage("training.checklist", null, null)).split(","));
		/**
		 *  Specifying the excel sheet name.
		 */
		odkFormNameSheet = "Training Checklist";
		List<Object[]> trainingChecklist = crpRegistrationRepository.getTrainingChecklist();
		/**
		 * checking the size of trainingChecklist. If it is greater than 0, then mailFlag is true.
		 */
		if(trainingChecklist.size()>0)
			mailFlag = true;
		allFormDetails.put(odkFormNameSheet, trainingChecklist.size());
		/**
		 * Generating an sheet in the workbook and putting all the trainingChecklist values.
		 */
		generateExcel(trainingChecklist, workbook, odkFormNameSheet, listOfColumnName, 
				areaMap, components, monthNames, null, null, null, null, null, null, null);
		
		/**
		 * Getting a align center style with color
		 */
		CellStyle styleForColorHeader = LockWorkbook.getStyleForColorHeader(workbook);
		
		/**
		 * Getting a align center style
		 */
		CellStyle styleForCenter = LockWorkbook.getStyleForCenter(workbook);
		
		/**
		 * Creating a sheet in 0(zero) index which contain incomplete ODK form details.
		 */
		
		Sheet sh = workbook.createSheet("Incomplete ODK form details");
		workbook.setSheetOrder(sh.getSheetName(), 0);
		int rowNum = 0;
		int colNum = 0;
		/**
		 * creating a row in the sheet
		 */
		Row row = sh.createRow(rowNum);
		/**
		 * creating a cell in the sheet
		 */
		Cell cell = row.createCell(colNum);
		cell.setCellValue("ODK Form name");
		cell.setCellStyle(styleForColorHeader);
		colNum++;
		cell = row.createCell(colNum);
		cell.setCellValue("Incomplete Submission");
		cell.setCellStyle(styleForColorHeader);
		rowNum = 1;
		colNum = 0;
		/**
		 * Iterating all the form names  
		 */
		for (Map.Entry<String, Integer> entry : allFormDetails.entrySet()){
			row = sh.createRow(rowNum);
			row.setHeight((short)300);
			cell = row.createCell(colNum);
			sh.setColumnWidth(cell.getColumnIndex(), 9900);
			cell.setCellValue(entry.getKey());
			cell.setCellStyle(styleForCenter);
			colNum++;
			cell = row.createCell(colNum);
			sh.setColumnWidth(cell.getColumnIndex(), 4500);
			cell.setCellValue(entry.getValue());
			cell.setCellStyle(styleForCenter);
			
			colNum=0;
			row = null;
			cell = null;
			rowNum++;
	    }
		/**
		 * generating an excel sheet. 
		 * 
		 */
		String path = messageSourceNotification.getMessage("ngo.upload.report.path", null, null);
		String fileName = "ODK_details"+"_"+timestampFormat.format(new Date())+".xls";
		String outputPath = path+fileName;
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(new File(outputPath));
			workbook.write(fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
			workbook.close();
		} catch (IOException e) {
			LOGGER.error(messageSourceNotification.getMessage("unable.towrite.excel", null, null)+" : "+timestampFormat.format(new Date()));
		}
		/**
		 * Putting the file name and path in a map 
		 */
		Map<String, String> map = new HashMap<String, String>();
		map.put(fileName, path);
		
		/**
		 * Getting the Admin user details from mst_collect_user table
		 */
		CollectUser user = collectUserRepository.findByUserId(Integer.parseInt(messageSourceNotification.getMessage("user.id", null, null)));

		/**
		 * Adding subject and message in the Mail model.
		 */
		try {
			Mail mail=new Mail();
			mail.setToEmailIds(Arrays.asList(user.getEmailId()));
			mail.setToUserName(user.getUsername());
			mail.setSubject(messageSourceNotification.getMessage("incomplete.mail.subject", null, null)+dateFormat.format(new Date()));
			mail.setMessage(messageSourceNotification.getMessage("incomplete.mail.message", null, null));
			mail.setFromUserName(messageSourceNotification.getMessage("incomplete.mail.from.user", null, null));
			mail.setAttachments(map);
			mail.setRegards(null);
			/**
			 * If mailFlag is true then, sending an email to Admin
			 */
			if(mailFlag==true)
				mailService.sendMail(mail);
			/**
			 * deleting the file 
			 */
			new File(outputPath).delete();
		} catch (Exception e) {
			LOGGER.error(messageSourceNotification.getMessage("error.while.sending.mail", null, null)+" : "+timestampFormat.format(new Date()));
		} 
	}

	/**
	 * Saving the form details in the excel sheet.
	 * 
	 * @param crpRegistration
	 * @param workbook
	 * @param odkFormNameSheet
	 * @return workbook
	 * 
	 * @author Subrata
	 */ 
	private Workbook generateExcel(List<Object[]> listObj, Workbook workbook, String odkFormNameSheet, 
					List<String> listOfColumnName, Map<String, String> areaMap, Map<Integer, String> components,
					Map<Integer, String> monthNames, Map<Double, String> farmerMap, Map<Double, String> seedCenterNameMap,
					Map<Double, String> entrepreneurMap, Map<Double, String> customHiringCenterMap,
					Map<Integer, String> roleMap, Map<Integer, String> visitLevelMap, Map<Integer, String> meetingLevelMap) {
		
		/**
		 * Getting a style with text align center
		 */
		CellStyle styleForCenter = LockWorkbook.getStyleForCenter(workbook);
		
		/**
		 * Getting a style with text align center with color
		 */
		CellStyle styleForColorHeader = LockWorkbook.getStyleForColorHeader(workbook);
		
		/**
		 * Getting a style with text align center with color
		 */
		CellStyle styleForTitle = LockWorkbook.getStyleForTitle(workbook);
		/**
		 * According to the ODK form name, creating a sheet in the workbook
		 */
		Sheet sheet = workbook.createSheet(odkFormNameSheet);
		
		/**
		 * Creating a row in the sheet for ODK form Name
		 */
		Row row = sheet.createRow(0);
		row.setHeight((short)400);
		Cell cell = row.createCell(0);
		/**
		 * Merging cell
		 */
		if(listOfColumnName.size()==4)
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,3)); 
		else
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,listOfColumnName.size()-1));
		cell.setCellValue("Form : "+odkFormNameSheet);
		cell.setCellStyle(styleForTitle);
		row = null;
		cell = null;
		/**
		 * Creating a row in the sheet for column names
		 */
		row = sheet.createRow(1);
		/**
		 * Iterating and storing all the required column name in the sheet
		 */
		for(int i = 0;i<listOfColumnName.size();i++){
			cell = row.createCell(i);
			sheet.setColumnWidth(cell.getColumnIndex(), 5500);
			cell.setCellValue(listOfColumnName.get(i));
			cell.setCellStyle(styleForColorHeader);
		}
		
		row = null;
		cell = null;
		/**
		 * In first row i.e 0 we are setting the ODK form name and in the second row i.e 1 we are setting column name.
		 * So from third row we are setting the data i.e 2.
		 */
		int toEnterData = 2;
		/**
		 * Iterating listObj(contains "is_complete" is false) and putting all the ODK form data in the sheet 
		 */
		for(int i = 0; i < listObj.size(); i++){
			row = sheet.createRow(toEnterData);
			Object[] object = listObj.get(i);

			for(int j = 0; j < Array.getLength(object); j++){
				cell = row.createCell(j);
				sheet.setColumnWidth(cell.getColumnIndex(), 5500);
				if(j==1){
					String value = object[j].toString().split(":")[1].split("\\|")[0];
					cell.setCellValue(value); 
				}else if((j==4 || j==5 || j==6 || j==7)){
					cell.setCellValue(object[j]!=null?areaMap.get(object[j].toString()):"");
				}else if(j==0){
					sheet.setColumnWidth(cell.getColumnIndex(), 11000);
					cell.setCellValue(object[j].toString());
				}else if(j==9 && odkFormNameSheet.equals("Training Checklist")){
					if((object[j]!=null) && (components.get(object[j].toString())!=null || components.get(Integer.parseInt(object[j].toString()))!=null)){
						cell.setCellValue(components.get(Integer.parseInt(object[j].toString())));
					}else{
						cell.setCellValue(object[j]!=null?object[j].toString():"");
					}
				}else if(j==9 && odkFormNameSheet.equals("Production Checklist")){
					if(farmerMap.get(Double.parseDouble(object[j].toString())) != null){
						cell.setCellValue(farmerMap.get(Double.parseDouble(object[j].toString())));
					}else{
						cell.setCellValue(object[j]!=null?object[j].toString():"");
					}
				}else if(j==9 && odkFormNameSheet.equals("Seed Producer Registration")){
					if(seedCenterNameMap.get(Double.parseDouble(object[j].toString())) != null){
						cell.setCellValue(seedCenterNameMap.get(Double.parseDouble(object[j].toString())));
					}else{
						cell.setCellValue(object[j]!=null?object[j].toString():"");
					}
				}else if(j==9 && odkFormNameSheet.equals("Seed Production In Seed Center Checklist")){
					if(seedCenterNameMap.get(Double.parseDouble(object[j].toString())) != null){
						cell.setCellValue(seedCenterNameMap.get(Double.parseDouble(object[j].toString())));
					}else{
						cell.setCellValue(object[j]!=null?object[j].toString():"");
					}
				}else if(j==9 && odkFormNameSheet.equals("Custom Hiring Center Checklist")){
					if(customHiringCenterMap.get(Double.parseDouble(object[j].toString())) != null){
						cell.setCellValue(customHiringCenterMap.get(Double.parseDouble(object[j].toString())));
					}else{
						cell.setCellValue(object[j]!=null?object[j].toString():"");
					}
				}else if(j==9 && odkFormNameSheet.equals("Processing Checklist")){
					if(entrepreneurMap.get(Double.parseDouble(object[j].toString())) != null){
						cell.setCellValue(entrepreneurMap.get(Double.parseDouble(object[j].toString())));
					}else{
						cell.setCellValue(object[j]!=null?object[j].toString():"");
					}
				}else if(j==9 && odkFormNameSheet.equals("Field Visit Checklist")){
					if(visitLevelMap.get(Integer.parseInt(object[j].toString())) != null){
						cell.setCellValue(visitLevelMap.get(Integer.parseInt(object[j].toString())));
					}else{
						cell.setCellValue(object[j]!=null?object[j].toString():"");
					}
				}else if(j==9 && odkFormNameSheet.equals("Meeting Checklist")){
					if(meetingLevelMap.get(Integer.parseInt(object[j].toString()))!=null){
						cell.setCellValue(meetingLevelMap.get(Integer.parseInt(object[j].toString())));
					}else{
						cell.setCellValue(object[j]!=null?object[j].toString():"");
					}
				}else if(j==10 && odkFormNameSheet.equals("Processing Checklist")){
					if((object[j]!=null) && (monthNames.get(Integer.parseInt(object[j].toString()))!=null)){
						cell.setCellValue(monthNames.get(Integer.parseInt(object[j].toString())));
					}else{
						cell.setCellValue(object[j]!=null?object[j].toString():"");
					}
				}else if(j==10 && odkFormNameSheet.equals("Custom Hiring Center Member Registration")){
					if((object[j]!=null) && (roleMap.get(Integer.parseInt(object[j].toString()))!=null)){
						cell.setCellValue(roleMap.get(Integer.parseInt(object[j].toString())));
					}else{
						cell.setCellValue(object[j]!=null?object[j].toString():"");
					}
				}else {
					cell.setCellValue(object[j]!=null?object[j].toString():"");
				}
				cell.setCellStyle(styleForCenter);
			}
			
			toEnterData++;
		}
		return workbook;
	}
}
