package org.sdrc.missionmillet.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sdrc.missionmillet.core.Authorize;
import org.sdrc.missionmillet.model.DistrictUserNgoListModel;
import org.sdrc.missionmillet.model.DistrictUserUploadSoEModel;
import org.sdrc.missionmillet.service.DistrictUserDownloadTemplateService;
import org.sdrc.missionmillet.service.DistrictUserTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * @author Maninee Mahapatra(maninee@sdrc.co.in) This controller for district
 *         User date(08-12-2017)
 * 
 *
 */
@Controller
public class DistrictUserController {

	private static final Logger LOGGER = LoggerFactory.getLogger("LOGGER");

	@Autowired
	private DistrictUserTemplateService districtUserTemplateService;

	@Autowired
	private DistrictUserDownloadTemplateService districtUserDownloadTemplateService;

	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;

	/**
	 * @Description Request for retrieve ngo list with id and status when
	 *              districtNgoSoEManage page load this request call
	 * @return List<DistrictUserNgoListModel>
	 */
	@Authorize(feature = "district_soe_reports", permission = "view")
	@RequestMapping(value = "/retrieveDistrictNgoList", method = RequestMethod.GET)
	@ResponseBody
	public List<DistrictUserNgoListModel> retrieveDistrictNgoListTemplateSoE() {
		return districtUserDownloadTemplateService.retrieveDistrictNgoList();
	}

	/**
	 * @Description Request for download the template by district user to update
	 *              budget for six month
	 * @param request
	 * @param response
	 * @param ngoId
	 * @param ngoName
	 * @return String filepath
	 */
	@Authorize(feature = "district_soe_reports", permission = "view")
	@RequestMapping(value = "/downloadTemplate", method = RequestMethod.POST)
	@ResponseBody
	public String Download(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("ngoId") Integer ngoId, @RequestParam("ngoName") String ngoName) {

		try {
			return districtUserDownloadTemplateService.downloadTemplate(ngoId, ngoName);
		} catch (Exception e) {
			LOGGER.info("Error description", e);
			return messageSourceNotification.getMessage("download.status.failed", null, null);
		}

	}

	/**
	 * @Description Request for upload SoE for specific Ngo by district user after
	 *              update the budget
	 * @param request
	 * @return String success
	 */
	@Authorize(feature = "district_soe_reports", permission = "edit")
	@RequestMapping(value = "/uploadDistrictSoE", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	@ResponseBody
	public String Upload(MultipartHttpServletRequest request) {
		Integer ngoId = Integer.parseInt(request.getParameter("ngoId"));
		Integer timePeriodId = Integer.parseInt(request.getParameter("timePeriodId"));
		MultipartFile file = request.getFile("uploadedFile");
		LocalDate today = LocalDate.now();
		if ((today.getMonthValue() == Integer.parseInt(messageSourceNotification.getMessage("month.april", null, null))
				&& today.getDayOfMonth() <= Integer
						.parseInt(messageSourceNotification.getMessage("deadline.dayofmonth", null, null)))
				|| (today.getMonthValue() == Integer
						.parseInt(messageSourceNotification.getMessage("month.october", null, null))
						&& today.getDayOfMonth() <= 31 && today.getDayOfMonth() > Integer
								.parseInt(messageSourceNotification.getMessage("deadline.dayofmonth", null, null)))) {
			try {
				return districtUserTemplateService.uploadTemplate(file, request, ngoId, timePeriodId);
			} catch (Exception e) {
				LOGGER.info("Error description", e);
				return messageSourceNotification.getMessage("upload.correct.file", null, null);
			}
		} else {
			return "false";
		}

	}

	/**
	 * @Description Request for populate the ngo list which are upload their SoE for
	 *              approve or reject of corresponding district
	 * @return Map<String, List<DistrictUserNgoListModel>>
	 */
	@Authorize(feature = "district_soe_reports", permission = "view")
	@RequestMapping(value = "/retrieveNgoListSoEStatus", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, List<DistrictUserNgoListModel>> retrieveDistrictNgoListSoEStatus() {
		try {
			return districtUserDownloadTemplateService.retrieveDistrictNgoListSoEStatus();
		} catch (Exception e) {
			LOGGER.info("Error description", e);
			return null;
		}
	}

	/**
	 * @Description Request for download the ngo uploaded soe for approve/reject
	 * @param ngoId
	 * @param month
	 * @param year
	 * @param ngoName
	 * @return filepath
	 */
	@Authorize(feature = "district_soe_reports,state_soe_reports", permission = "view")
	@RequestMapping(value = "/getDistrictNgoSoEReport", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> retrieveDistrictNgoReport(@RequestParam("ngoId") Integer ngoId,
			@RequestParam("month") Integer month, @RequestParam("year") Integer year,
			@RequestParam("ngoName") String ngoName) {
		DistrictUserUploadSoEModel districtUserUploadSoEModel = new DistrictUserUploadSoEModel();
		districtUserUploadSoEModel.setNgoId(ngoId);
		districtUserUploadSoEModel.setMonth(month);
		districtUserUploadSoEModel.setYear(year);
		districtUserUploadSoEModel.setNgoName(ngoName);
		try {
			return new ResponseEntity<String>(
					districtUserDownloadTemplateService.downloadDistNgoSoEForAction(districtUserUploadSoEModel),
					HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.info("Error description", e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * @Description Request when district user approve the Ngo SoE
	 * @param ngoId
	 * @param month
	 * @param year
	 * @param reMark
	 * @return String approve
	 */
	@Authorize(feature = "district_soe_reports,state_soe_reports", permission = "edit")
	@RequestMapping(value = "/approveNgoSoEStatus", method = RequestMethod.GET)
	@ResponseBody
	public String approveNgoSoEStatus(@RequestParam("ngoId") Integer ngoId, @RequestParam("month") Integer month,
			@RequestParam("year") Integer year, @RequestParam("reMark") String reMark) {
		DistrictUserUploadSoEModel districtUserUploadSoEModel = new DistrictUserUploadSoEModel();
		districtUserUploadSoEModel.setNgoId(ngoId);
		districtUserUploadSoEModel.setMonth(month);
		districtUserUploadSoEModel.setYear(year);
		districtUserUploadSoEModel.setReMark(reMark);
		try {
			return districtUserDownloadTemplateService.approveSoEStatus(districtUserUploadSoEModel);
		} catch (Exception e) {
			LOGGER.info("Error description", e);
			return messageSourceNotification.getMessage("soe.not.approved", null, null);
		}
	}

	/**
	 * @Description Request when district user reject the Ngo SoE
	 * @param ngoId
	 * @param month
	 * @param year
	 * @param reMark
	 * @return String reject
	 */
	@Authorize(feature = "district_soe_reports,state_soe_reports", permission = "edit")
	@RequestMapping(value = "/rejectNgoSoEStatus", method = RequestMethod.GET)
	@ResponseBody
	public String rejectNgoSoEStatus(@RequestParam("ngoId") Integer ngoId, @RequestParam("month") Integer month,
			@RequestParam("year") Integer year, @RequestParam("reMark") String reMark) {
		DistrictUserUploadSoEModel districtUserUploadSoEModel = new DistrictUserUploadSoEModel();
		districtUserUploadSoEModel.setNgoId(ngoId);
		districtUserUploadSoEModel.setMonth(month);
		districtUserUploadSoEModel.setYear(year);
		districtUserUploadSoEModel.setReMark(reMark);
		return districtUserDownloadTemplateService.rejectSoEStatus(districtUserUploadSoEModel);
	}

	@Authorize(feature = "district_soe_reports", permission = "view")
	@RequestMapping("/districtNgoSoEManage")
	String districtUpdateSoE() {
		return "districtNgoSoEManage";
	}

	@Authorize(feature = "district_soe_reports", permission = "view")
	@RequestMapping("/districtNgoSoEHistory")
	String districtNgoSoEHistory() {
		return "districtNgoSoEHistory";
	}

	@Authorize(feature = "district_report", permission = "view")
	@RequestMapping("/districtNgoReport")
	String districtNgoReport() {
		return "districtNgoReport";
	}

	/**
	 * @Description Request for retrieve report of all ngo in corresponding district
	 * @return Map<String, List<DistrictUserNgoListModel>>
	 */
	@Authorize(feature = "district_report", permission = "view")
	@RequestMapping(value = "/getDistrictNgoReport", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, List<DistrictUserNgoListModel>> retrieveDistrictNgoReport() {

		return districtUserDownloadTemplateService.retrieveDistNgoReport();

	}

	/**
	 * @Description Request for download report from report page
	 * @param ngoId
	 * @param ngoName
	 * @param reportFileId
	 * @return String filepath
	 */
	@Authorize(feature = "district_report", permission = "view")
	@RequestMapping(value = "/downLoadDistrictNgoReport", method = RequestMethod.POST)
	@ResponseBody
	public String downloadDistrictNgoReport(@RequestParam("ngoId") Integer ngoId,
			@RequestParam("ngoName") String ngoName, @RequestParam("reportFileId") Integer reportFileId) {
		DistrictUserUploadSoEModel districtUserUploadSoEModel = new DistrictUserUploadSoEModel();
		districtUserUploadSoEModel.setNgoId(ngoId);
		districtUserUploadSoEModel.setNgoName(ngoName);
		districtUserUploadSoEModel.setReportFileId(reportFileId);
		return districtUserDownloadTemplateService.downloadDistNgoReport(districtUserUploadSoEModel);
	}

	/**
	 * @Description Request for retrieve history of all ngo in corresponding district
	 * @return Map<String, List<DistrictUserNgoListModel>>
	 */
	@Authorize(feature = "district_report", permission = "view")
	@RequestMapping(value = "/getDistrictNgoHistory", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, List<DistrictUserNgoListModel>> retrieveDistrictNgoHistory() {

		return districtUserDownloadTemplateService.retrieveDistNgoHistory();

	}

	/**
	 * @Description Request for download the SoE from history page
	 * @param reportId
	 * @param ngoId
	 * @param ngoName
	 * @return String filepath
	 */
	@Authorize(feature = "district_soe_reports,state_soe_reports", permission = "view")
	@RequestMapping(value = "/downLoadDistrictNgoHistoryFile", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> downloadDistrictNgoHistoryFile(@RequestParam("reportId") Integer reportId,
			@RequestParam("ngoId") Integer ngoId, @RequestParam("ngoName") String ngoName) {
		DistrictUserUploadSoEModel districtUserUploadSoEModel = new DistrictUserUploadSoEModel();
		districtUserUploadSoEModel.setNgoId(ngoId);
		districtUserUploadSoEModel.setNgoName(ngoName);
		try {
			return new ResponseEntity<String>(districtUserDownloadTemplateService.downloadDistNgoHistoryFile(reportId,
					districtUserUploadSoEModel), HttpStatus.OK);
		} catch (Exception re) {
			LOGGER.info("Error description", re);
			return new ResponseEntity<String>(re.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
