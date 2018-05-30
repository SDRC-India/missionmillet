package org.sdrc.missionmillet.service;

import java.util.List;
import java.util.Map;

import org.sdrc.missionmillet.model.DropdownModel;
import org.sdrc.missionmillet.model.NGOSoEReportModel;
import org.sdrc.missionmillet.model.NGOSoEUploadsStatusModel;
import org.sdrc.missionmillet.model.NGOSoEViewModel;
import org.sdrc.missionmillet.model.NgoReportsModel;

/**
 * 
 * @author Abhisheka
 * 
 */
public interface NgoReportsService {

	String upload(NgoReportsModel ngoReportsModel) throws Exception;
	
	Map<String, List<NgoReportsModel>> getNgoReportsDetails();
	
	String downloadReportData(int reportId,int typeId,int reportTypeId) throws Exception;
	
	String uploadSoe(NGOSoEReportModel ngoSoEReportModel) throws Exception;
	
	Map<String, List<DropdownModel>> dropdownValues() throws Exception;
	
	Map<String, List<NGOSoEViewModel>> viewSoeDetails();
	
	void insertIntoSoeUploadStatus();
	
	Map<String, List<NGOSoEUploadsStatusModel>> getUploadTableDetails() throws Exception;

	String downloadSOEreport(int month,int year) throws Exception;
	
	String downloadSOEData(int reportId,int typeId) throws Exception;

	String downloadLatestSOE(int month, int year);

}
