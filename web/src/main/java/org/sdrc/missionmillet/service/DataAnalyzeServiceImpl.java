package org.sdrc.missionmillet.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.sdrc.missionmillet.domain.CollectUser;
import org.sdrc.missionmillet.domain.NGO;
import org.sdrc.missionmillet.domain.NGOSoEReport;
import org.sdrc.missionmillet.model.Mail;
import org.sdrc.missionmillet.repository.CollectUserRepository;
import org.sdrc.missionmillet.repository.NGORepository;
import org.sdrc.missionmillet.repository.NGOSoEReportRepository;
import org.sdrc.missionmillet.util.LockWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Subham Ashish(subham@sdrc.co.in)
 * 
 *@Description Report on NGOs pending to upload their SoE by 13th Of each month  
 */
@Service
public class DataAnalyzeServiceImpl implements DataAnalyzeService {

	@Autowired
	private NGOSoEReportRepository nGOSoEReportRepository;
	
	@Autowired
	private NGORepository nGORepository;
	
	@Autowired
	private CollectUserRepository collectUserRepository;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private LockWorkbook lockWorkbook;
	
	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;
	
	private static final Logger LOGGER = LoggerFactory.getLogger("LOGGER");
	
	/**
	 * This method executes on every month except October on 13th at 10:00am
	 */
	@Scheduled(cron="0 0 10 13 * ?")
	public void monthlyDataAnalyze() {
		
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		
		// as we are fetching data for previous month
		int month = cal.get(Calendar.MONTH)-1;
		
		// in january month, fetching december month data
		if(month==-1){
			month=11;
			year=year-1;
		}
		
		//blocking it to execute in december month
		if(month!=8)
			dataAnalysis(month,year);
	}
	
	/**
	 * This method executes only in the month of october on 7th at 10:15am
	 */
	@Scheduled(cron="0 15 10 7 10 ?")
	public void monthlyDataAnalyzeOctober() {
		
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)-1;
		
		dataAnalysis(month,year);
		
	}
	
	private void dataAnalysis(int month,int year){
		
		//get all the NGOSoE report for the present month
		List<NGOSoEReport> ngoSoeList = nGOSoEReportRepository.findByMonthAndYearAndIsLiveTrue(month,year);
		
		// all the submitted ngo id is stored here
		List<Integer> ngoId = new ArrayList<>();
		
		//iterating it to store submitted ngoId
		ngoSoeList.forEach(ngoSoE->ngoId.add(ngoSoE.getNgo().getId()));
		
		// to attach file
		Map<String, String> attachmentMap = new HashMap<String, String>();
		try{
			List<NGO> ngoList ;
			
			if(ngoSoeList.isEmpty())
				//no one has submitted, so get all the NGOs
				ngoList = nGORepository.findAll();
			else
				//get all the list of ngos which are not there in ngoId
				ngoList = nGORepository.findByIdNotIn(ngoId);
			
			
			if(ngoList.isEmpty()){
				//send mail, all ngos has submitted SoE
				Mail mail = sendMailMessage(messageSourceNotification.getMessage("ngo.soe.report.mail.body.allsubmitted", null,null),month,attachmentMap);
				mailService.sendMail(mail);
			}
			
			HSSFWorkbook workbook=null;
			HSSFSheet sheet=null;
		
			if(!ngoList.isEmpty()){
				//find all the user whose ngoid is present in ngoList.
				List<CollectUser> userList =  collectUserRepository.findByNgoIn(ngoList);
				Map<Integer,String> userMap = new HashMap<>();
				//making ngoId as a key to get userName based on ngoId
				for(CollectUser user : userList){
					if(userMap.containsKey(user.getNgo().getId()))
						userMap.put(user.getNgo().getId(),user.getUsername());
					else
						userMap.put(user.getNgo().getId(),user.getUsername());
					}
				
				workbook = new HSSFWorkbook();
				sheet = workbook.createSheet("data_analyze");
				CellStyle headerStyle = LockWorkbook.getStyleForColorHeader(workbook);
				CellStyle getStyleForCenter = LockWorkbook.getStyleForCenter(workbook);
				
				Row row;
				Cell cell;
				
				//HEADING
				row = sheet.createRow(0);
				cell = row.createCell(0);
				cell.setCellStyle(headerStyle);
				sheet= lockWorkbook.doMerge(0,0,0,4,sheet);
				cell.setCellValue("MissionMillet-Pending SoE Submisison");
				
				//Month
				row = sheet.createRow(1);
				cell = row.createCell(0);
				cell.setCellStyle(headerStyle);
				sheet= lockWorkbook.doMerge(1,1,0,4,sheet);
				cell.setCellValue("Month "+" : "+Month.of(month+1).name());
				
				//deadLinedate
				row = sheet.createRow(2);
				cell = row.createCell(0);
				cell.setCellStyle(headerStyle);
				sheet= lockWorkbook.doMerge(2,2,0,4,sheet);
				cell.setCellValue("Date of Report Generation"+" : "+new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
				
				String headerValue = messageSourceNotification.getMessage("ngo.soe.report.heading", null,null);
				String[] fixedHederValue = headerValue.split(",");
				
				row = sheet.createRow(3);
				
				///setting all the header values here
				for(int index=0;index<fixedHederValue.length;index++){
					cell = row.createCell(index);
					sheet.setColumnWidth(cell.getColumnIndex(), 4000);
					sheet.setHorizontallyCenter(true);
					cell.setCellStyle(headerStyle);
					cell.setCellValue(fixedHederValue[index]);
				}
				
				int rowIndex = 4;
				for(NGO ngo : ngoList){
					row = sheet.createRow(rowIndex);
					//sino
					cell = row.createCell(0);
					cell.setCellStyle(getStyleForCenter);
					cell.setCellValue(rowIndex-3);
					//district
					cell= row.createCell(1);
					cell.setCellValue(ngo.getDistrictId().getAreaName());
					cell.setCellStyle(getStyleForCenter);
					//username
					cell= row.createCell(2);
					cell.setCellStyle(getStyleForCenter);
					cell.setCellValue(userMap.get(ngo.getId()));
					//block
					cell= row.createCell(3);
					cell.setCellStyle(getStyleForCenter);
					cell.setCellValue(ngo.getBlockId().getAreaName());
					//ngo name
					cell= row.createCell(4);
					cell.setCellStyle(getStyleForCenter);
					cell.setCellValue(ngo.getName());
					
					rowIndex++;
					
					}	
				}	
			
				String path = messageSourceNotification.getMessage("wassan.upload.report.path", null, null);
				String fileName = "mission_millet"+new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())+".xls";
				String finalPath = path+fileName;
				FileOutputStream fos = new FileOutputStream(new File(finalPath));
				workbook.write(fos);
				
				fos.close();
				workbook.close();
				//Putting file name and path in a map to be attached
				attachmentMap.put(fileName, path);
				//again read the same file  and attached to mail
				Mail mail = sendMailMessage(messageSourceNotification.getMessage("ngo.soe.report.mail.body", null, null),month,attachmentMap);
				//sending mail
				mailService.sendMail(mail);
				//deleting path
				new File(finalPath).delete();
		}catch(Exception e){
			LOGGER.error("Error description : "+"while access dataAnalysis() method : "
					+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), e);
			throw new RuntimeException();
		}
		
	}
	
	/**
	 * @Description  Adding subject and message in the Mail model.
	 * @param message
	 * @param month
	 * @param attachmentMap
	 * @return
	 */
	
	private Mail sendMailMessage(String message,int month, Map<String, String> attachmentMap) {
		
		Mail mail = new Mail();
		mail.setSubject(messageSourceNotification.getMessage("pending.mail.subject", null, null));
		mail.setToUserName("Admin");
		mail.setFromUserName(messageSourceNotification.getMessage("incomplete.mail.from.user", null, null));
		mail.setMessage(message+Month.of(month+1).name());
		mail.setToEmailIds(Arrays.asList(messageSourceNotification.getMessage("ngo.soe.report.mail.tomailid", null,null)));
		mail.setAttachments(attachmentMap);
		mail.setRegards(null);
		return mail;
	}

	

}
