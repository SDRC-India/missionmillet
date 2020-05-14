package org.sdrc.missionmillet.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.sdrc.missionmillet.model.DistrictUserNgoListModel;
import org.sdrc.missionmillet.model.DistrictUserUploadSoEModel;

/**
 * @author Maninee Mahapatra(maninee@sdrc.co.in) This controller for district
 *         User date(08-12-2017)
 * 
 *
 */

public interface DistrictUserDownloadTemplateService {

	/**
	 * @throws Exception
	 *             update SoETemplate table for all ngo district user download which
	 *             template for update budget for ngo These templates are updated by
	 *             this method
	 */
	void districtUserupdateSoE() throws Exception;

	/**
	 * @return <List> of ngo list This method retrieve all ngolist present in
	 *         corresponding district for current time period
	 */
	List<DistrictUserNgoListModel> retrieveDistrictNgoList();

	/**
	 * @param ngoId
	 * @param ngoName
	 * @return string downloaded filepath This method download the blank template by
	 *         district user
	 * @throws Exception
	 */
	String downloadTemplate(Integer ngoId, String ngoName) throws Exception;

	/**
	 * @return List<DistrictUserNgoListModel> This method used to retrieve all the
	 *         ngo list of corresponding district after deadline date who upload
	 *         their filled SOE for approved/reject
	 * @throws Exception
	 */
	Map<String, List<DistrictUserNgoListModel>> retrieveDistrictNgoListSoEStatus() throws Exception;

	/**
	 * @param districtUserUploadSoEModel
	 * @return String approve This method call when district user click the approve
	 *         button in pending action section of district user ngo workspace and
	 *         State user ngo workspace manage page
	 * @throws Exception
	 */
	String approveSoEStatus(DistrictUserUploadSoEModel districtUserUploadSoEModel) throws Exception;

	/**
	 * @param districtUserUploadSoEModel
	 * @return String reject This method call when district user click the reject
	 *         button in pending action section of district user ngo workspace and
	 *         State user ngo workspace manage page
	 */
	String rejectSoEStatus(DistrictUserUploadSoEModel districtUserUploadSoEModel);

	/**
	 * @return List<DistrictUserNgoListModel> This method call when district use
	 *         click the report button in report page of ngo workspace
	 */
	Map<String, List<DistrictUserNgoListModel>> retrieveDistNgoReport();

	/**
	 * @param districtUserUploadSoEModel
	 * @return String filepath This method call when district user download Icon For
	 *         seen the uploaded SOE of corresponding Ngo for approve/reject in
	 *         pending action section of district user ngo workspace manage page
	 * @throws Exception
	 */
	String downloadDistNgoSoEForAction(DistrictUserUploadSoEModel districtUserUploadSoEModel) throws Exception;

	/**
	 * @return List<DistrictUserNgoListModel> This method call when district user
	 *         download Icon For seen the uploaded SOE of corresponding Ngo for
	 *         approve/reject in pending action section of district user ngo
	 *         workspace manage page
	 */
	Map<String, List<DistrictUserNgoListModel>> retrieveDistNgoHistory();

	/**
	 * @param districtUserUploadSoEModel
	 * @return String filepath This method call when district user click download
	 *         Icon For seen the uploaded Report of corresponding Ngo
	 * @throws Exception
	 */
	String downloadDistNgoReport(DistrictUserUploadSoEModel districtUserUploadSoEModel);

	/**
	 * @param reportId
	 * @param districtUserUploadSoEModel
	 * @return string filepath when district user click on download icon to seen
	 *         rejected or approve soe in history section
	 * @throws Exception
	 */
	String downloadDistNgoHistoryFile(Integer reportId, DistrictUserUploadSoEModel districtUserUploadSoEModel)
			throws Exception;

	/**
	 * This method used for job scheduled for auto approve
	 * @throws IOException 
	 */
	void autoApprove() throws IOException;

	
	/**
	 * This method used for job scheduled in every october(00:00:00) time to false status of soe template
	 */
	void setStausFalseOfSoETemplateInOctober();
	
	

	/**
	 * @param ngoId
	 * @param periodicity
	 * @return byte[] file
	 * @Description If month is April then fetch the blank SoE template
	 */
	public byte[] getBlankSoETemplate(Integer ngoId, String periodicity);

	/**
	 * @Description If month is October then find the latest updated SoE
	 * 
	 * @param ngoId
	 * @param periodicity
	 * @return byte[] file
	 * 
	 */
	public byte[] getSoETemplateForOctober(Integer ngoId, String periodicity); 
	
}
