package org.sdrc.missionmillet.controller;

import java.util.List;
import java.util.Map;

import org.sdrc.missionmillet.core.Authorize;
import org.sdrc.missionmillet.model.StateDropDownModel;
import org.sdrc.missionmillet.model.ValueObject;
import org.sdrc.missionmillet.model.WASSANReportsModel;
import org.sdrc.missionmillet.model.WASSANSoEReportModel;
import org.sdrc.missionmillet.model.YearAndMonthModel;
import org.sdrc.missionmillet.service.StateUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Subham Ashish (subham@sdrc.co.in)
 * 
 * @Description This controller has methods for Statement of Expenditure(SOE) and Reports of STATE level users. 
 */

@Controller
public class StateUserController {

	@Autowired
	private StateUserService stateUserService;
	
	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;

	/**
	 * upload SOE TEMPLATE
	 */
	@Authorize(feature = "state_soe_reports", permission = "edit")
	@ResponseBody
	@RequestMapping(value = "uploadStateSoE", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public ResponseEntity<String> uploadSoE(@RequestPart MultipartFile file) {
		return stateUserService.wassanSoEReportSave(file);
	}

	/**
	 * download SOE template
	 */
	
	@Authorize(feature = "state_soe_reports", permission = "view")
	@ResponseBody
	@RequestMapping(value = "downloadStateTemplate", method = RequestMethod.POST)
	public String downloadTemplate() {
		return stateUserService.downloadStateTemplate();
	}

	/**
	 * History of SoE
	 */
	@Authorize(feature = "state_soe_reports", permission = "view")
	@RequestMapping(value = "stateSoEHistory")
	@ResponseBody
	public Map<String, List<WASSANSoEReportModel>> getSoEHistoryDetails() {

		return stateUserService.wassonSoEHistoryDetails();
	}

	/**
	 * download SOE in history section
	 */
	@Authorize(feature = "state_soe_reports", permission = "view")
	@ResponseBody
	@RequestMapping(value = "monthlySoEDownload", method = RequestMethod.POST)
	public String downloadSoE(@RequestParam("id") Integer id) {
		return stateUserService.wassanMonthlySoEDownload(id);
	}

	
	/**
	 * month and financial year, display change dynamically 
	 */
	@Authorize(feature = "state_soe_reports", permission = "view")
	@ResponseBody
	@RequestMapping(value = "soeMonthAndFinancialYear")
	YearAndMonthModel monthAndFinancialYearDetail() {
		return stateUserService.monthAndFinancialYearDetail();

	}

	/**
	 * State report uploading
	 */
	@Authorize(feature = "state_report", permission = "edit")
	@RequestMapping(value = "uploadStateReport", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	@ResponseBody
	public ResponseEntity<String> UploadReport(@RequestPart("model") WASSANReportsModel wassanReportsModel,
			@RequestPart("file") MultipartFile file) {

		wassanReportsModel.setUploadedFile(file);
		return stateUserService.wassanReportSave(wassanReportsModel);
	}

	/**
	 *  history of state reports
	 */
	@Authorize(feature = "state_report", permission = "view")
	@RequestMapping(value = "StateReportHistory")
	@ResponseBody
	public Map<String, List<WASSANReportsModel>> getReportHistoryDetails() {
		return stateUserService.wassonReportHistoryDetails();
	}

	/**
	 * dropdown values for year,month and report-type
	 */
	@Authorize(feature = "state_report", permission = "view")
	@ResponseBody
	@RequestMapping(value = "dropDownList")
	public Map<String, List<StateDropDownModel>> dropDownValue() {
		return stateUserService.dropDownList();
	}

	/**
	 * download report 
	 */
	@Authorize(feature = "state_report", permission = "view")
	@RequestMapping(value = "downloadReport")
	@ResponseBody
	public String downloadReport(@RequestParam("reportId") Integer reportId,
			@RequestParam("typeId") Integer typeId) {
		return stateUserService.wassanReportDownload(reportId, typeId);
	}

	/**
	 * delete Report
	 */
	@Authorize(feature = "state_report", permission = "edit")
	@RequestMapping(value = "deleteReport")
	@ResponseBody
	public ResponseEntity<String> deleteReport(
			@RequestParam("wassanReportId") Integer wassanReportId) {
		try {
			return stateUserService.deleteReport(wassanReportId);
		} catch (BadCredentialsException e) {
			return new ResponseEntity<String>(messageSourceNotification.getMessage("user.session.expired", null, null), HttpStatus.BAD_GATEWAY);
		}catch (Exception e) {
			return new ResponseEntity<String>(messageSourceNotification.getMessage("unknown.error", null, null), HttpStatus.BAD_GATEWAY);
		}
		
	}

	@Authorize(feature = "state_soe_reports", permission = "view")
	@ResponseBody
	@RequestMapping(value = "getPeriodicity")
	public List<ValueObject> getPeriodicity() {
		return stateUserService.getPeriodicity();
	}

	@Authorize(feature = "state_soe_reports", permission = "view")
	@ResponseBody
	@RequestMapping(value = "/getTypeOfConfiguration")
	public List<ValueObject> getTypeOfConfiguration() {
		return stateUserService.getTypeOfConfiguration();
	}

	@ResponseBody
	@Authorize(feature = "state_soe_reports", permission = "view")
	@RequestMapping(value = "/getTypeOfAggregation")
	public Map<String, Map<String, List<ValueObject>>> getTypeOfAggregation() {
		return stateUserService.getTypeOfAggregation();
	}

	@Authorize(feature = "state_soe_reports", permission = "edit")
	@ResponseBody
	@RequestMapping(value = "/setConfiguration", method = RequestMethod.POST)
	public String setConfiguration(
			@RequestParam("cutOffDays") Integer cutOffDays,
			@RequestParam("typeDetailsId") Integer typeDetailsId) {
		return stateUserService.setConfiguration(cutOffDays, typeDetailsId);
	}

	@Authorize(feature = "state_report", permission = "edit")
	@RequestMapping("/stateNgoReport")
	String stateNgoReport() {
		return "stateNgoReport";
	}
	
	@Authorize(feature = "state_report", permission = "edit")
	@RequestMapping("/stateReports")
	String stateReports() {
		return "stateReports";
	}

	@Authorize(feature = "state_soe_reports", permission = "view")
	@RequestMapping("/stateSoE")
	String stateSoE() {
		return "stateSoE";
	}

	@Authorize(feature = "state_soe_reports", permission = "view")
	@RequestMapping("/stateNgoSoEHistory")
	String stateNgoSoEHistory() {
		return "stateNgoSoEHistory";
	}

	@Authorize(feature = "state_soe_reports", permission = "view")
	@RequestMapping("/stateNgoSoEManage")
	String stateNgoSoEManage() {
		return "stateNgoSoEManage";
	}

	@Authorize(feature = "state_soe_reports", permission = "view")
	@RequestMapping("/stateNgoSoeReport")
	String stateNgoSoeReport() {
		return "stateNgoSoeReport";
	}

	@Authorize(feature = "state_soe_reports", permission = "view")
	@RequestMapping("/configuration")
	String configuration() {
		return "configuration";
	}

}
