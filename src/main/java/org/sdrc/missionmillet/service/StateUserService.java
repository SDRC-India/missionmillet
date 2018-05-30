package org.sdrc.missionmillet.service;

import java.util.List;
import java.util.Map;

import org.sdrc.missionmillet.model.StateDropDownModel;
import org.sdrc.missionmillet.model.ValueObject;
import org.sdrc.missionmillet.model.WASSANReportsModel;
import org.sdrc.missionmillet.model.WASSANSoEReportModel;
import org.sdrc.missionmillet.model.YearAndMonthModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Subham Ashish (subham@sdrc.co.in)
 * 
 */
public interface StateUserService {

	/**
	 * @param file
	 * @return it accepts a multipart file and save it in DB
	 * @author Subham Ashish (subham@sdrc.co.in)
	 */
	ResponseEntity<String> wassanSoEReportSave(MultipartFile file);

	/**
	 * @return history details of wassan soe
	 */
	Map<String, List<WASSANSoEReportModel>> wassonSoEHistoryDetails();

	/**
	 * @param id
	 * @return it download SOE in history detail column
	 * @author Subham Ashish (subham@sdrc.co.in)
	 */
	String wassanMonthlySoEDownload(Integer id);

	/**
	 * @param wassanReportsModel
	 * @return it accepts all details and save in db for report part wassan level
	 * @author Subham Ashish (subham@sdrc.co.in)
	 */
	ResponseEntity<String> wassanReportSave(WASSANReportsModel wassanReportsModel);

	/**
	 * @return displays history of reports
	 * @author Subham Ashish (subham@sdrc.co.in)
	 */
	Map<String, List<WASSANReportsModel>> wassonReportHistoryDetails();

	/**
	 * @param wassanReportId
	 * @return it delete wassan level report
	 * @author Subham Ashish (subham@sdrc.co.in)
	 */
	 ResponseEntity<String> deleteReport(Integer wassanReportId);

	/**
	 * @return it download soe template
	 * @author Subham Ashish (subham@sdrc.co.in)
	 */
	String downloadStateTemplate();

	List<ValueObject> getPeriodicity();

	List<ValueObject> getTypeOfConfiguration();
	
	/**
	 * @return drop down list for report type, month 
	 */
	Map<String, List<StateDropDownModel>> dropDownList();

	Map<String, Map<String, List<ValueObject>>> getTypeOfAggregation();

	/**
	 * @param reportId
	 * @param typeId
	 * @return it download reports ie certificate and report file
	 * @author Subham Ashish (subham@sdrc.co.in)
	 */
	String wassanReportDownload(Integer reportId,Integer typeId);

	public String setConfiguration(Integer cutOffDays, Integer typeDetailsId);

	/**
	 * @return it displays financial year and month dynamically changes accoring to date
	 * @author Subham Ashish (subham@sdrc.co.in)
	 */
	YearAndMonthModel monthAndFinancialYearDetail();
	
}
