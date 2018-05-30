package org.sdrc.missionmillet.springdatajpa;

import java.sql.Timestamp;
import java.util.List;

import org.sdrc.missionmillet.domain.NGOSoEReport;
import org.sdrc.missionmillet.repository.NGOSoEReportRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataNGOSoEReportRepository extends NGOSoEReportRepository, JpaRepository<NGOSoEReport, Integer> {

	/**
	 * @Description Getting all the SoE reports by passing currentDate, ngoId, reUpload 
	 * and list(approved, autoApproved, rejected, noActionTaken, reUpload) for an NGO.
	 * 
	 * @param list
	 * @param ngoId
	 * @param currentDate
	 * @param reUpload
	 */
	@Override
	@Query(value = "select nsr.ngo_soe_report_id_pk,status.month,status.year,nsr.created_date,status.ngo_soe_upload_status_id_pk,"
			+ "nrf.type_detail_id_fk,status.status_id_fk,nsr.user_id_fk,nsr.remarks,status.reject_count,nsr.action_taken_by_user_id_fk ,"
			+ "nsr.re_entry_status,nsr.action_taken_date from public.ngo_soe_upload_status  status left outer join public.ngo_soe_report nsr "
			+ "on nsr.ngo_soe_upload_status_id_fk = status.ngo_soe_upload_status_id_pk LEFT JOIN ngo_report_file nrf on "
			+ "nsr.ngo_soe_report_id_pk = nrf.ngo_soe_reports_id_fk where status.ngo_id_fk =:ngoId and "
			+ "(nsr.deadline_date < :currentDate or status.deadline_date < :currentDate) and (nsr.re_entry_status "
			+ "in (:list) OR nsr.re_entry_status IS NULL) and (nsr.is_latest = true OR nsr.is_latest IS NULL "
			+ "OR status.reject_count <=2 and nsr.re_entry_status <> :reUpload) order by "
			+ "status.year,status.month,nsr.action_taken_date", nativeQuery = true)
	public List<Object[]> viewSoeDetails(@Param("list") List<Integer> list, @Param("ngoId") int ngoId,
			@Param("currentDate") Timestamp currentDate, @Param("reUpload") int reUpload);

	/**
	 * @Description Getting the latest approved/auto-approved SoE file by passing
	 *              the userId, ngoId, startDate, endDate for an NGO user.
	 * 
	 * @param userId
	 * @param ngoId
	 * @param startDate
	 * @param endDate
	 * @param list(contains
	 *            approved and auto-approved)
	 */
	@Override
	@Query("select nr from NGOSoEReport nr,NGOSoEUploadsStatus status where nr.nGOSoEUploadsStatus.ngoSoEUploadsStatusId = "
			+ "status.ngoSoEUploadsStatusId and nr.collectUser.userId=:userId and nr.ngo.id=:ngoId and nr.createdDate >= "
			+ ":startDate and nr.createdDate <= :endDate and status.status.typeDetailId in (:list) order by nr.month,nr.year")
	public List<NGOSoEReport> getDataFile(@Param("userId") int userId, @Param("ngoId") int ngoId,
			@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate,
			@Param("list") List<Integer> list);

	/**
	 * @author Maninee Mahapatra(maninee@sdrc.co.in)
	 * 
	 * @Description This query used to retrieve all the ngo list of corresponding
	 *              district Who upload their SOE for approve/reject
	 * @param districtId
	 * @param pendingId
	 * @param currentDate
	 */
	@Override
	@Query("select s.month,s.year,s.createdDate,r.id,r.name,td "
			+ "from NGOSoEReport s join s.ngo as r join s.nGOSoEUploadsStatus as st join st.status as td "
			+ " where s.ngo.id=r.id and r.districtId.areaId=:districtId and s.isLive = TRUE "
			+ "and td.typeDetailId =:pendingId and st.deadlineDate<:currentDate order by s.month,s.year,r.name,s.createdDate")
	List<Object[]> findSoEStatusByDistrictId(@Param("districtId") int districtId, @Param("pendingId") Integer pendingId,
			@Param("currentDate") java.util.Date currentDate);

	/**
	 * @Description getting the reject count value for a NGO by passing the
	 * parameters userId, ngoId, month and year.
	 *          
	 * @param userId
	 * @param ngoId
	 * @param month
	 * @param year
	 */
	@Override
	@Query("select nsus.rejectCount from NGOSoEReport nsr,NGOSoEUploadsStatus nsus where nsr.nGOSoEUploadsStatus.ngoSoEUploadsStatusId "
			+ "= nsus.ngoSoEUploadsStatusId and nsr.collectUser.userId=:userId and nsr.ngo.id=:ngoId "
			+ " and nsr.month=:month and nsr.year=:year")
	public Integer getRejectedValue(@Param("userId") int userId, @Param("ngoId") int ngoId, @Param("month") int month,
			@Param("year") int year);

	/**
	 * @author Maninee Mahapatra(maninee@sdrc.co.in)
	 * 
	 * @Description This query used to retrieve all the ngo list of the state Who
	 *              upload their SOE for approve/reject
	 * @param pendingId
	 * @param currentDate
	 */
	@Override
	@Query(value = "select a.area_id_pk,a.area_name,n.ngo_id_pk,n.name,s.month,s.year,s.created_date"
			+ " from ngo_soe_report s left join ngo_soe_upload_status st on s.ngo_soe_upload_status_id_fk=st.ngo_soe_upload_status_id_pk "
			+ " left join mst_type_details td on st.status_id_fk=td.type_detail_id_pk"
			+ " left join mst_ngo n on st.ngo_id_fk=n.ngo_id_pk left join mst_area a on n.district_id_fk=a.area_id_pk"
			+ " where s.ngo_id_fk=n.ngo_id_pk and n.district_id_fk=a.area_id_pk and s.is_live=true"
			+ " and td.type_detail_id_pk =:pendingId and st.deadline_date<:currentDate order by a.area_name,n.name,s.month,s.year", nativeQuery = true)
	List<Object[]> findSoEStatusOfAllDistrict(@Param("pendingId") Integer pendingId,
			@Param("currentDate") java.util.Date currentDate);

	/**
	 * @Description This query used to retrieve all the ngo list of corresponding
	 *              district With their history either approved/rejected or no
	 *              action taken of SOE with date.
	 * @param districtId
	 * @param currentdate
	 * @param approveId
	 * @param rejectId
	 * @param autoApprove
	 * @param pendingId
	 */
	@Override
	@Query(value = "select n.ngo_id_pk,n.name,r.month,r.year,r.created_date,r.re_entry_status,r.action_taken_date,r.ngo_soe_report_id_pk,r.remarks, "
			+ "us.month as x,us.year as y,us.created_date as cd ,us.status_id_fk "
			+ "from ngo_soe_upload_status us LEFT JOIN mst_ngo n on us.ngo_id_fk=n.ngo_id_pk "
			+ "Left join ngo_soe_report r on us.ngo_soe_upload_status_id_pk=r.ngo_soe_upload_status_id_fk "
			+ "WHERE n.district_id_fk =:districtId and r.deadline_date <:currentdate "
			+ "AND (r.re_entry_status =:approveId OR r.re_entry_status =:autoApprove OR r.re_entry_status=:rejectId) "
			+ "OR (us.status_id_fk=:pendingId and r.deadline_date IS NULL AND r.re_entry_status IS  NULL AND us.deadline_date<:currentdate and n.district_id_fk =:districtId)"
			+ "order by n.name,us.year,us.month,r.created_date", nativeQuery = true)
	List<Object[]> findNgoHistoryByDistrictId(@Param("districtId") int districtId,
			@Param("currentdate") Timestamp currentdate, @Param("approveId") Integer approveId,
			@Param("rejectId") Integer rejectId, @Param("autoApprove") Integer autoApprove,
			@Param("pendingId") Integer pendingId);

	/**
	 * @Description This query used to retrieve all the ngo list of all district
	 *              With their history either approved/rejected or no action taken
	 *              of SOE with date.
	 * @param currentdate
	 * @param approveId
	 * @param rejectId
	 * @param autoApprove
	 * @param pendingId
	 */
	@Override
	@Query(value = "select a.area_id_pk,a.area_name,n.ngo_id_pk,n.name,r.month,r.year,r.created_date,r.re_entry_status,r.action_taken_date,r.ngo_soe_report_id_pk,r.remarks, "
			+ "us.month as x,us.year as y,us.created_date as cd ,us.status_id_fk,r.action_taken_by_user_id_fk,cu.user_name "
			+ "from ngo_soe_upload_status us LEFT JOIN mst_ngo n on us.ngo_id_fk=n.ngo_id_pk "
			+ "Left join ngo_soe_report r on us.ngo_soe_upload_status_id_pk=r.ngo_soe_upload_status_id_fk "
			+ "left join mst_area a on n.district_id_fk=a.area_id_pk "
			+ "left join mst_collect_user cu on r.action_taken_by_user_id_fk=cu.user_id_pk "
			+ "WHERE  r.deadline_date < :currentdate "
			+ "AND (r.re_entry_status =:approveId OR r.re_entry_status =:autoApprove OR r.re_entry_status=:rejectId) "
			+ "OR (us.status_id_fk=:pendingId and r.deadline_date IS NULL AND r.re_entry_status IS  NULL AND us.deadline_date<:currentdate) "
			+ "order by a.area_name, n.name,us.year,us.month,r.created_date", nativeQuery = true)
	List<Object[]> findSoEHistoryOfAllDistrict(@Param("currentdate") Timestamp currentdate,
			@Param("approveId") Integer approveId, @Param("rejectId") Integer rejectId,
			@Param("autoApprove") Integer autoApprove, @Param("pendingId") Integer pendingId);

	/**
	 * @author Maninee Mahapatra(maninee@sdrc.co.in)
	 * 
	 * @Description retrieve all the ngo details who pending for approval after
	 *              deadline date
	 * @param reuploadstatus
	 */
	@Override
	@Query(value = "select s.* from ngo_soe_report s where (s.re_entry_status IS NULL or s.re_entry_status=:reuploadstatus) and s.is_live=true", nativeQuery = true)
	List<NGOSoEReport> findByReEntryStatusANDIsLiveIsTRUE(@Param("reuploadstatus") int reuploadstatus);

	/**
	 * @Description Getting the latest SOE file passing the parameters userId,
	 *              ngoId, year and month for NGO level user.
	 * 
	 * @param userId
	 * @param ngoId
	 * @param month
	 * @param year
	 */
	@Override
	@Query("select nrf.reportsFile " + "from NgoReportsFile nrf, NGOSoEReport nsr "
			+ "where nsr.ngoSoEReportsId = nrf.ngoSoeReport.ngoSoEReportsId and nsr.month = :month "
			+ "and nsr.year = :year and nsr.collectUser.userId = :userId and nsr.ngo.id = :ngoId and nsr.isLive=true")
	public byte[] getLatestSoeReport(@Param("userId") Integer userId, @Param("ngoId") Integer ngoId,
			@Param("month") int month, @Param("year") int year);

	/**
	 * @Author Maninee Mahapatra(maninee@sdrc.co.in)
	 * 
	 * @Description latest approved or auto approved SoE template of corresponding
	 *              ngo for current year from ngo_soe_report table
	 * @param ngoId
	 * @param approveId
	 * @param autoApproved
	 * @param year
	 */
	@Override
	@Query(value = "select s.* from ngo_soe_report s where s.ngo_id_fk=:ngoId and s.year=:year and (s.re_entry_status =:approveId or s.re_entry_status =:autoApproved)  "
			+ " order by ngo_soe_report_id_pk desc limit 1", nativeQuery = true)
	NGOSoEReport getReportOfLatestFile(@Param("ngoId") Integer ngoId, @Param("approveId") Integer approveId,
			@Param("autoApproved") Integer autoApproved, @Param("year") int year);

	/**
	 * @Description Getting the last approve/auto-approve SoE file uploaded by the
	 *              NGO user by passing ngoId and list(contains
	 *              approve/auto-approved)
	 * 
	 * @param ngoId
	 * @param typeDetails(contains
	 *            approve/auto-approved)
	 */
	@Query("select nsr from NGOSoEReport nsr,NGOSoEUploadsStatus status where "
			+ "nsr.nGOSoEUploadsStatus.ngoSoEUploadsStatusId = status.ngoSoEUploadsStatusId and nsr.ngo.id = :ngoId "
			+ "and nsr.reEntryStatus.typeDetailId in (:typeDetails) order by nsr.month,nsr.year")
	public List<NGOSoEReport> getApprovedOrAutoApprovedData(@Param("ngoId") int ngoId,
			@Param("typeDetails") List<Integer> typeDetails);

	/**
	 * @Description Retrieve report details of previous month SoE
	 * @param districtId
	 * @param month
	 * @param year
	 */
	@Override
	@Query("select nsr from NGOSoEReport nsr JOIN nsr.ngo as n where n.districtId.areaId=:districtId and nsr.month=:month and nsr.year=:year and nsr.isLatest IS TRUE")
	List<NGOSoEReport> getNgoUploadSoEStatusForPreviousMonth(@Param("districtId") Integer districtId,
			@Param("month") int month, @Param("year") int year);

}
