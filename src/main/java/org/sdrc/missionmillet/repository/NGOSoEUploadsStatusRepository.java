package org.sdrc.missionmillet.repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.sdrc.missionmillet.domain.NGO;
import org.sdrc.missionmillet.domain.NGOSoEUploadsStatus;
import org.springframework.transaction.annotation.Transactional;

public interface NGOSoEUploadsStatusRepository {

	@Transactional
	Iterable<NGOSoEUploadsStatus> save(Iterable<NGOSoEUploadsStatus> ngoSoEUploadsStatus);
	
	List<NGOSoEUploadsStatus> getUploadTableDetails(int ngoId,Timestamp currentDate,List<Integer> typeDetailsIds, int rejectcount);

	/**
	 * @Description find status details of specific ngo for specific month and year 
	 * @param ngo
	 * @param month
	 * @param year
	 * 
	 */
	NGOSoEUploadsStatus findByNgoAndMonthAndYear(NGO ngo, Integer month, Integer year);
	
	NGOSoEUploadsStatus getTimePeriodByNGOMonthAndYear(int ngoId,int month,int year);
	
	/**
	 * @param uploadsStatus
	 * @Description update the details of ngo after upload SoE budget
	 */
	@Transactional
	NGOSoEUploadsStatus save(NGOSoEUploadsStatus uploadsStatus);
	
	NGOSoEUploadsStatus findByNgoSoEUploadsStatusId(int ngoSoEUploadsStatusId);

	List<Object[]> getPendingStatus(List<Integer> statusId, Date startDate, Date endDate, Integer maxReject);

	/**
	 * @param currentdate
	 * @Description get all the ngo details who pending for approve after deadline date
	 */
	List<NGOSoEUploadsStatus> getSoEListForAutoApprove(Timestamp currentdate);
	
	List<Object[]> getDataForPreviousMonth(int ngoId,int month,int year);

	
	/**
	 * @param districtId
	 * @param month
	 * @param year
	 * @return updated previous month status of all ngo of corresponding district from ngosoeupload status table. 
	 */
	List<NGOSoEUploadsStatus> getNgoUploadSoEStatusForPreviousMonth(int districtId,int month, int year);

	/**
	 * @Description Getting all the records from NGOSoEUploadsStatus table
	 */
	List<NGOSoEUploadsStatus> findAll();
/**
 * @Description Return count value of ngo if available in NGOSoEUploadsStatus table
 * @param ngoId
 * @return int
 */
	int findRecord(int ngoId);

}
