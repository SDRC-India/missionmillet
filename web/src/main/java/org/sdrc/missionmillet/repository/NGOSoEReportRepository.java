package org.sdrc.missionmillet.repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.sdrc.missionmillet.domain.CollectUser;
import org.sdrc.missionmillet.domain.NGO;
import org.sdrc.missionmillet.domain.NGOSoEReport;
import org.springframework.transaction.annotation.Transactional;

public interface NGOSoEReportRepository {

	@Transactional
	NGOSoEReport save(NGOSoEReport ngoSoEReport);

	List<Object[]> viewSoeDetails(List<Integer> list, int ngoId, Timestamp currentDate, int reUpload);

	List<NGOSoEReport> getDataFile(int userId, int templateId, Timestamp startDate, Timestamp endDate,
			List<Integer> typeDetails);

	/**
	 * @param districtId
	 * @param pendingId
	 * @param currentDate
	 * @Description list of ngo details of corresponding district whose SoE pending
	 *              for approve or reject after deadline date
	 */
	List<Object[]> findSoEStatusByDistrictId(int districtId, Integer pendingId, Date currentDate);

	/**
	 * @Description find status details of specific ngo for specific month and year
	 * @param ngo
	 * @param month
	 * @param year
	 * 
	 */
	NGOSoEReport findByNgoAndMonthAndYearAndIsLiveTrue(NGO ngo, Integer month, Integer year);
	/**
	 * @Description Getting the NGOSoEReport report by passing the user, ngo, month and year for that month and year
	 * 
	 * @param user
	 * @param ngo
	 * @param month
	 * @param year
	 * @return NGOSoEReport
	 */
	NGOSoEReport findByCollectUserAndNgoAndMonthAndYearAndIsLiveTrue(CollectUser user, NGO ngo, int month, int year);

	Integer getRejectedValue(int userId, int ngoId, int month, int year);

	/**
	 * @param pendingId
	 * @param currentdate
	 * @Description retrieve all the ngo list of the state Who upload their SOE for
	 *              approve/reject with parameter pendingId and currentdate
	 */
	List<Object[]> findSoEStatusOfAllDistrict(Integer pendingId, Date currentdate);

	/**
	 * @param districtId
	 * @param currentdate
	 * @param approveId
	 * @param rejectId
	 * @param autoApprove
	 * @param pendingId
	 * @Description retrieve all the ngo list of corresponding district With their
	 *              history either approved/rejected of SOE with date
	 */
	List<Object[]> findNgoHistoryByDistrictId(int districtId, Timestamp currentdate, Integer approveId,
			Integer rejectId, Integer autoApprove, Integer pendingId);

	/**
	 * @param currentdate
	 * @param approveId
	 * @param rejectId
	 * @param autoApprove
	 * @param pendingId
	 * @Description retrieve all the ngo list of all district With their
	 *              history either approved/rejected of SOE with date
	 */
	List<Object[]> findSoEHistoryOfAllDistrict(Timestamp currentdate, Integer approveId, Integer rejectId,
			Integer autoApprove, Integer pendingId);

	/**
	 * @Description After descending order getting the top NGOSoEReport objects 
	 * @param ngo
	 */
	NGOSoEReport findTop1ByNgoOrderByNgoSoEReportsIdDesc(NGO ngo);

	/**
	 * @param reuploadstatus
	 * @Description retrieve all the ngo details who pending for approval after
	 *              deadline date
	 */
	List<NGOSoEReport> findByReEntryStatusANDIsLiveIsTRUE(int reuploadstatus);

	byte[] getLatestSoeReport(Integer userId, Integer ngoId, int month, int year);

	/**
	 * @Description Getting list of NGOSoEReport by passing the parameters userId,
	 *              ngoId, month and year for the selected user, ngo, month and year
	 * 
	 * @param collectUser
	 * @param ngo
	 * @param month
	 * @param year
	 */
	List<NGOSoEReport> findByCollectUserAndNgoAndMonthAndYear(CollectUser collectUser, NGO ngo, int month, int year);

	/**
	 * @param ngoId
	 * @param approveId
	 * @param autoApprove
	 * @param YEAR
	 * @Description latest approved or auto approved SoE template of corresponding
	 *              ngo for current year from ngosoereport table
	 */
	NGOSoEReport getReportOfLatestFile(Integer ngoId, Integer approveId, Integer autoApprove, int YEAR);

	List<NGOSoEReport> getApprovedOrAutoApprovedData(int ngoId, List<Integer> typeDetails);

	/**
	 * @param areaId
	 * @param month
	 * @param year
	 * @Description updated previous month status details of all ngo of corresponding
	 *         district from ngosoereport table
	 */
	List<NGOSoEReport> getNgoUploadSoEStatusForPreviousMonth(Integer areaId, int month, int year);

	List<NGOSoEReport> findAll();

	List<NGOSoEReport> findByMonthAndYearAndIsLiveTrue(int month, int year);

}
