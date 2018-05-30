package org.sdrc.missionmillet.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.sdrc.missionmillet.core.Authorize;
import org.sdrc.missionmillet.model.CollectUserModel;
import org.sdrc.missionmillet.model.DropdownModel;
import org.sdrc.missionmillet.model.NGOSoEReportModel;
import org.sdrc.missionmillet.model.NGOSoEUploadsStatusModel;
import org.sdrc.missionmillet.model.NGOSoEViewModel;
import org.sdrc.missionmillet.model.NgoReportsModel;
import org.sdrc.missionmillet.service.DistrictUserDownloadTemplateService;
import org.sdrc.missionmillet.service.NgoReportsService;
import org.sdrc.missionmillet.service.UserService;
import org.sdrc.missionmillet.util.Constants;
import org.sdrc.missionmillet.util.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Abhisheka Mishra(abhisheka@sdrc.co.in)
 * @description: This controller has methods for Statement of Expenditure(SOE) and Reports of NGO level users. 
 */
@Controller
public class NgoController {
	
	@Autowired
	private NgoReportsService ngoReportsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DistrictUserDownloadTemplateService districtUserDownloadTemplateService;
	
	@Autowired
	private StateManager stateManager;
	
	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;
	
	private static final Logger	LOGGER	= LoggerFactory.getLogger( "LOGGER" );

	private SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * Upload in Reports
	 */
	@Authorize(feature="ngo_report",permission="edit")
	@RequestMapping(value="/uploadNgoReport",method=RequestMethod.POST,consumes= { "multipart/form-data" })
	@ResponseBody
	public String uploadExcel(@RequestPart("model") NgoReportsModel ngoReportsModel,@RequestPart("file") MultipartFile file){
		try {
			ngoReportsModel.setUploadedFile(file);
			return ngoReportsService.upload(ngoReportsModel);
		} catch (Exception e) {
			LOGGER.error("Error description", e);
			return messageSourceNotification.getMessage("upload.status.failed",null, null);
		}
	}
	
	/**
	 * History in Reports
	 */
	@Authorize(feature="ngo_report",permission="view")
	@RequestMapping(value="/viewNgoReportData",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, List<NgoReportsModel>> viewNgoReportData(){
		return ngoReportsService.getNgoReportsDetails();
	}
	
	/**
	 * Downloads Report History
	 */
	@Authorize(feature="ngo_report",permission="view")
	@RequestMapping(value="/downloadReportData",method=RequestMethod.POST)
	@ResponseBody
	public String downloadReportData(int reportId,int typeId,int reportTypeId) {
		try {
			return ngoReportsService.downloadReportData(reportId, typeId,reportTypeId);
		} catch (Exception e) {
			LOGGER.error("Error description", e);
			return messageSourceNotification.getMessage("download.status.failed",null, null);
		}
	}
	
	/*
	 * Upload in SOE
	 */
	@Authorize(feature="ngo_soe_reports",permission="edit")
	@RequestMapping(value="/uploadSoe",method=RequestMethod.POST,consumes= { "multipart/form-data" })
	@ResponseBody
	public String uploadSoe(@RequestPart("model") NGOSoEReportModel ngoSoEReportModel,@RequestPart("file") MultipartFile[] file) {
		try {
			ngoSoEReportModel.setSoeFile(file);
			return ngoReportsService.uploadSoe(ngoSoEReportModel);
		} catch (Exception e) {
			LOGGER.error("Error description", e);
			return messageSourceNotification.getMessage("excel.sheet.not.matching",null, null);
		}
		
	}
	
	/*
	 * Report Type and Timeperiod in Reports
	 */
	@Authorize(feature="ngo_report",permission="view")
	@RequestMapping(value="/dropdownValues",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, List<DropdownModel>> dropdownValues() {
		try {
			return ngoReportsService.dropdownValues();
		} catch (Exception e) {
			LOGGER.error("Error description : "+fullDateFormat.format(new Date()), e);
			return null;
		}
	}
	
	/*
	 * History in SOE
	 */
	@Authorize(feature="ngo_soe_reports",permission="view")
	@RequestMapping(value="/viewSoeDetails",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, List<NGOSoEViewModel>> viewSoeDetails(){
		return ngoReportsService.viewSoeDetails();
	}
	
	/*
	 * Upload Table Details in SOE
	 */
	@Authorize(feature="ngo_soe_reports",permission="view")
	@RequestMapping(value="/getUploadTableDetails",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, List<NGOSoEUploadsStatusModel>> getUploadTableDetails(){
		try {
			return ngoReportsService.getUploadTableDetails();
		} catch (Exception e) {
			LOGGER.error("Error description : "+fullDateFormat.format(new Date()), e);
			return null;
		}
	}
	
	/*
	 * Download Template in Upload SOE
	 */
	@Authorize(feature="ngo_soe_reports",permission="view")
	@RequestMapping(value="/downloadSOEreport",method=RequestMethod.POST)
	@ResponseBody
	public String downloadSOEreport(int month,int year){
		try {
			return ngoReportsService.downloadSOEreport(month,year);
		} catch (Exception e) {
			LOGGER.error("Error description : "+fullDateFormat.format(new Date()), e);
			return messageSourceNotification.getMessage("download.soe.status.failed",null, null);
		}
	}
	
	/*
	 * Download in History SOE
	 */
	@Authorize(feature="ngo_soe_reports",permission="view")
	@RequestMapping(value="/downloadSOEData",method=RequestMethod.POST)
	@ResponseBody
	public String downloadSOEData(int reportId,int typeId){
		try {
			return ngoReportsService.downloadSOEData(reportId, typeId);
		} catch (Exception e) {
			LOGGER.error("Error description : "+ fullDateFormat.format(new Date()), e);
			return messageSourceNotification.getMessage("download.soe.status.failed",null, null);
		}
	}
	
	/*
	 * Download Latest Excel in Upload SOE
	 */
	@Authorize(feature="ngo_soe_reports",permission="view")
	@RequestMapping(value="/downloadLatestSOE",method=RequestMethod.POST)
	@ResponseBody
	public String downloadLatestSOE(int month,int year){
		return ngoReportsService.downloadLatestSOE(month, year);
	}
	
	@Authorize(feature="ngo_soe_reports",permission="edit")
	@RequestMapping("/ngoSoE")
	String ngoSoE(){
		return "ngoSoE"; 
	}
	
	@Authorize(feature="ngo_report",permission="edit")
	@RequestMapping("/ngoReport")
	String ngoReport(){
		return "ngoReport"; 
	}
		
	@RequestMapping(value = "/downloadSheet", method = RequestMethod.POST)
	public void downLoad(@RequestParam("fileName") String name, HttpServletResponse response) throws IOException {
		
		CollectUserModel collectUserModel = ((CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL));
		if(collectUserModel==null){
			LOGGER.error("Error description : "+fullDateFormat.format(new Date())+" : "
					+messageSourceNotification.getMessage("user.session.expired", null, null));
			throw new AccessDeniedException(messageSourceNotification.getMessage("user.session.expired", null, null));
		}
		InputStream inputStream;
		try {
			String fileName = name.replaceAll("%3A", ":").replaceAll("%2F", "/").replaceAll("%2C", ",")
					.replaceAll("\\+", " ").replaceAll("%20", " ").replaceAll("%26", "&").replaceAll("%5C", "/");
			inputStream = new FileInputStream(fileName);
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", new java.io.File(fileName).getName());
			response.setHeader(headerKey, headerValue);
			response.setContentType("pdf");
			ServletOutputStream outputStream = response.getOutputStream();
			FileCopyUtils.copy(inputStream, outputStream);
			outputStream.close();
			new File(fileName).delete();
			LOGGER.info(collectUserModel.getUsername()+ " : "+fullDateFormat.format(new Date())+" : "
					+fileName.split("/")[fileName.split("/").length-1]+" : "+ messageSourceNotification.getMessage("file.download.success", null, null));
		} catch (IOException e) {
			LOGGER.error("Error description : "+fullDateFormat.format(new Date())+" : "+collectUserModel.getUsername(), 
					messageSourceNotification.getMessage("file.not.available", null, null)+" : "+e);
		} 
	}
}
