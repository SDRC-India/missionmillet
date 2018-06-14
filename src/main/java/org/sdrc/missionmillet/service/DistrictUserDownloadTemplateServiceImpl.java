package org.sdrc.missionmillet.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.sdrc.missionmillet.domain.CollectUser;
import org.sdrc.missionmillet.domain.NGO;
import org.sdrc.missionmillet.domain.NGOSoEReport;
import org.sdrc.missionmillet.domain.NGOSoEUploadsStatus;
import org.sdrc.missionmillet.domain.NgoReports;
import org.sdrc.missionmillet.domain.NgoReportsFile;
import org.sdrc.missionmillet.domain.RawData;
import org.sdrc.missionmillet.domain.SoETemplate;
import org.sdrc.missionmillet.domain.SoETransaction;
import org.sdrc.missionmillet.domain.TimePeriod;
import org.sdrc.missionmillet.domain.TypeDetails;
import org.sdrc.missionmillet.domain.UUIdGenerator;
import org.sdrc.missionmillet.domain.UserAreaMapping;
import org.sdrc.missionmillet.model.CollectUserModel;
import org.sdrc.missionmillet.model.DistrictUserNgoListModel;
import org.sdrc.missionmillet.model.DistrictUserUploadSoEModel;
import org.sdrc.missionmillet.repository.AreaRepository;
import org.sdrc.missionmillet.repository.NGORepository;
import org.sdrc.missionmillet.repository.NGOSoEReportRepository;
import org.sdrc.missionmillet.repository.NGOSoEUploadsStatusRepository;
import org.sdrc.missionmillet.repository.NgoReportsFileRepository;
import org.sdrc.missionmillet.repository.RawDataRepository;
import org.sdrc.missionmillet.repository.SoETemplateRepository;
import org.sdrc.missionmillet.repository.SoETransactionRepository;
import org.sdrc.missionmillet.repository.TimePeriodRepository;
import org.sdrc.missionmillet.repository.TypeDetailsRepository;
import org.sdrc.missionmillet.repository.UUIdGeneratorRepository;
import org.sdrc.missionmillet.repository.UserAreaMappingRepository;
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
 * @author Maninee Mahapatra(maninee@sdrc.co.in)
 * 
 * @Description This is the Implementation class of
 *              DistrictUserDownloadTemplateService interface.
 */
@Service
public class DistrictUserDownloadTemplateServiceImpl implements DistrictUserDownloadTemplateService {
	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;
	@Autowired
	private ServletContext context;
	@Autowired
	private NGORepository ngoRepository;
	@Autowired
	private SoETemplateRepository soETemplateRepository;
	@Autowired
	private NGOSoEReportRepository nGOSoEReportRepository;
	@Autowired
	private NGOSoEUploadsStatusRepository nGOSoEUploadsStatusRepository;
	@Autowired
	private NgoReportsFileRepository ngoReportsFileRepository;
	@Autowired
	private StateManager stateManager;
	@Autowired
	private UserAreaMappingRepository userAreaMappingRepository;
	@Autowired
	private RawDataRepository rawDataRepository;
	@Autowired
	private TimePeriodRepository timePeriodRepository;
	@Autowired
	private AreaRepository areaRepository;
	@Autowired
	private TypeDetailsRepository typeDetailsRepository;
	@Autowired
	private UUIdGeneratorRepository uuIdGeneratorRepository;
	@Autowired
	private SoETransactionRepository soETransactionRepository;

	private SimpleDateFormat yearFromat = new SimpleDateFormat("YYYY");
	private SimpleDateFormat monthFromat = new SimpleDateFormat("MM");
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final Logger LOGGER = LoggerFactory.getLogger("LOGGER");

	/**
	 * @Description This scheduler executes every 12 month to create new entry of
	 *              the SoE template for ngo level user in SoETemplate table.
	 */
	@Scheduled(cron = "0 5 0 1 4/12 ?")
	@Override
	@Transactional
	public void districtUserupdateSoE() {
		List<NGO> ngos = ngoRepository.findAll();
	    insertNgoListToSoETemplate(ngos);
	}
	

	private void insertNgoListToSoETemplate(List<NGO> ngos) {

		SoETemplate soetemp = null;

		List<SoETemplate> soelist = new ArrayList<SoETemplate>();

		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		int preYear = 0, nextYear = 0;
		if (month > 2) {
			preYear = cal.get(Calendar.YEAR);
			cal.add(Calendar.YEAR, 1);
			nextYear = cal.get(Calendar.YEAR);
		} else {
			cal.add(Calendar.YEAR, -1);
			preYear = cal.get(Calendar.YEAR);
			cal.add(Calendar.YEAR, 1);
			nextYear = cal.get(Calendar.YEAR);
		}

		File file = new File(
				context.getRealPath(messageSourceNotification.getMessage("district.upload.template", null, null)));
		int getTimeperiodId = timePeriodRepository.getTimePeriodId(
				messageSourceNotification.getMessage("timeperiod.periodicity.yearly", null, null),
				(preYear + "-" + nextYear), new Timestamp(new java.util.Date().getTime()));
		for (NGO ngo : ngos) {
			soetemp = new SoETemplate();
			try {
				soelist.add(pushAllNGOToDB(ngo, file, soetemp, getTimeperiodId));
			} catch (Exception e) {
				LOGGER.error("Error description : " + messageSourceNotification.getMessage("excel.not.found", null, null)+ fullDateFormat.format(new Date()),e);
			}
		}
		soETemplateRepository.save(soelist);
	}


	/**
	 * @Description This method creates the SoE template for a given time period and
	 *              NGO in SoETemplate Table
	 * @param ngo
	 * @param file
	 * @param soetemp
	 * @param getTimeperiodId
	 * @return SoETemplate object
	 * 
	 */
	private SoETemplate pushAllNGOToDB(NGO ngo, File file, SoETemplate soetemp, int getTimeperiodId) {
		try {
			FileInputStream excelfile = new FileInputStream(file);
			HSSFWorkbook workbook = new HSSFWorkbook(excelfile);
			HSSFSheet sheet1 = workbook.createSheet("uuid");

			HSSFRow row = sheet1.createRow(0);
			HSSFCell cell = row.createCell(0);

			String uuid = UUID.randomUUID().toString();
			cell.setCellValue(uuid);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			workbook.write(baos);
			workbook.close();
			byte[] xls = baos.toByteArray();

			soetemp.setTimePeriod(new TimePeriod(getTimeperiodId));
			soetemp.setTemplateFile(xls);
			soetemp.setNgo(new NGO(ngo.getId()));
			soetemp.setUuid(uuid);
			soetemp.setStatus(false);
			soetemp.setCreatedDate(new Timestamp(new Date().getTime()));
			return soetemp;
		} catch (Exception e) {
			LOGGER.error("Error description : " + messageSourceNotification.getMessage("excel.not.found", null, null)
					+ fullDateFormat.format(new Date()), e);
			return soetemp;
		}
	}

	/**
	 * @Description This method fetches list of NGO in a district
	 */
	@Override
	public List<DistrictUserNgoListModel> retrieveDistrictNgoList() {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL));
		if (collectUserModel == null) {
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null) + " "
					+ fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		String email = collectUserModel.getEmailId() == null? "": collectUserModel.getEmailId();
		List<DistrictUserNgoListModel> ngolist = new ArrayList<DistrictUserNgoListModel>();
		DistrictUserNgoListModel districtUserNgoListModel = null;
		UserAreaMapping userAreaMapping = userAreaMappingRepository
				.findByCollectUser(new CollectUser(collectUserModel.getUserId()));
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int preYear = 0, nextYear = 0;
		if (month > 2) {
			preYear = cal.get(Calendar.YEAR);
			cal.add(Calendar.YEAR, 1);
			nextYear = cal.get(Calendar.YEAR);
		} else {
			cal.add(Calendar.YEAR, -1);
			preYear = cal.get(Calendar.YEAR);
			cal.add(Calendar.YEAR, 1);
			nextYear = cal.get(Calendar.YEAR);
		}

		// Retrieve all the ngo and their status of district
		List<Object[]> listOfNgoStatus = soETemplateRepository.findByDistrictIdFk(userAreaMapping.getArea().getAreaId(),
				messageSourceNotification.getMessage("timeperiod.periodicity.yearly", null, null),
				(preYear + "-" + nextYear), new Timestamp(new java.util.Date().getTime()));

		if (listOfNgoStatus.isEmpty()) {
			LOGGER.info("Description : " + collectUserModel.getUsername() + "(" + email + ")" 
		                                 + " : Action :"+ messageSourceNotification.getMessage("ngo.not.available", null, null) + " : "
									     + fullDateFormat.format(new Date()));
		}
		// Retrieve current half yearly time period
		TimePeriod timePeriod = timePeriodRepository.getTimePeriodofSixPeriodicity(
				messageSourceNotification.getMessage("timeperiod.periodicity.halfyearly", null, null),
				new Timestamp(new java.util.Date().getTime()));

		if (timePeriod == null) {
			LOGGER.info("Description : " + collectUserModel.getUsername() + "(" + email + ")" 
		                                 + " : Action :"+ messageSourceNotification.getMessage("timeperiod.not.available", null, null)
									     + " : " + fullDateFormat.format(new Date()));
			return ngolist;
		}
		LocalDate today = LocalDate.now();
		// Retrieve previous month status of all ngo in a district
		List<NGOSoEUploadsStatus> listngosoeuploadsstatus = nGOSoEUploadsStatusRepository
				.getNgoUploadSoEStatusForPreviousMonth(userAreaMapping.getArea().getAreaId(),
						cal.get(Calendar.MONTH) - 1, today.getYear());
		// Retrieve previous month status details from NGOSoEReport table
		List<NGOSoEReport> listngoSoEReport = nGOSoEReportRepository.getNgoUploadSoEStatusForPreviousMonth(
				userAreaMapping.getArea().getAreaId(), cal.get(Calendar.MONTH) - 1, today.getYear());
		Map<Integer, NGOSoEUploadsStatus> ngMap = new LinkedHashMap<Integer, NGOSoEUploadsStatus>();
		Map<Integer, NGOSoEReport> reportMap = new LinkedHashMap<Integer, NGOSoEReport>();
		// Insert all NGO SoE status into a map
		for (NGOSoEUploadsStatus ngoSoEUploadsStatus : listngosoeuploadsstatus) {
			ngMap.put(ngoSoEUploadsStatus.getNgo().getId(), ngoSoEUploadsStatus);
		}
		// Insert all NGO SoE Report into another map
		for (NGOSoEReport ngoSoEReport : listngoSoEReport) {
			reportMap.put(ngoSoEReport.getNgo().getId(), ngoSoEReport);
		}
		String setTimeperiod = timePeriod.getTimePeriod().substring(0, 3) + " "
				+ (String) timePeriod.getStartDate().toString().substring(0, 4) + "-"
				+ timePeriod.getTimePeriod().substring(4, 7) + " "
				+ (String) timePeriod.getEndDate().toString().substring(0, 4);
		// Iterate all ngo and its current status and set into a model
		for (Object[] objects : listOfNgoStatus) {
			districtUserNgoListModel = new DistrictUserNgoListModel();
			districtUserNgoListModel.setNgoId((int) objects[0]);
			districtUserNgoListModel.setNgoName((String) objects[1]);
			districtUserNgoListModel.setStatus((boolean) objects[2]);
			districtUserNgoListModel.setTimePeriod(setTimeperiod);
			districtUserNgoListModel.setTimePeriodYear((Integer) objects[4]);
			districtUserNgoListModel.setTimePeriodId((Integer) objects[5]);
			// Check whether the current month is April or not and date in between 1 to 10
			if ((today.getMonthValue() == Integer
					.parseInt(messageSourceNotification.getMessage("month.april", null, null))
					&& today.getDayOfMonth() <= Integer
							.parseInt(messageSourceNotification.getMessage("deadline.dayofmonth", null, null)))) {
				districtUserNgoListModel.setUploadStatus(true);
			} else {
				// Check whether the current month is October or not
				if (today.getMonthValue() == Integer
						.parseInt(messageSourceNotification.getMessage("month.october", null, null))) {
					NGOSoEUploadsStatus ngoSoEUploadsStatus = ngMap.get(Integer.parseInt(objects[0].toString()));
					/*
					 * Check whether status is available any of the previous month or new entry in
					 * October and date in between 10 to 15. If true set upload status true Setting
					 * the status for month April 1 to 10 and October 10 to 15 to true
					 */
					if (ngoSoEUploadsStatus == null && (today.getDayOfMonth() > Integer
							.parseInt(messageSourceNotification.getMessage("deadline.dayofmonth", null, null))
							&& today.getDayOfMonth() <= Integer.parseInt(
									messageSourceNotification.getMessage("october.deadline.dayofmonth", null, null)))) {
						districtUserNgoListModel.setUploadStatus(true);
					}
					// If any of the previous month SoE is available
					else if (ngoSoEUploadsStatus != null) {
						NGOSoEReport ngoSoEReport = reportMap.get(Integer.parseInt(objects[0].toString()));
						// Check whether current date is after the deadline date
						if (ngoSoEUploadsStatus.getDeadlineDate().before(new Timestamp(new Date().getTime()))) {
							/*
							 * If any of the following 5 condition is true it set upload status true If
							 * previous month SoE status is pending for upload and reject count null and
							 * current date after deadline date then set upload status true Or if previous
							 * month status approved or auto approved and current date after action taken
							 * date then set upload status true Or if previous month status rejected and
							 * reject count is 1 or 2 and current date after deadline date then set upload
							 * status true
							 */
							if ((ngoSoEUploadsStatus.getStatus().getTypeDetailId() == Integer.parseInt(
									messageSourceNotification.getMessage("typedetails.pending.upload", null, null))
									&& ngoSoEUploadsStatus.getRejectCount() == null
									&& (new Timestamp(new Date().getTime())
											.before(Timestamp.valueOf(ngoSoEUploadsStatus.getDeadlineDate()
													.toLocalDateTime().toLocalDate().plusDays(5).atTime(23, 59, 59)))))
									|| (ngoSoEUploadsStatus.getStatus().getTypeDetailId() == Integer.parseInt(
											messageSourceNotification.getMessage("typedetails.rejected", null, null))
											&& ngoSoEUploadsStatus.getRejectCount() == 2
											&& (new Timestamp(new Date().getTime()).before(Timestamp
													.valueOf(ngoSoEUploadsStatus.getDeadlineDate().toLocalDateTime()
															.toLocalDate().plusDays(5).atTime(23, 59, 59)))))
									|| (ngoSoEUploadsStatus.getStatus().getTypeDetailId() == Integer.parseInt(
											messageSourceNotification.getMessage("typedetails.approved", null, null))
											&& (new Timestamp(new Date().getTime()).before(Timestamp
													.valueOf(ngoSoEReport.getActionTakenDate().toLocalDateTime()
															.toLocalDate().plusDays(5).atTime(23, 59, 59)))))
									|| ((ngoSoEUploadsStatus.getStatus()
											.getTypeDetailId() == Integer.parseInt(messageSourceNotification
													.getMessage("typedetails.auto.approved", null, null)))
											&& (new Timestamp(new Date().getTime()).before(Timestamp
													.valueOf(ngoSoEReport.getActionTakenDate().toLocalDateTime()
															.toLocalDate().plusDays(5).atTime(23, 59, 59)))))
									|| ((ngoSoEUploadsStatus.getStatus().getTypeDetailId() == Integer.parseInt(
											messageSourceNotification.getMessage("typedetails.rejected", null, null)))
											&& ngoSoEUploadsStatus.getRejectCount() == 1
											&& (new Timestamp(new Date().getTime()).before(Timestamp
													.valueOf(ngoSoEUploadsStatus.getDeadlineDate().toLocalDateTime()
															.toLocalDate().plusDays(5).atTime(23, 59, 59)))))) {
								districtUserNgoListModel.setUploadStatus(true);
							} else {
								// set upload status false
								districtUserNgoListModel.setUploadStatus(false);
							}
						} else {
							// set upload status false
							districtUserNgoListModel.setUploadStatus(false);
						}
					} else {
						// set upload status false
						districtUserNgoListModel.setUploadStatus(false);
					}
				} else {
					// set upload status false
					districtUserNgoListModel.setUploadStatus(false);
				}
			}
			/*
			 * if no action taken by district user it enable the download button. show alert
			 * to the user regarding the pending status for the previous month
			 */
			if (today.getMonthValue() == Integer
					.parseInt(messageSourceNotification.getMessage("month.october", null, null))
					&& today.getDayOfMonth() > Integer
							.parseInt(messageSourceNotification.getMessage("deadline.dayofmonth", null, null))
					&& today.getDayOfMonth() <= Integer.parseInt(
							messageSourceNotification.getMessage("october.deadline.dayofmonth", null, null))) {
				if (districtUserNgoListModel.isUploadStatus()) {
					districtUserNgoListModel.setIfFalseInOctober(false);
				} else {
					districtUserNgoListModel.setIfFalseInOctober(true);
				}
			}
			ngolist.add(districtUserNgoListModel);
		}

		return ngolist;
	}

	/**
	 * @Description This method downloads the SoE template for a district user to
	 *              update budget for six months
	 * @param ngoId
	 * @param ngoName
	 * @return String filepath
	 */

	@Override
	@Transactional
	public String downloadTemplate(Integer ngoId, String ngoName) {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL));
		if (collectUserModel == null) {
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null) + " "
					+ fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		String email = collectUserModel.getEmailId() == null? "": collectUserModel.getEmailId();
		try {
			byte[] file = null;
			// Retrieve details of given ngo
			NGO ngo = ngoRepository.findById(ngoId);
			// Retrieve district Name of given ngo
			String districtName = areaRepository.getDistrictNameByNgoId(ngo.getDistrictId());
			// Retrieve block Name of given ngo
			String blockName = areaRepository.getBlockNameByNgoId(ngo.getBlockId());
			LocalDate today = LocalDate.now();
			/*
			 * Check whether the current month is April or October If October download the
			 * latest updated file else download blank SoE template for update budget
			 */
			if (today.getMonthValue() == Integer
					.parseInt(messageSourceNotification.getMessage("month.october", null, null))) {
				file = getSoETemplateForOctober(ngoId,
						messageSourceNotification.getMessage("timeperiod.periodicity.yearly", null, null));
			} else {
				file = getBlankSoETemplate(ngoId,
						messageSourceNotification.getMessage("timeperiod.periodicity.yearly", null, null));
			}

			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file);
			/**
			 * @author Subrata
			 * 
			 * @Description Locking excel file while downloading.
			 */
			List<Integer> listOfRowToLock = MissionMilletUtil
					.getLockedList(messageSourceNotification.getMessage("row.lock", null, null));

			HSSFWorkbook workbook = new HSSFWorkbook(byteArrayInputStream);
			// fetching the current biannual(half-yearly) time period
			String halfyearlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.halfyearly",
					null, null);
			Timestamp currentBiannualTp = new Timestamp(
					simpleDateFormat.parse("01-" + (Integer.parseInt(monthFromat.format(new java.util.Date()))) + "-"
							+ (Integer.parseInt(yearFromat.format(new java.util.Date())))).getTime());
			TimePeriod latestTimePeriod = timePeriodRepository.getTimePeriodofSixPeriodicity(halfyearlyPeriodicity,
					currentBiannualTp);

			HSSFSheet uuidSheet = workbook.getSheet("uuid");
			Row uuidRow = uuidSheet.createRow(3);
			Cell uuidCell = uuidRow.createCell(0);
			String uuid = UUID.randomUUID().toString();
			uuidCell.setCellValue(uuid);

			UUIdGenerator uuidGenerator = new UUIdGenerator();
			uuidGenerator.setCollectUser(new CollectUser(ngoId));
			uuidGenerator.setCreatedDate(new Timestamp(new Date().getTime()));
			uuidGenerator.setMonth(Integer.parseInt(monthFromat.format(new java.util.Date())));
			uuidGenerator.setUuid(uuid);
			uuidGenerator.setYear(Integer.parseInt(yearFromat.format(new java.util.Date())));
			uuidGenerator.setTimePeriod(latestTimePeriod);
			uuIdGeneratorRepository.save(uuidGenerator);

			Map<Integer, String> map = MissionMilletUtil.getPropertiesValues(
					getClass().getClassLoader().getResourceAsStream("messages/district_month_values.properties"));

			List<Integer> list = new ArrayList<Integer>(map.keySet());

			HSSFSheet sheet = workbook.getSheet("Detailed SOE");

			HSSFCellStyle styleForUnLocking = LockWorkbook.getStyleForUnLocking(workbook);

			HSSFCellStyle styleForLocking = LockWorkbook.getStyleForLocking(workbook);

			HSSFCellStyle styleForLeftAlign = LockWorkbook.getStyleForLeftAlign(workbook);

			HSSFRow row = null;
			HSSFCell cell = null;

			Calendar cal = Calendar.getInstance();
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int year = cal.get(Calendar.YEAR);

			row = sheet.getRow(2);
			cell = row.getCell(8);
			cell.setCellValue(districtName);
			cell.setCellStyle(styleForLeftAlign);
			cell = row.getCell(23);
			cell.setCellValue(blockName);
			cell.setCellStyle(styleForLeftAlign);

			row = null;
			cell = null;
			row = sheet.getRow(3);
			cell = row.getCell(8);
			cell.setCellValue(ngoName);
			cell.setCellStyle(styleForLeftAlign);

			cell = row.getCell(23);
			cell.setCellValue("");
			if (cell.getCellStyle().getLocked() == true)
				cell.setCellStyle(styleForUnLocking);

			row = null;
			cell = null;

			SimpleDateFormat sdf = new SimpleDateFormat("MM");

			Date currentDate = null;

			row = sheet.getRow(4);
			cell = row.getCell(5);
			cell.setCellValue("FY ("+latestTimePeriod.getFinancialYear()+")");
			cell = null;
			cell = row.getCell(7);
			// checking in month is already in the cell or not.
			if (cell.getCellType() == 3) {
				int k = 0;
				boolean beforeApril = true;
				boolean afterApril = true;
				for (int i = 0; i <= list.size(); i++) {

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
					Integer colValues = Integer.valueOf(map.get(Integer.valueOf(sdf.format(date).trim())));

					cell = row.getCell(colValues);
					cell.setCellValue(date);

					k++;

					cal.set(Calendar.MONTH, k);
				}
			}

			row = null;
			cell = null;

			/**
			 * Excel sheet will be unlocked in April and October
			 */
			int startIndex=Integer.parseInt(messageSourceNotification.getMessage("district.soe.start.row.index", null, null));
			int endIndex=Integer.parseInt(messageSourceNotification.getMessage("soe.end.row.index", null, null));
			
			int responsibleIndex=Integer.parseInt(messageSourceNotification.getMessage("ngo.soe.responsible.col.index", null, null));
			int unitsIndex=Integer.parseInt(messageSourceNotification.getMessage("ngo.soe.units.col.index", null, null));
			int rateIndex=Integer.parseInt(messageSourceNotification.getMessage("ngo.soe.rate.col.index", null, null));
			int nosIndex=Integer.parseInt(messageSourceNotification.getMessage("ngo.soe.nos.row.index", null, null));
			
			boolean flag = false;
			if ((month == 3 && day <= 10 || month == 9)) {
				flag = true;
			}
			for (int i = startIndex; i <= endIndex; i++) {
				row = sheet.getRow(i);
				for (int j = 0; j < row.getLastCellNum(); j++) {
					cell = row.getCell(j);
					if (cell.getCellStyle().getLocked() == false)
						cell.setCellStyle(styleForLocking);
					/**
					 * 2,3,4,5 are the columns to lock or unlock
					 */
					if (flag && !listOfRowToLock.contains(i)) {
						if ((cell.getColumnIndex() == responsibleIndex || cell.getColumnIndex() == unitsIndex 
								|| cell.getColumnIndex() == rateIndex || cell.getColumnIndex() == nosIndex)) {
							cell.setCellStyle(styleForUnLocking);
						}
					} else {
						if ((cell.getColumnIndex() == responsibleIndex || cell.getColumnIndex() == unitsIndex 
								|| cell.getColumnIndex() == rateIndex || cell.getColumnIndex() == nosIndex)) {
							cell.setCellStyle(styleForLocking);
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

			workbook.setSheetHidden(2, HSSFWorkbook.SHEET_STATE_VERY_HIDDEN);
			workbook.setSheetHidden(3, HSSFWorkbook.SHEET_STATE_VERY_HIDDEN);

			String date = new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date());
			String filepath = messageSourceNotification.getMessage("wassan.upload.report.path", null, null)
					+ "SoETemplate" + "_" + ngoId + "_" + ngoName + "_" + date + ".xls";
			FileOutputStream fileOut = new FileOutputStream(filepath);
			workbook.write(fileOut);
			fileOut.close();
			return filepath;
		} catch (Exception e) {
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "(" + email + ")" + fullDateFormat.format(new Date()),e);
			return null;
		}
	}

	/**
	 * @param ngoId
	 * @param periodicity
	 * @return byte[] file
	 * @Description If month is April then fetch the blank SoE template
	 */
	@Override
	public byte[] getBlankSoETemplate(Integer ngoId, String periodicity) {
		SoETemplate xlsfile = soETemplateRepository.findByNgoIdAndTimePeriod(ngoId, new java.util.Date(),
				Integer.parseInt(yearFromat.format(new java.util.Date())), periodicity);
		if (xlsfile == null || xlsfile.getTemplateFile() == null) {
			LOGGER.error(
					"Error description :" + messageSourceNotification.getMessage("file.not.foundForNgo", null, null)
							+ " : " + ngoId + fullDateFormat.format(new Date()));
			throw new NullPointerException(messageSourceNotification.getMessage("file.not.foundForNgo", null, null)
					+ fullDateFormat.format(new Date()) + ngoId);
		}
		return xlsfile.getTemplateFile();
	}

	/**
	 * @Description If month is October then find the latest updated SoE
	 * 
	 * @param ngoId
	 * @param periodicity
	 * @return byte[] file
	 * 
	 */
	@Override
	public byte[] getSoETemplateForOctober(Integer ngoId, String periodicity) {
		NGOSoEReport ngoReport = nGOSoEReportRepository.getReportOfLatestFile(ngoId,
				Integer.parseInt(messageSourceNotification.getMessage("typedetails.approved", null, null)),
				Integer.parseInt(messageSourceNotification.getMessage("typedetails.auto.approved", null, null)),
				Integer.parseInt(yearFromat.format(new java.util.Date())));
		SoETransaction soETransaction = null;
		if (ngoReport == null) {
			Timestamp currentDate;
			currentDate = new Timestamp(new Date().getTime());
			String yearlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.yearly", null,
					null);
			String monthlyPeriodicity = messageSourceNotification.getMessage("timeperiod.periodicity.monthly", null,
					null);
			TimePeriod latestTimePeriod = timePeriodRepository.getTimePeriodofSixPeriodicity(monthlyPeriodicity,
					currentDate);
			TimePeriod timePeriod = timePeriodRepository.getTimePeriodData(currentDate, yearlyPeriodicity,
					latestTimePeriod.getFinancialYear());
			soETransaction = soETransactionRepository.getLatestTemplate(timePeriod.getTimePeriodId(), ngoId);

			if (soETransaction == null) {
				return getBlankSoETemplate(ngoId, periodicity);
			} else {
				return soETransaction.getUploadedTemplate();
			}

		} else {
			NgoReportsFile reportsFile = ngoReportsFileRepository.getLatestReportFileBySoEReportId(ngoReport.getNgoSoEReportsId());
			return reportsFile.getReportsFile();
		}
	}

	/**
	 * @Description retrieve all the ngo list of corresponding district with
	 *              uploaded SoE for approval/rejection after deadline.
	 */
	@Override
	public Map<String, List<DistrictUserNgoListModel>> retrieveDistrictNgoListSoEStatus() {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL));
		if (collectUserModel == null) {
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null) + " "
					+ fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		String email = collectUserModel.getEmailId() == null? "": collectUserModel.getEmailId();
		try {
			List<DistrictUserNgoListModel> ngolist = new ArrayList<DistrictUserNgoListModel>();
			DistrictUserNgoListModel districtUserNgoListModel = null;
			UserAreaMapping userAreaMapping = userAreaMappingRepository
					.findByCollectUser(new CollectUser(collectUserModel.getUserId()));
			List<TimePeriod> timePeriods = timePeriodRepository.findByPeriodicity("1");
			// Retrieve all the ngo list who upload their SoE for approval
			List<Object[]> listOfNgoStatusSoE = nGOSoEReportRepository.findSoEStatusByDistrictId(
					userAreaMapping.getArea().getAreaId(),
					Integer.parseInt(messageSourceNotification.getMessage("typedetails.pending", null, null)),
					new java.util.Date());
			// Iterate the list of ngo and set in model to be sent to frontend
			for (Object[] objects : listOfNgoStatusSoE) {
				districtUserNgoListModel = new DistrictUserNgoListModel();
				districtUserNgoListModel.setMonthInt((Integer) objects[0]);
				districtUserNgoListModel.setYear((Integer) objects[1]);
				Timestamp tmst = (Timestamp) objects[2];
				districtUserNgoListModel.setNgoId((int) objects[3]);
				districtUserNgoListModel.setNgoName((String) objects[4]);
				districtUserNgoListModel.setDate(tmst.toString().substring(0, 10));

				Month month = Month.of(districtUserNgoListModel.getMonthInt() + 1);
				districtUserNgoListModel.setMonth(month.getDisplayName(TextStyle.SHORT, Locale.UK));

				TypeDetails td = (TypeDetails) objects[5];
				districtUserNgoListModel.setStatusId(td.getTypeDetailId());
				districtUserNgoListModel.setStatusDetail(td.getTypeDetailName());

				TimePeriod timePeriod = NgoReportsServiceImpl.getTimePeriodByMonthAndYear(timePeriods,
						month.getDisplayName(TextStyle.SHORT, Locale.UK), (Integer) objects[1]);
				districtUserNgoListModel.setFinancialyear(timePeriod.getFinancialYear());

				ngolist.add(districtUserNgoListModel);
			}
			// create a map with ngo list and corresponding year for SOE
			List<DistrictUserNgoListModel> reportsModels = null;
			Map<String, List<DistrictUserNgoListModel>> map = new HashMap<String, List<DistrictUserNgoListModel>>();
			for (int i = 0; i < ngolist.size(); i++) {
				if (map.containsKey(ngolist.get(i).getFinancialyear())) {
					reportsModels = map.get(ngolist.get(i).getFinancialyear());
					reportsModels.add(ngolist.get(i));
					map.put(ngolist.get(i).getFinancialyear(), reportsModels);
				} else {
					reportsModels = new ArrayList<DistrictUserNgoListModel>();
					reportsModels.add(ngolist.get(i));
					map.put(ngolist.get(i).getFinancialyear(), reportsModels);
				}
			}
			return map;
		} catch (Exception e) {
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" + fullDateFormat.format(new Date()),e);
			return null;
		}
	}

	/**
	 * @Description This method called when district user approves the SoE uploaded
	 *              by a NGO for a specific month
	 * @param districtUserUploadSoEModel
	 * @return String
	 */
	@Override
	@Transactional
	public String approveSoEStatus(DistrictUserUploadSoEModel districtUserUploadSoEModel) {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL));
		if (collectUserModel == null) {
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null) + " "
					+ fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		String email = collectUserModel.getEmailId() == null? "": collectUserModel.getEmailId();
		NGO ngo = new NGO();
		ngo.setId(districtUserUploadSoEModel.getNgoId());
		// find the specific ngo and update its details after approved
		NGOSoEUploadsStatus ngosoeuploadsstatus = nGOSoEUploadsStatusRepository.findByNgoAndMonthAndYear(ngo,
				districtUserUploadSoEModel.getMonth(), districtUserUploadSoEModel.getYear());
		ngosoeuploadsstatus.setStatus(new TypeDetails(
				Integer.parseInt(messageSourceNotification.getMessage("typedetails.approved", null, null))));
		NGOSoEReport soeReport = nGOSoEReportRepository.findByNgoAndMonthAndYearAndIsLiveTrue(ngo,
				districtUserUploadSoEModel.getMonth(), districtUserUploadSoEModel.getYear());
		soeReport.setLive(false);
		soeReport.setReEntryStatus(new TypeDetails(
				Integer.parseInt(messageSourceNotification.getMessage("typedetails.approved", null, null))));
		soeReport.setActionTakenDate(new Timestamp(new Date().getTime()));
		soeReport.setActionTakenByUserId(new CollectUser(collectUserModel.getUserId()));
		soeReport.setRemarks(districtUserUploadSoEModel.getReMark());
		NgoReportsFile reportfile = ngoReportsFileRepository
				.findByNgoSoeReport(new NGOSoEReport(soeReport.getNgoSoEReportsId()));
		if (reportfile.getReportsFile() == null) {
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
		                                        + " : Action :"+ messageSourceNotification.getMessage("file.not.found", null, null) + " : "
									            + fullDateFormat.format(new Date()));
			throw new NullPointerException(messageSourceNotification.getMessage("file.not.found", null, null)
					+ fullDateFormat.format(new Date()) + collectUserModel.getUserId());
		}
		// Saving approved data in RawData table
		/**
		 * @author Subrata
		 * 
		 * @Description If NGO data is approved, then, we are saving the total value for
		 *              financial data in RawData table.
		 * 
		 */
		
		List<RawData> allAutoApprovedData = new ArrayList<RawData>();
		Map<Integer, String> map = null;
		try {
			// get value from property file
			map = MissionMilletUtil.getPropertiesValues(
					getClass().getClassLoader().getResourceAsStream("messages/rawdata.properties"));
		} catch (IOException e) {
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
		                                        + fullDateFormat.format(new Date()),e);
		}

		allAutoApprovedData.add(getRawDataFromExcel(reportfile.getReportsFile(), ngo, soeReport.getTimePeriod(), map));

		// saving raw data object
		rawDataRepository.save(allAutoApprovedData);
		return "approve";
	}

	/**
	 * @Description district user rejects the SoE of an ngo for specific month
	 * 
	 * @param districtUserUploadSoEModel
	 * @return String
	 */
	@Override
	@Transactional
	public String rejectSoEStatus(DistrictUserUploadSoEModel districtUserUploadSoEModel) {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL));
		if (collectUserModel == null) {
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null) + " "
					+ fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		NGO ngo = new NGO();
		ngo.setId(districtUserUploadSoEModel.getNgoId());
		// Find specific ngo and update its details after rejected
		NGOSoEUploadsStatus ngosoeuploadsstatus = nGOSoEUploadsStatusRepository.findByNgoAndMonthAndYear(ngo,
				districtUserUploadSoEModel.getMonth(), districtUserUploadSoEModel.getYear());

		if (ngosoeuploadsstatus.getRejectCount() == null) {
			ngosoeuploadsstatus.setRejectCount(1);
			ngosoeuploadsstatus.setDeadlineDate(Timestamp.valueOf(new Timestamp(new Date().getTime()).toLocalDateTime()
					.toLocalDate().plusDays(5).atTime(23, 59, 59)));
		} else {
			ngosoeuploadsstatus.setRejectCount(ngosoeuploadsstatus.getRejectCount() + 1);

		}
		ngosoeuploadsstatus.setStatus(new TypeDetails(
				Integer.parseInt(messageSourceNotification.getMessage("typedetails.rejected", null, null))));

		NGOSoEReport soeReport = nGOSoEReportRepository.findByNgoAndMonthAndYearAndIsLiveTrue(ngo,
				districtUserUploadSoEModel.getMonth(), districtUserUploadSoEModel.getYear());
		soeReport.setLive(false);
		soeReport.setReEntryStatus(new TypeDetails(
				Integer.parseInt(messageSourceNotification.getMessage("typedetails.rejected", null, null))));
		soeReport.setActionTakenDate(new Timestamp(new Date().getTime()));
		soeReport.setActionTakenByUserId(new CollectUser(collectUserModel.getUserId()));
		soeReport.setRemarks(districtUserUploadSoEModel.getReMark());
		if (ngosoeuploadsstatus.getRejectCount() == 1) {
			soeReport.setLatest(false);
		}
		return "reject";
	}

	/**
	 * @Description Fetch the all the reports uploaded by the ngo to be displayed in
	 *              the workspace, after the deadline date.
	 * @return Map<String, List<DistrictUserNgoListModel>>
	 */
	@Override
	public Map<String, List<DistrictUserNgoListModel>> retrieveDistNgoReport() {
		List<DistrictUserNgoListModel> ngolist = new ArrayList<DistrictUserNgoListModel>();
		DistrictUserNgoListModel districtUserNgoListModel = null;
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL));
		if (collectUserModel == null) {
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null) + " "
					+ fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		UserAreaMapping userAreaMapping = userAreaMappingRepository
				.findByCollectUser(new CollectUser(collectUserModel.getUserId()));
		// Get the report details of all ngo
		List<Object[]> listOfNgoReport = ngoReportsFileRepository.findNgoReportByDistrictId(
				userAreaMapping.getArea().getAreaId(), new Timestamp(new java.util.Date().getTime()));
		List<TimePeriod> timePeriods = timePeriodRepository.findByPeriodicity("1");
		int reportId = 0;
		// Iterate the list and set in model to be sent to front end
		for (Object[] objects : listOfNgoReport) {
			NgoReports ngoReport = (NgoReports) objects[1];
			if (reportId != ngoReport.getReportsId()) {
				districtUserNgoListModel = new DistrictUserNgoListModel();
				reportId = ngoReport.getReportsId();
			}
			districtUserNgoListModel.setMonthInt(ngoReport.getMonth());
			districtUserNgoListModel.setYear(ngoReport.getYear());
			districtUserNgoListModel.setRemark(ngoReport.getRemarks());
			districtUserNgoListModel.setNgoId(ngoReport.getNgo().getId());
			districtUserNgoListModel.setNgoName(ngoReport.getNgo().getName());
			districtUserNgoListModel.setDate(ngoReport.getCreatedDate().toString().substring(0, 10));

			Month month = Month.of(districtUserNgoListModel.getMonthInt() + 1);
			districtUserNgoListModel.setMonth(month.getDisplayName(TextStyle.SHORT, Locale.UK));

			districtUserNgoListModel.setStatusId(ngoReport.getReportType().getTypeDetailId());
			districtUserNgoListModel.setStatusDetail(ngoReport.getReportType().getTypeDetailName());

			TimePeriod timePeriod = NgoReportsServiceImpl.getTimePeriodByMonthAndYear(timePeriods,
					month.getDisplayName(TextStyle.SHORT, Locale.UK), (Integer) ngoReport.getYear());
			districtUserNgoListModel.setFinancialyear(timePeriod.getFinancialYear());
			TypeDetails reportFileType = (TypeDetails) objects[2];
			if (reportFileType.getTypeDetailId() == 8)
				districtUserNgoListModel.setReportFileId((Integer) objects[0]);
			else
				districtUserNgoListModel.setUtilizationCertificateId((Integer) objects[0]);

			if (!ngolist.contains(districtUserNgoListModel))
				ngolist.add(districtUserNgoListModel);

		}
		// creating a map of list and corresponding year
		List<DistrictUserNgoListModel> reportsModels = null;
		Map<String, List<DistrictUserNgoListModel>> map = new TreeMap<String, List<DistrictUserNgoListModel>>(
				Collections.reverseOrder());
		for (int i = 0; i < ngolist.size(); i++) {
			if (map.containsKey(ngolist.get(i).getFinancialyear())) {
				reportsModels = map.get(ngolist.get(i).getFinancialyear());
				reportsModels.add(ngolist.get(i));
				map.put(ngolist.get(i).getFinancialyear(), reportsModels);
			} else {
				reportsModels = new ArrayList<DistrictUserNgoListModel>();
				reportsModels.add(ngolist.get(i));
				map.put(ngolist.get(i).getFinancialyear(), reportsModels);
			}
		}

		return map;
	}

	/**
	 * @Description Download the SoE which are uploaded by the ngo, for review.
	 * 
	 * @param districtUserUploadSoEModel
	 * @return String
	 */
	@Override
	@Transactional(readOnly = true)
	public String downloadDistNgoSoEForAction(DistrictUserUploadSoEModel districtUserUploadSoEModel) {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL));
		if (collectUserModel == null) {
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null) + " "
					+ fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		String email = collectUserModel.getEmailId() == null? "": collectUserModel.getEmailId();
		String filepath = null;
		ByteArrayInputStream byteArrayInputStream;
		HSSFWorkbook workbook = null;
		NGO ngo = new NGO();
		ngo.setId(districtUserUploadSoEModel.getNgoId());
		// Find the SoE file for a specific ngo and month
		NGOSoEReport ngoSoeRepotId = nGOSoEReportRepository.findByNgoAndMonthAndYearAndIsLiveTrue(ngo,
				districtUserUploadSoEModel.getMonth(), districtUserUploadSoEModel.getYear());
		NgoReportsFile xlsfile = ngoReportsFileRepository
				.findByNgoSoeReport(new NGOSoEReport(ngoSoeRepotId.getNgoSoEReportsId()));
		if (xlsfile == null || xlsfile.getReportsFile() == null) {
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
		                                        + " : Action :"+ messageSourceNotification.getMessage("file.not.found", null, null) + " : "
									            + fullDateFormat.format(new Date()));
			throw new NullPointerException(messageSourceNotification.getMessage("file.not.found", null, null)
					+ fullDateFormat.format(new Date()) + districtUserUploadSoEModel.getNgoId());
		}

		byte[] file = (byte[]) xlsfile.getReportsFile();
		// Lock the unused cell of excel
		try {
			byteArrayInputStream = new ByteArrayInputStream(file);
			workbook = LockWorkbook.workbookLock(new HSSFWorkbook(byteArrayInputStream));
		} catch (IOException e) {
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
		                                        + " : Action :"+ messageSourceNotification.getMessage("unable.tolock.excel", null, null) + " : "
									            + fullDateFormat.format(new Date()));
			throw new RuntimeException(messageSourceNotification.getMessage("unable.tolock.excel", null, null)
					+ fullDateFormat.format(new Date()), e);
		}
		String date = new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date());
		filepath = messageSourceNotification.getMessage("wassan.upload.report.path", null, null) + "SoETemplate" + "_"
				+ districtUserUploadSoEModel.getNgoId() + "_" + districtUserUploadSoEModel.getNgoName() + "_" + date + ".xls";
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(filepath);
		} catch (FileNotFoundException e) {
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
		                                        + " : Action :"+ messageSourceNotification.getMessage("excel.not.found", null, null) + " : "
									            + fullDateFormat.format(new Date()));
			throw new RuntimeException(messageSourceNotification.getMessage("excel.not.found", null, null)
					+ fullDateFormat.format(new Date()) + filepath, e);
		}
		try {
			workbook.write(fileOut);
		} catch (IOException e1) {
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
		                                        + " : Action :"+ messageSourceNotification.getMessage("unable.towrite.excel", null, null) + " : "
									            + fullDateFormat.format(new Date()));
			throw new RuntimeException(messageSourceNotification.getMessage("unable.towrite.excel", null, null)
					+ fullDateFormat.format(new Date()) + filepath, e1);
		}
		try {
			fileOut.close();
		} catch (IOException e) {
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
		                                        + " : Action :"+ messageSourceNotification.getMessage("unable.toclose.excel", null, null) + " : "
									            + fullDateFormat.format(new Date()));
			throw new RuntimeException(messageSourceNotification.getMessage("unable.toclose.excel", null, null)
					+ fullDateFormat.format(new Date()) + filepath, e);
		}
		return filepath;
	}

	/**
	 * @description This method retrieves SoE templates for all NGO in a district
	 *
	 * @return map<String, List<DistrictUserNgoListModel>>
	 */
	@Override
	public Map<String, List<DistrictUserNgoListModel>> retrieveDistNgoHistory() {
		List<DistrictUserNgoListModel> ngolist = new ArrayList<DistrictUserNgoListModel>();
		DistrictUserNgoListModel districtUserNgoListModel = null;
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL));
		if (collectUserModel == null) {
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null) + " "
					+ fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		UserAreaMapping userAreaMapping = userAreaMappingRepository
				.findByCollectUser(new CollectUser(collectUserModel.getUserId()));
		List<TypeDetails> details = typeDetailsRepository.findAll();
		Integer approveId = Integer.parseInt(messageSourceNotification.getMessage("typedetails.approved", null, null));
		Integer rejectId = Integer.parseInt(messageSourceNotification.getMessage("typedetails.rejected", null, null));
		Integer autoApprove = Integer
				.parseInt(messageSourceNotification.getMessage("typedetails.auto.approved", null, null));
		Integer pendingId = Integer
				.parseInt(messageSourceNotification.getMessage("typedetails.pending.upload", null, null));
		// Retrieve all ngo SoE history for a district
		List<Object[]> listOfNgoReport = nGOSoEReportRepository.findNgoHistoryByDistrictId(
				userAreaMapping.getArea().getAreaId(), new Timestamp(new java.util.Date().getTime()), approveId,
				rejectId, autoApprove, pendingId);
		List<TimePeriod> timePeriods = timePeriodRepository.findByPeriodicity("1");
		// Iterate the list and set into model to be sent to front end
		for (Object[] objects : listOfNgoReport) {
			districtUserNgoListModel = new DistrictUserNgoListModel();
			districtUserNgoListModel.setNgoId((Integer) objects[0]);
			districtUserNgoListModel.setNgoName((String) objects[1]);
			districtUserNgoListModel
					.setMonthInt((Integer) objects[2] != null ? (Integer) objects[2] : (Integer) objects[9]);
			districtUserNgoListModel
					.setYear((Integer) objects[3] != null ? (Integer) objects[3] : (Integer) objects[10]);
			Timestamp createdDate = (Timestamp) objects[4] != null ? (Timestamp) objects[4] : null;
			districtUserNgoListModel.setDate(createdDate == null ? null : createdDate.toString().substring(0, 10));
			districtUserNgoListModel.setStatusDetail(objects[5] != null ? NgoReportsServiceImpl
					.getTypeDetailsByTypeDetailsId(details, Integer.valueOf(objects[5].toString())).getTypeDetailName()
					: NgoReportsServiceImpl
							.getTypeDetailsByTypeDetailsId(details, Integer.valueOf(objects[12].toString()))
							.getTypeDetailName());

			Timestamp actionTakenDate = (Timestamp) (objects[6] != null ? (Timestamp) objects[6] : null);
			districtUserNgoListModel
					.setActionTakenDate(actionTakenDate == null ? null : actionTakenDate.toString().substring(0, 10));
			districtUserNgoListModel.setReportId((Integer) objects[7] != null ? (Integer) objects[7] : null);
			districtUserNgoListModel.setRemark(actionTakenDate == null ? null : (String) objects[8]);
			Month month = Month.of(districtUserNgoListModel.getMonthInt() + 1);
			districtUserNgoListModel.setMonth(month.getDisplayName(TextStyle.SHORT, Locale.UK));
			TimePeriod timePeriod = NgoReportsServiceImpl.getTimePeriodByMonthAndYear(timePeriods,
					month.getDisplayName(TextStyle.SHORT, Locale.UK), districtUserNgoListModel.getYear());
			districtUserNgoListModel.setFinancialyear(timePeriod.getFinancialYear());

			ngolist.add(districtUserNgoListModel);
		}

		List<DistrictUserNgoListModel> reportsModels = null;
		Map<String, List<DistrictUserNgoListModel>> map = new TreeMap<String, List<DistrictUserNgoListModel>>(
				Collections.reverseOrder());
		for (int i = 0; i < ngolist.size(); i++) {
			if (map.containsKey(ngolist.get(i).getFinancialyear())) {
				reportsModels = map.get(ngolist.get(i).getFinancialyear());
				reportsModels.add(ngolist.get(i));
				map.put(ngolist.get(i).getFinancialyear(), reportsModels);
			} else {
				reportsModels = new ArrayList<DistrictUserNgoListModel>();
				reportsModels.add(ngolist.get(i));
				map.put(ngolist.get(i).getFinancialyear(), reportsModels);
			}
		}

		return map;
	}

	/**
	 * @Description Download report file uploaded by the NGO for a specific month
	 * @param districtUserUploadSoEModel
	 * @return String filepath
	 */
	@Override
	@Transactional(readOnly = true)
	public String downloadDistNgoReport(DistrictUserUploadSoEModel districtUserUploadSoEModel) {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL));
		if (collectUserModel == null) {
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null) + " "
					+ fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		String email = collectUserModel.getEmailId() == null? "": collectUserModel.getEmailId();
		Integer reportsfileid = districtUserUploadSoEModel.getReportFileId();
		// Find the report for download with reportfileId
		NgoReportsFile ngoReportsFile = ngoReportsFileRepository.getByReportsFileId(reportsfileid);

		if (ngoReportsFile == null || ngoReportsFile.getFilePath() == null) {
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
		                                        + " : Action :"+ messageSourceNotification.getMessage("file.not.found", null, null) + " : "
									            + fullDateFormat.format(new Date()));
			throw new NullPointerException(messageSourceNotification.getMessage("file.not.found", null, null)
					+ fullDateFormat.format(new Date()) + reportsfileid);
		}
		return MissionMilletUtil.getPdfFile(ngoReportsFile.getFilePath(),
				messageSourceNotification.getMessage("wassan.upload.report.path", null, null));
	}

	/**
	 * @Description Download SoE file uploaded by the NGO for a specific month
	 * @param reportId
	 * @param districtUserUploadSoEModel
	 * @return String filepath
	 */
	@Override
	@Transactional(readOnly = true)
	public String downloadDistNgoHistoryFile(Integer reportId, DistrictUserUploadSoEModel districtUserUploadSoEModel) {
		// Getting the logged in user credentials and if not found the credentials throwing an error.
		CollectUserModel collectUserModel = ((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL));
		if (collectUserModel == null) {
			LOGGER.error(messageSourceNotification.getMessage("invalid.user", null, null) + " "
					+ fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		String email = collectUserModel.getEmailId() == null? "": collectUserModel.getEmailId();
		String filepath = null;
		ByteArrayInputStream byteArrayInputStream;
		HSSFWorkbook workbook = null;
		// Find the history file with reportId
		NgoReportsFile xlsfile = ngoReportsFileRepository.findByNgoSoeReport(new NGOSoEReport(reportId));
		if (xlsfile == null || xlsfile.getReportsFile() == null) {
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
		                                        + " : Action :"+ messageSourceNotification.getMessage("file.not.found", null, null) + " : "
									            + fullDateFormat.format(new Date()));
			throw new NullPointerException(messageSourceNotification.getMessage("file.not.found", null, null)
					+ fullDateFormat.format(new Date()) + reportId);
		}

		byte[] file = (byte[]) xlsfile.getReportsFile();

		try {
			byteArrayInputStream = new ByteArrayInputStream(file);
			workbook = LockWorkbook.workbookLock(new HSSFWorkbook(byteArrayInputStream));
		} catch (IOException e) {
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
		                                        + " : Action :"+ messageSourceNotification.getMessage("unable.tolock.excel", null, null) + " : "
									            + fullDateFormat.format(new Date()));
			throw new RuntimeException(messageSourceNotification.getMessage("unable.tolock.excel", null, null)
					+ fullDateFormat.format(new Date()), e);
		}

		String date = new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date());
		filepath = messageSourceNotification.getMessage("wassan.upload.report.path", null, null) + "SoETemplate" + "_"
				+ districtUserUploadSoEModel.getNgoId() + "_" + districtUserUploadSoEModel.getNgoName() + "_" + date + ".xls";
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(filepath);
		} catch (FileNotFoundException e) {
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
		                                        + " : Action :"+ messageSourceNotification.getMessage("excel.not.found", null, null) + " : "
									            + fullDateFormat.format(new Date()));
			throw new RuntimeException(messageSourceNotification.getMessage("excel.not.found", null, null)
					+ fullDateFormat.format(new Date()) + filepath, e);
		}
		try {
			workbook.write(fileOut);
		} catch (IOException e1) {
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
		                                        + " : Action :"+ messageSourceNotification.getMessage("unable.towrite.excel", null, null) + " : "
									            + fullDateFormat.format(new Date()));
			throw new RuntimeException(messageSourceNotification.getMessage("unable.towrite.excel", null, null)
					+ fullDateFormat.format(new Date()) + filepath, e1);
		}
		try {
			fileOut.close();
		} catch (IOException e) {
			LOGGER.error("Error description : " + collectUserModel.getUsername() + "("+ email + ")" 
		                                        + " : Action :"+ messageSourceNotification.getMessage("unable.toclose.excel", null, null) + " : "
									            + fullDateFormat.format(new Date()));
			throw new RuntimeException(messageSourceNotification.getMessage("unable.toclose.excel", null, null)
					+ fullDateFormat.format(new Date()) + filepath, e);
		}

		return filepath;
	}

	/**
	 * @Description This scheduled job executes every day at 00:00 to auto approved
	 *              the pending SoE, if the current date is after 5 days of deadline
	 *              date of uploaded SoE.
	 */
	@Scheduled(cron = "0 0 0 1/1 * ?")
	@Override
	@Transactional
	public void autoApprove() throws IOException {
		// Find all the ngo SoE details for auto approve
		List<NGOSoEUploadsStatus> listOfNGOSoEUploadsStatus = nGOSoEUploadsStatusRepository
				.getSoEListForAutoApprove(new Timestamp(new Date().getTime()));
		List<NGOSoEReport> listOfNgoSeoReports = nGOSoEReportRepository.findByReEntryStatusANDIsLiveIsTRUE(
				Integer.parseInt(messageSourceNotification.getMessage("typedetails.re.upload", null, null)));
		for (NGOSoEUploadsStatus nGOSoEUploadsStatus : listOfNGOSoEUploadsStatus) {
			Timestamp deadlineDate = nGOSoEUploadsStatus.getDeadlineDate();
			Timestamp approvedDate = Timestamp
					.valueOf(deadlineDate.toLocalDateTime().toLocalDate().plusDays(5).atTime(23, 59, 59));

			List<RawData> allAutoApprovedData = new ArrayList<RawData>();
			Map<Integer, String> map = MissionMilletUtil.getPropertiesValues(
					getClass().getClassLoader().getResourceAsStream("messages/rawdata.properties"));

			// auto approve them and update NGOSoEUploadsStatus and NGOSoEReport table
			for (NGOSoEReport ngoSeoReports : listOfNgoSeoReports) {
				if ((new Timestamp(new Date().getTime()).after(approvedDate))
						&& nGOSoEUploadsStatus.getMonth() == ngoSeoReports.getMonth()
						&& nGOSoEUploadsStatus.getYear() == ngoSeoReports.getYear()
						&& nGOSoEUploadsStatus.getNgo().getId() == ngoSeoReports.getNgo().getId()) {
					nGOSoEUploadsStatus.setStatus(new TypeDetails(3));
					ngoSeoReports.setActionTakenDate(new Timestamp(System.currentTimeMillis()));
					ngoSeoReports.setReEntryStatus(new TypeDetails(3));
					ngoSeoReports.setLive(false);
					NgoReportsFile reportfile = ngoReportsFileRepository
							.findByNgoSoeReport(new NGOSoEReport(ngoSeoReports.getNgoSoEReportsId()));
					if (reportfile.getReportsFile() == null) {
						LOGGER.error("Error description :"
								+ messageSourceNotification.getMessage("file.not.found", null, null) + " : "
								+ fullDateFormat.format(new Date()));
						throw new NullPointerException(
								messageSourceNotification.getMessage("file.not.found", null, null)
										+ fullDateFormat.format(new Date()));
					}

					NGO ngo = new NGO();
					ngo.setId(ngoSeoReports.getNgo().getId());
					// Saving approved data in RawData table
					allAutoApprovedData.add(getRawDataFromExcel(reportfile.getReportsFile(), ngo, ngoSeoReports.getTimePeriod(), map));
				}
			}
			rawDataRepository.save(allAutoApprovedData);
		}
	}

	/**
	 * @Description Read the total budget row and save in RawData table
	 * 
	 * @author Subrata
	 * @param getFileBytes
	 * @param ngo
	 * @param timePeriod
	 * @param map
	 * @return RawData
	 * 
	 */

	private RawData getRawDataFromExcel(byte[] getFileBytes, NGO ngo, TimePeriod timePeriod, Map<Integer, String> map) {
		try {
			RawData rawdata = new RawData();
			HSSFWorkbook workbook = new HSSFWorkbook(MissionMilletUtil.getByteArrayStream(getFileBytes));
			HSSFSheet sheet = workbook.getSheet("Detailed SOE");

			// for Month getting the 4th row for putting the month
			HSSFRow row = sheet.getRow(4);

			Cell cell = null;
			List<Integer> list = new ArrayList<Integer>(map.keySet());
			Collections.sort(list);
			int colNum = 0;
			Date dateFromExcel = new Date(timePeriod.getStartDate().getTime());
			LocalDate localDateFromExcel = null;
			Date dateFromDb = null;
			LocalDate localDateFromDb = dateFromExcel.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			for (Integer cellNumber : list) {
				cell = row.getCell(cellNumber);

				dateFromDb = cell.getDateCellValue();
				localDateFromExcel = dateFromDb.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				if (localDateFromDb.getMonthValue() == localDateFromExcel.getMonthValue())
					colNum = cellNumber;
			}
			row = null;
			row = sheet.getRow(72); // reading the total budget row (starting from 0 in the excel) i.e. 72

			// setting all the values in row data object reading values from excel
			cell = row.getCell(Integer.valueOf((map.get(colNum).split(",")[0])));
			rawdata.setOpeningBalance(cell.getNumericCellValue());

			cell = row.getCell(Integer.valueOf((map.get(colNum).split(",")[1])));
			rawdata.setAmountReceived(cell.getNumericCellValue());

			cell = row.getCell(Integer.valueOf((map.get(colNum).split(",")[2])));
			rawdata.setTotalBalance(cell.getNumericCellValue());

			cell = row.getCell(Integer.valueOf((map.get(colNum).split(",")[3])));
			rawdata.setPreviousMonthAmountSpent(cell.getNumericCellValue());

			cell = row.getCell(Integer.valueOf((map.get(colNum).split(",")[4])));
			rawdata.setCurrentMonthAmountSpent(cell.getNumericCellValue());

			cell = row.getCell(Integer.valueOf((map.get(colNum).split(",")[5])));
			rawdata.setCumulativeAmountSpent(cell.getNumericCellValue());

			cell = row.getCell(Integer.valueOf((map.get(colNum).split(",")[6])));
			rawdata.setClosingBalance(cell.getNumericCellValue());

			cell = row.getCell(Integer.valueOf((map.get(colNum).split(",")[7])));
			rawdata.setPhysicalUnit((int) cell.getNumericCellValue());

			cell = row.getCell(Integer.valueOf((map.get(colNum).split(",")[8])));
			rawdata.setFinance(cell.getNumericCellValue());

			rawdata.setNgo(ngo);
			rawdata.setTimePeriod(timePeriod);
			workbook.close();
			return rawdata;

		} catch (IOException e) {
			LOGGER.error("Error description : "
					+ messageSourceNotification.getMessage("unable.toconvert.workbook", null, null) + " : "
					+ fullDateFormat.format(new Date()));
			return null;
		}
		
	}

	/**
	 * @Description This scheduler executes at 00:06am on 1st October to update the
	 *              SoE status in SoETemplate table to false.
	 */
	@Scheduled(cron = "0 6 0 1 10/12 ?")
	@Override
	@Transactional
	public void setStausFalseOfSoETemplateInOctober() {
		soETemplateRepository.setSoETemplateStatusFalse();
		List<NGO> ngos = ngoRepository.findListOfNewNgos();
		insertNgoListToSoETemplate(ngos);

	}
	
}
