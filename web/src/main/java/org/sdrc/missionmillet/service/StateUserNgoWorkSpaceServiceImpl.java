package org.sdrc.missionmillet.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.time.DateUtils;
import org.sdrc.missionmillet.domain.Area;
import org.sdrc.missionmillet.domain.NGO;
import org.sdrc.missionmillet.domain.NGOSoEReport;
import org.sdrc.missionmillet.domain.NgoReports;
import org.sdrc.missionmillet.domain.NgoReportsFile;
import org.sdrc.missionmillet.domain.TimePeriod;
import org.sdrc.missionmillet.domain.TypeDetails;
import org.sdrc.missionmillet.model.DistrictUserNgoListModel;
import org.sdrc.missionmillet.model.NgoReportsModel;
import org.sdrc.missionmillet.repository.NGOSoEReportRepository;
import org.sdrc.missionmillet.repository.NgoReportsFileRepository;
import org.sdrc.missionmillet.repository.NgoReportsRepository;
import org.sdrc.missionmillet.repository.TimePeriodRepository;
import org.sdrc.missionmillet.repository.TypeDetailsRepository;
import org.sdrc.missionmillet.util.MissionMilletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Maninee Mahapatra(maninee@sdrc.co.in) date(20-12-2017)
 * 
 *
 */
@Service
public class StateUserNgoWorkSpaceServiceImpl implements StateUserNgoWorkSpaceService {

	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;

	@Autowired
	private NGOSoEReportRepository nGOSoEReportRepository;

	@Autowired
	private NgoReportsRepository ngoReportsRepository;

	@Autowired
	private TimePeriodRepository timePeriodRepository;

	@Autowired
	private NgoReportsFileRepository ngoReportsFileRepository;

	@Autowired
	private TypeDetailsRepository typeDetailsRepository;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat dateFormatNew = new SimpleDateFormat("MMM");
	private SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Logger LOGGER = LoggerFactory.getLogger("LOGGER");
	/**
	 * @Description Retrieves all the ngo list 
	 */
	@Override
	public Map<String, List<DistrictUserNgoListModel>> retrieveStatetNgoListSoEStatus() {
		List<DistrictUserNgoListModel> ngolist = new ArrayList<DistrictUserNgoListModel>();
		DistrictUserNgoListModel districtUserNgoListModel = null;
		Integer pendingId = Integer.parseInt(messageSourceNotification.getMessage("typedetails.pending", null, null));
		// Get all details of ngo SoE For taking any action
		List<Object[]> listOfNgoStatusSoE = nGOSoEReportRepository.findSoEStatusOfAllDistrict(pendingId,
				new java.util.Date());
		List<TimePeriod> timePeriods = timePeriodRepository.findByPeriodicity(messageSourceNotification.getMessage("timeperiod.periodicity.monthly", null, null));
		// Iterate the list and set into a model to be sent to frontend
		for (Object[] objects : listOfNgoStatusSoE) {
			districtUserNgoListModel = new DistrictUserNgoListModel();
			districtUserNgoListModel.setDistrictId((int) objects[0]);
			districtUserNgoListModel.setDistrictName((String) objects[1]);
			districtUserNgoListModel.setNgoId((int) objects[2]);
			districtUserNgoListModel.setNgoName((String) objects[3]);
			districtUserNgoListModel.setMonthInt((Integer) objects[4]);
			districtUserNgoListModel.setYear((Integer) objects[5]);
			Timestamp tmst = (Timestamp) objects[6];
			districtUserNgoListModel.setDate(tmst.toString().substring(0, 10));

			Month month = Month.of(districtUserNgoListModel.getMonthInt() + 1);
			districtUserNgoListModel.setMonth(month.getDisplayName(TextStyle.SHORT, Locale.UK));

			TimePeriod timePeriod = NgoReportsServiceImpl.getTimePeriodByMonthAndYear(timePeriods,
					month.getDisplayName(TextStyle.SHORT, Locale.UK), (Integer) objects[5]);
			districtUserNgoListModel.setFinancialyear(timePeriod.getFinancialYear());

			ngolist.add(districtUserNgoListModel);
		}

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
	}

	/**
	 * @Description This method retrieve all the history of all ngo of the state
	 */
	@Override
	public Map<String, List<DistrictUserNgoListModel>> retrieveStatetNgoSoEHistory() {
		List<DistrictUserNgoListModel> ngolist = new ArrayList<DistrictUserNgoListModel>();
		DistrictUserNgoListModel districtUserNgoListModel = null;
		List<TypeDetails> details = typeDetailsRepository.findAll();
		Integer approveId = Integer.parseInt(messageSourceNotification.getMessage("typedetails.approved", null, null));
		Integer rejectId = Integer.parseInt(messageSourceNotification.getMessage("typedetails.rejected", null, null));
		Integer autoApprove = Integer
				.parseInt(messageSourceNotification.getMessage("typedetails.auto.approved", null, null));
		Integer pendingId = Integer
				.parseInt(messageSourceNotification.getMessage("typedetails.pending.upload", null, null));
		//Get details action history of ngo SoE
		List<Object[]> listOfNgoStatusSoE = nGOSoEReportRepository.findSoEHistoryOfAllDistrict(
				new Timestamp(new java.util.Date().getTime()), approveId, rejectId, autoApprove, pendingId);
		List<TimePeriod> timePeriods = timePeriodRepository.findByPeriodicity("1");
		//Iterate the list and set it into a model to be sent to frontend
		for (Object[] objects : listOfNgoStatusSoE) {
			districtUserNgoListModel = new DistrictUserNgoListModel();
			districtUserNgoListModel.setDistrictId((int) objects[0]);
			districtUserNgoListModel.setDistrictName((String) objects[1]);
			districtUserNgoListModel.setNgoId((int) objects[2]);
			districtUserNgoListModel.setNgoName((String) objects[3]);
			districtUserNgoListModel.setMonthInt(objects[4] != null ? (Integer) objects[4] : (Integer) objects[11]);
			districtUserNgoListModel.setYear(objects[5] != null ? (Integer) objects[5] : (Integer) objects[12]);
			Timestamp createddate = objects[6] != null ? (Timestamp) objects[6] : null;
			districtUserNgoListModel.setDate(createddate == null ? null : createddate.toString().substring(0, 10));
			districtUserNgoListModel.setStatusDetail(objects[7] != null ? NgoReportsServiceImpl
					.getTypeDetailsByTypeDetailsId(details, Integer.valueOf(objects[7].toString())).getTypeDetailName()
					: NgoReportsServiceImpl
							.getTypeDetailsByTypeDetailsId(details, Integer.valueOf(objects[14].toString()))
							.getTypeDetailName());
			Timestamp actiontakendate = objects[8] != null ? (Timestamp) objects[8] : null;
			districtUserNgoListModel
					.setActionTakenDate(actiontakendate == null ? null : actiontakendate.toString().substring(0, 10));
			districtUserNgoListModel.setReportId(objects[9] != null ? (Integer) objects[9] : null);
			districtUserNgoListModel.setRemark(actiontakendate == null ? null : (String) objects[10]);
			districtUserNgoListModel.setActionTakenBy(objects[15] != null ? (int) objects[15] : 0);
			districtUserNgoListModel.setActionTakenByName(objects[16] != null ? (String) objects[16] : null);
			
			Month month = Month.of(districtUserNgoListModel.getMonthInt() + 1);
			districtUserNgoListModel.setMonth(month.getDisplayName(TextStyle.SHORT, Locale.UK));

			TimePeriod timePeriod = NgoReportsServiceImpl.getTimePeriodByMonthAndYear(timePeriods,
					month.getDisplayName(TextStyle.SHORT, Locale.UK), districtUserNgoListModel.getYear());
			districtUserNgoListModel.setFinancialyear(timePeriod.getFinancialYear());

			ngolist.add(districtUserNgoListModel);
		}

		List<DistrictUserNgoListModel> reportsModels = null;
		Map<String, List<DistrictUserNgoListModel>> map = new TreeMap<String, List<DistrictUserNgoListModel>>(Collections.reverseOrder());
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
	 * @author subham (subham@sdrc.co.in)
	 * 
	 * @Description get all the Report details uploaded by NGO to display in history
	 * section, financial year wise
	 */
	@Override
	@Transactional
	public Map<String, List<NgoReportsModel>> stateUserNgoDistList() {

		String fCheck = financialYearCheck();

		List<NGOSoEReport> soeReportList = nGOSoEReportRepository.findAll();

		if (soeReportList == null) {
			LOGGER.error("Description : "+"Action :"+ messageSourceNotification.getMessage
					("ngo.report.unavailable", null,null)+" : "+fullDateFormat.format(new Date()));
			return null;
		}

		// put month and year as a key so that for every month and year , report must be after deadLine date
		Map<String, Timestamp> soeMap = new HashMap<String, Timestamp>();

		for (NGOSoEReport ngoSoeReport : soeReportList) {

			soeMap.put(ngoSoeReport.getMonth() + "_" + ngoSoeReport.getYear() + "_" + ngoSoeReport.getNgo().getId(),
					ngoSoeReport.getFirstDeadlineDate());

		}

		//fetching all the reports whose isLive is true
		List<NgoReportsFile> ngoReportsFiles = ngoReportsFileRepository.findByIsLiveTrue();

		List<NgoReportsModel> list = new ArrayList<NgoReportsModel>();

		Calendar cal = Calendar.getInstance();

		int ngoReport = Integer.valueOf(messageSourceNotification.getMessage("ngo.upload.report", null, null));

		List<TimePeriod> timePeriods = timePeriodRepository
				.findByPeriodicity(messageSourceNotification.getMessage("timeperiod.periodicity.monthly", null, null));

		List<Object[]> objectList = ngoReportsRepository.findByIsLiveTrueOrderByDistNameNgoNameYearAndMinCreatedDate();

		//iterating loop to set all details in model that to be sent to frontend
		for (Object[] o : objectList) {

			Date currentDate = new Date();

			NgoReports ngoReports = (NgoReports) o[0];
			NGO ngo = (NGO) o[1];
			Area area = (Area) o[2];

			Timestamp deadLineDate = soeMap.get(ngoReports.getMonth() + "_" + ngoReports.getYear() + "_" + ngo.getId());

			// checking report is after deadline date or not
			if (DateUtils.isSameDay(currentDate, deadLineDate) | currentDate.after(deadLineDate)) {

				NgoReportsModel ngoReportsModel = new NgoReportsModel();

				ngoReportsModel.setCreatedDate(ngoReports.getCreatedDate() != null ? 
						simpleDateFormat.format(ngoReports.getCreatedDate()): null);
				if ((Integer) ngoReports.getMonth() != null) {
					cal.set(Calendar.MONTH, ngoReports.getMonth());
				}

				ngoReportsModel.setMonth((Integer) ngoReports.getMonth() != null ? dateFormatNew.format(cal.getTime()) : null);
				ngoReportsModel.setReportsId(ngoReports.getReportsId());
				ngoReportsModel.setYear((Integer) ngoReports.getYear());
				ngoReportsModel.setRemarks(ngoReports.getRemarks() != null ? ngoReports.getRemarks() : null);
				ngoReportsModel.setReportTypeId(ngoReports.getReportType().getTypeDetailId());
				ngoReportsModel.setReportType(ngoReports.getReportType().getTypeDetailName());

				ngoReportsModel.setNgoId(ngo.getId());
				ngoReportsModel.setNgoName(ngo.getName());

				ngoReportsModel.setDistName(area.getAreaName());

				List<NgoReportsFile> ngoReportsFileValue = NgoReportsServiceImpl.getReportTypeDetails(ngoReportsFiles,
						ngoReports.getReportsId()) != null? NgoReportsServiceImpl.getReportTypeDetails
								(ngoReportsFiles, ngoReports.getReportsId()): null;

				if (ngoReportsFileValue != null) {
					for (int j = 0; j < ngoReportsFileValue.size(); j++) {
						if (ngoReportsFileValue.get(j).getStatus().getTypeDetailId() == ngoReport) {
							ngoReportsModel.setDownloadUploadedFile(ngoReportsFileValue.get(j).getStatus().getTypeDetailId());
						} 
					}
				}

				TimePeriod timePeriod = NgoReportsServiceImpl.getTimePeriodByMonthAndYear(timePeriods,
						dateFormatNew.format(cal.getTime()), ngoReports.getYear());
				ngoReportsModel.setFinancialYear(timePeriod != null ? timePeriod.getFinancialYear() : null);

				//only current financial year report can be deleted so checking which record lies in current financial year
				//and udating its check value to true
				if (fCheck.equals(timePeriod.getFinancialYear())) {
					ngoReportsModel.setCheck(true);
				} else
					ngoReportsModel.setCheck(false);

				list.add(ngoReportsModel);
			}

		}

		//making financial year as a key
		List<NgoReportsModel> reportsModels = null;

		Map<String, List<NgoReportsModel>> map = new HashMap<String, List<NgoReportsModel>>();

		for (int i = 0; i < list.size(); i++) {

			if (map.containsKey(list.get(i).getFinancialYear())) {
				reportsModels = map.get(list.get(i).getFinancialYear());
				reportsModels.add(list.get(i));
				map.put(list.get(i).getFinancialYear(), reportsModels);
			} else {
				reportsModels = new ArrayList<NgoReportsModel>();
				reportsModels.add(list.get(i));
				map.put(list.get(i).getFinancialYear(), reportsModels);
			}
		}

		return map;
	}

	public static String financialYearCheck() {

		// just to check timePeriod is in same financial year or not
		Calendar cal = Calendar.getInstance();
		int month1 = cal.get(Calendar.MONTH);

		// for financial year
		int preYear = 0, nextYear = 0;
		cal = Calendar.getInstance();
		if (month1 > 2) {

			preYear = cal.get(Calendar.YEAR);
			cal.add(Calendar.YEAR, +1);
			nextYear = cal.get(Calendar.YEAR);

		} else {
			nextYear = cal.get(Calendar.YEAR);
			cal.add(Calendar.YEAR, -1);
			preYear = cal.get(Calendar.YEAR);
		}

		String fCheck = preYear + "-" + nextYear;

		return fCheck;
	}
	/**
	 * @author subham (subham@sdrc.co.in)
	 * 
	 * @Description Returns the file path of report, which is to be downloaded in the history section of Report
	 * 
	 * @param reportId 
	 * @param typeId
	 * @param reportTypeId
	 * @param ngoId
	 */
	@Override
	@Transactional
	public String reportAndCertificateDownload(int reportId, int typeId, int reportTypeId, int ngoId) {

		NgoReportsFile ngoReportsFile = ngoReportsFileRepository.getFileByNgoIdReportTypeIdReportId
				(reportId, typeId,reportTypeId, ngoId);
		
		if (ngoReportsFile != null) {
			
			String path = MissionMilletUtil.getPdfFile(ngoReportsFile.getFilePath(),
					messageSourceNotification.getMessage("ngo.upload.report.path", null, null));
			
			LOGGER.info("Description : "+"Action :"+ messageSourceNotification.getMessage
					("ngo.download.report.message.success", null,null)+" : "+fullDateFormat.format(new Date()));
			return path.toString();

		} else {
			LOGGER.error("Error description : "+"Action :"+ messageSourceNotification.getMessage
					("ngo.download.report.message.error", null,null)+" : "+fullDateFormat.format(new Date()));
			return messageSourceNotification.getMessage("file.not.founds", null, null);
		}
	}

	
	/**
	 * @author subham (subham@sdrc.co.in)
	 * 
	 * @Description This method delete the report uploaded by NGO by setting isLive value as false
	 * 
	 * @param reportId
	 */
	@Override
	@Transactional
	public boolean deleteReportsAndCertificate(Integer reportId) {

		//finding the required report to be deleted
		NgoReports ngoReport = ngoReportsRepository.findByReportsId(reportId);
		//updating islive value as false in NgoReports table
		ngoReport.setLive(false);
		NgoReports deleteReport = ngoReportsRepository.save(ngoReport);
		
		List<NgoReportsFile> deleteReportFile = ngoReportsFileRepository.findByNgoReports(ngoReport);

		for (NgoReportsFile file : deleteReportFile) {
			//updating islive value as false in NgoReportsFile table
			file.setLive(false);
		}

		Iterable<NgoReportsFile> newFileList = ngoReportsFileRepository.save(deleteReportFile);

		if (deleteReport != null && newFileList != null) {
			LOGGER.info("Description : "+"Action :"+ messageSourceNotification.getMessage
					("ngo.report.delete.success", null,null)+" : "+fullDateFormat.format(new Date()));
			return true;
		}

		LOGGER.error("Description : "+"Action :"+ messageSourceNotification.getMessage
				("ngo.report.delete.error", null,null)+" : "+fullDateFormat.format(new Date()));
		return false;
	}

}
