package org.sdrc.missionmillet.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.sdrc.missionmillet.domain.CollectUser;
import org.sdrc.missionmillet.domain.Configuration;
import org.sdrc.missionmillet.domain.NGO;
import org.sdrc.missionmillet.domain.NGOSoEUploadsStatus;
import org.sdrc.missionmillet.domain.SoETemplate;
import org.sdrc.missionmillet.domain.SoETransaction;
import org.sdrc.missionmillet.domain.TimePeriod;
import org.sdrc.missionmillet.domain.TypeDetails;
import org.sdrc.missionmillet.domain.UUIdGenerator;
import org.sdrc.missionmillet.model.CollectUserModel;
import org.sdrc.missionmillet.repository.CollectUserRepository;
import org.sdrc.missionmillet.repository.ConfigurationRepository;
import org.sdrc.missionmillet.repository.NGOSoEUploadsStatusRepository;
import org.sdrc.missionmillet.repository.SoETemplateRepository;
import org.sdrc.missionmillet.repository.SoETransactionRepository;
import org.sdrc.missionmillet.repository.TimePeriodRepository;
import org.sdrc.missionmillet.repository.UUIdGeneratorRepository;
import org.sdrc.missionmillet.util.Constants;
import org.sdrc.missionmillet.util.MissionMilletUtil;
import org.sdrc.missionmillet.util.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Maninee Mahapatra(maninee@sdrc.co.in)
 * 
 * @Description This is the Implementation class of DistrictUserTemplateService
 *              interface.
 */
@Service
public class DistrictUserTemplateServiceImpl implements DistrictUserTemplateService {

	@Autowired
	private SoETemplateRepository soETemplateRepository;
	@Autowired
	private SoETransactionRepository soETransactionRepository;
	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;
	@Autowired
	private TimePeriodRepository timePeriodRepository;
	@Autowired
	private NGOSoEUploadsStatusRepository ngoSoEUploadsStatusRepository;
	@Autowired
	private ConfigurationRepository configurationRepository;
	@Autowired
	private UUIdGeneratorRepository uuIdGeneratorRepository;
	@Autowired
	private StateManager stateManager;
	@Autowired
	DistrictUserDownloadTemplateService districtUserDownloadTemplateService;
	
	@Autowired
	private CollectUserRepository collectUserRepository;

	private SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final Logger LOGGER = LoggerFactory.getLogger("LOGGER");

	/**
	 * @Description This method upload SoE Template for Ngo by district user in
	 *              every six month with updated budget
	 * 
	 * @param file
	 * @param request
	 * @param ngoId
	 * @param timePeriodId
	 * @return String success
	 */
	@Override
	@Transactional
	public String uploadTemplate(MultipartFile file, HttpServletRequest request, Integer ngoId, Integer timePeriodId) {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL));
		
		if (collectUserModel == null) {
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null) + " "
					+ fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		String email = collectUserModel.getEmailId() == null? "": collectUserModel.getEmailId();
		try {
			if (!FilenameUtils.getExtension(file.getOriginalFilename()).equals("xls")) {
				
				LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
	                    + " : Action :"+ messageSourceNotification.getMessage("upload.anexcel.file", null, null) + " : "
			            + fullDateFormat.format(new Date()));
				return messageSourceNotification.getMessage("upload.anexcel.file", null, null)
						+ fullDateFormat.format(new Date());
			}
			HSSFWorkbook workbook = new HSSFWorkbook(MissionMilletUtil.getByteArrayStream(file.getBytes()));
			HSSFSheet spreadsheet = workbook.getSheet("uuid");
			if (spreadsheet == null) {
				workbook.close();
				LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
	                    + " : Action :"+ messageSourceNotification.getMessage("excel.sheet.not.matching", null, null) + " : "
			            + fullDateFormat.format(new Date()));
				return messageSourceNotification.getMessage("excel.sheet.not.matching", null, null);
			}
			HSSFSheet detailedSOE = workbook.getSheet("Detailed SOE");
			if (detailedSOE == null) {
				workbook.close();
				LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
	                    + " : Action :"+ messageSourceNotification.getMessage("excel.sheet.not.matching", null, null) + " : "
			            + fullDateFormat.format(new Date()));
				return messageSourceNotification.getMessage("excel.sheet.not.matching", null, null);
			}
			HSSFSheet sheet = workbook.getSheet("mm_check");
			if(sheet==null) {
				workbook.close();
				LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
	                    + " : Action :"+ messageSourceNotification.getMessage("excel.sheet.not.matching", null, null) + " : "
			            + fullDateFormat.format(new Date()));
				return messageSourceNotification.getMessage("excel.sheet.not.matching", null, null);
			}
			Row macroRow = sheet.getRow(0);
			Cell macroCell = macroRow.getCell(0);
			if (macroCell.getCellType() == 3 || macroCell.getBooleanCellValue() == false) {
				workbook.close();
				LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
	                    + " : Action :"+ messageSourceNotification.getMessage("macro.not.enable", null, null) + " : "
			            + fullDateFormat.format(new Date()));
				return messageSourceNotification.getMessage("macro.not.enable", null, null);
			} else {
				/**
				 * For month of April validate the UUID in the SoE template and Database, and
				 * then create a new entry in SoETransaction table. For a user, if the SoE
				 * template was not uploaded in April, then in month of October, validate the
				 * UUID, then create an new entry in SoETransaction table and
				 * NGOSoEUploadsStatus table. For a user, if the SoE template was uploaded in
				 * April, then in month of October, validate the UUID, then create an new entry
				 * in SoETransaction table.
				 */
				macroCell.setCellValue(false);

				SoETemplate dBuuidforSoe = soETemplateRepository.getUuidByNgoId(ngoId, timePeriodId);
				
				Row budgetRow = detailedSOE.getRow(72);
				Cell budgetCell = budgetRow.getCell(6);
				
				HSSFRow row = spreadsheet.getRow(0);
				HSSFCell cell = row.getCell(0);
				String sheetuuid = cell.getStringCellValue();

				row = null;
				cell = null;
				row = spreadsheet.getRow(3);
				cell = row.getCell(0);
				String sheetUUIDBiannual = cell.getStringCellValue();

				String halfyearlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.halfyearly",
						null, null);
				TimePeriod latestTimePeriod = timePeriodRepository.getTimePeriodofSixPeriodicity(halfyearlyPeriodicity,
						new Timestamp(new Date().getTime()));
				
				CollectUser user = collectUserRepository.getNgo(ngoId); 
				
				UUIdGenerator uuIdGenerator = uuIdGeneratorRepository.getUUIDdetailsForDistrict(user.getUserId(),
						sheetUUIDBiannual, latestTimePeriod.getTimePeriodId());
				if(uuIdGenerator == null){
					workbook.close();
					LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
		                    + " : Action :"+ messageSourceNotification.getMessage("upload.correct.file", null, null) + " : "
				            + fullDateFormat.format(new Date()));
					return messageSourceNotification.getMessage("upload.correct.file", null, null);
				}
				row = null;
				cell = null;

				row = spreadsheet.getRow(0);
				cell = row.getCell(0);
				
				if (uuIdGenerator.getUuid().equals(sheetUUIDBiannual)) {
					if (sheetuuid.equals(dBuuidforSoe.getUuid())) {

						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						Integer year = cal.get(Calendar.YEAR);
						Integer month = cal.get(Calendar.MONTH);

						SoETransaction soETransaction = soETransactionRepository.getYearlyTemplate(ngoId, timePeriodId);
						if (soETransaction == null && month == 9) { // 9 = October
						
							Calendar cale = Calendar.getInstance();
							cale.setTime(new Date());

							int typeDetailsId = Integer
									.valueOf(messageSourceNotification.getMessage("ngo.soe.soe.id", null, null));
							int pendingUpload = Integer.valueOf(
									messageSourceNotification.getMessage("ngo.soe.pending.upload", null, null));

							Configuration configuration = configurationRepository
									.findByTypeDetailsTypeDetailIdAndIsLiveTrue(typeDetailsId);

							Calendar calendar = Calendar.getInstance();
							calendar.setTime(new Date());
							Integer monthval = calendar.get(Calendar.MONTH);
							calendar.set(Calendar.MONTH, monthval + 1);
							calendar.set(Calendar.DAY_OF_MONTH, configuration.getNgoCutOffDays());

							NGOSoEUploadsStatus ngoSoEUploadsStatus = new NGOSoEUploadsStatus();

							ngoSoEUploadsStatus.setMonth(month);
							ngoSoEUploadsStatus.setYear(year);
							ngoSoEUploadsStatus.setDeadlineDate(new Timestamp(fullDateFormat
									.parse(dateFormat.format(calendar.getTime()) + " 23:59:59").getTime()));
							ngoSoEUploadsStatus.setLastUploadedDate(new Timestamp(new Date().getTime()));
							ngoSoEUploadsStatus.setCreatedDate(new Timestamp(new Date().getTime()));
							ngoSoEUploadsStatus.setNgo(new NGO(ngoId));
							ngoSoEUploadsStatus.setStatus(new TypeDetails(pendingUpload));
							ngoSoEUploadsStatus.setTimePeriod(latestTimePeriod);
							ngoSoEUploadsStatus.setNewEntry(true);
							ngoSoEUploadsStatusRepository.save(ngoSoEUploadsStatus);
							
						}
						if(soETransaction != null && month == 9){
							boolean budgetCellCheck=getLatestSoEBudget(budgetCell, ngoId,messageSourceNotification.getMessage("timeperiod.periodicity.yearly", null, null));
							if(budgetCellCheck != true) {
								workbook.close();
								LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
					                    + " : Action :"+ messageSourceNotification.getMessage("budget.not.matching", null, null) + " : "
							            + fullDateFormat.format(new Date()));
								return messageSourceNotification.getMessage("budget.not.matching", null, null);
							}
						}

						// change for read time period id in excel sheet
						if (row.getCell(1) == null) {
							cell = row.createCell(1);
						} else {
							cell = row.getCell(1);
						}
						String periodicitysix = messageSourceNotification
								.getMessage("timeperiod.periodicity.halfyearly", null, null);
						TimePeriod timePeriod = timePeriodRepository.getTimePeriodofSixPeriodicity(periodicitysix,
								new Timestamp(new java.util.Date().getTime()));
						cell.setCellValue(timePeriod.getTimePeriodId());
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						workbook.write(baos);
						workbook.close();
						soETransactionRepository.setSoETransactionStatus(dBuuidforSoe.getTemplateId());
						SoETransaction soeTransactiontemp = new SoETransaction();
						soeTransactiontemp.setUploadedTemplate(baos.toByteArray());
						soeTransactiontemp.setSoeTemplate(new SoETemplate(dBuuidforSoe.getTemplateId()));
						soeTransactiontemp
								.setTimePeriod(new TimePeriod(dBuuidforSoe.getTimePeriod().getTimePeriodId()));
						soeTransactiontemp.setLive(true);
						soeTransactiontemp.setCreatedDate(new Timestamp(new Date().getTime()));
						soeTransactiontemp.setBiAnnualTp(timePeriod.getTimePeriodId());
						soETransactionRepository.save(soeTransactiontemp);
						soETemplateRepository.setSoETemplateStatus(ngoId);

						LOGGER.info("Description : "+collectUserModel.getUsername()+ "("
						  + email +")" 
						  +" : Action :"+ messageSourceNotification.getMessage("file.upload.success", null,null)+fullDateFormat.format(new Date()));
						return "success";
					} else {
						workbook.close();
						LOGGER.info("Description : "+collectUserModel.getUsername()+ "("
								  + email +")" 
								  +" : Action :"+ messageSourceNotification.getMessage("invalid.excel", null,null)+fullDateFormat.format(new Date()));
						return messageSourceNotification.getMessage("upload.correct.file", null, null);
					}
				} else {
					workbook.close();
					LOGGER.info("Description : "+collectUserModel.getUsername()+ "("
							  + email +")" 
							  +" : Action :"+ messageSourceNotification.getMessage("invalid.excel", null,null)+fullDateFormat.format(new Date()));
					return messageSourceNotification.getMessage("upload.correct.file", null, null);
				}
			}
		} catch (IOException e) {
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
                    + " : Action :"+ messageSourceNotification.getMessage("exception.while.converting.excel.tobyte.array", null, null) + " : "
		            + fullDateFormat.format(new Date())+e);
			return messageSourceNotification.getMessage("excel.sheet.not.matching", null, null);
		} catch (ParseException e) {
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
                    + " : Action :"+ messageSourceNotification.getMessage("exception.while.converting.date", null, null) + " : "
		            + fullDateFormat.format(new Date())+e);
			return messageSourceNotification.getMessage("excel.sheet.not.matching", null, null);
		}

	}
	public boolean getLatestSoEBudget(Cell budget, Integer ngoId, String periodicity) {
		byte[] latestfile = districtUserDownloadTemplateService.getSoETemplateForOctober(ngoId,
				messageSourceNotification.getMessage("timeperiod.periodicity.yearly", null, null));
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(latestfile);
		HSSFWorkbook workbook;
		boolean flag = false;
		try {
			workbook = new HSSFWorkbook(byteArrayInputStream);
			HSSFSheet sheet = workbook.getSheet("Detailed SOE");
			if(sheet==null) {
				workbook.close();
				LOGGER.error("Error description : " 
	                    + messageSourceNotification.getMessage("invalid.excel", null, null) + " : "
			            + fullDateFormat.format(new Date()));
				return flag;
			}
			Row budgetRow = sheet.getRow(72);
			Cell budgetCell  = budgetRow.getCell(6);
			if((budgetCell.getNumericCellValue() <= budget.getNumericCellValue()) || (budgetCell.getNumericCellValue() == 0.0)){
				flag = true;
			}else if(count(ngoId)==0){
				flag = true;
			}else {
				flag = false;
			}
			workbook.close();
		} catch (IOException e) {
			LOGGER.error("Error description : " + messageSourceNotification.getMessage("excel.not.found", null, null)+ fullDateFormat.format(new Date()),e);
		}
		return flag;
	}
	
	private int count(int ngoId) {
		
		return ngoSoEUploadsStatusRepository.findRecord(ngoId);
	}
}
