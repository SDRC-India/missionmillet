package org.sdrc.missionmillet.springdatajpa;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.sdrc.missionmillet.domain.NGOSoEUploadsStatus;
import org.sdrc.missionmillet.repository.NGOSoEUploadsStatusRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataNGOSoEUploadsStatusRepository
		extends NGOSoEUploadsStatusRepository, JpaRepository<NGOSoEUploadsStatus, Integer> {

	/**
	 * @Description Getting SoE status for an NGO user by passing ngoId, rejectcount,
	 *              typeDetailsIds() and currentDate
	 * 
	 * @param ngoId
	 * @param currentDate
	 * @param typeDetailsIds
	 * @param rejectcount
	 */
	@Override
	@Query(value = "select ns from NGOSoEUploadsStatus ns where ns.ngo.id = :ngoId and ns.status.typeDetailId in (:typeDetailsIds) "
			+ "and ns.deadlineDate > :currentDate and (ns.rejectCount < :rejectcount OR ns.rejectCount IS NULL) "
			+ "order by ns.ngoSoEUploadsStatusId,ns.month,ns.year")
	public List<NGOSoEUploadsStatus> getUploadTableDetails(@Param("ngoId") int ngoId,
			@Param("currentDate") Timestamp currentDate, @Param("typeDetailsIds") List<Integer> typeDetailsIds,
			@Param("rejectcount") int rejectcount);

	/**
	 * @Description Getting the NGOSoEUploadsStatus object by passing the parameters
	 * 
	 * @param ngoId
	 * @param month
	 * @param year
	 */
	@Override
	@Query("select nsus from NGOSoEUploadsStatus nsus where nsus.ngo.id = :ngoId and nsus.month=:month and nsus.year=:year")
	public NGOSoEUploadsStatus getTimePeriodByNGOMonthAndYear(@Param("ngoId") int ngoId, @Param("month") int month,
			@Param("year") int year);

	@Query(value = "select a.area_id_pk, a.area_name, ng.ngo_id_pk, ng.name "
			+ "from ngo_soe_upload_status soe, mst_area a, mst_ngo ng "
			+ "where soe.status_id_fk in (:statusId) and soe.created_date >= :startDate "
			+ "and soe.created_date <= :endDate "
			+ "and soe.ngo_id_fk = ng.ngo_id_pk and a.area_id_pk = ng.district_id_fk "
			+ "and (soe.reject_count is Null or soe.reject_count < :maxReject)", nativeQuery = true)
	public List<Object[]> getPendingStatus(@Param("statusId") List<Integer> statusId,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("maxReject") Integer maxReject);

	/**
	 * @Description get all the ngo details who pending for approve after deadline date
	 * @param currentDate
	 */
	@Override
	@Query(value = "select s.* from ngo_soe_upload_status s where deadline_date<:currentDate and status_id_fk=1", nativeQuery = true)
	List<NGOSoEUploadsStatus> getSoEListForAutoApprove(@Param("currentDate") Timestamp currentdate);

	/**
	 * @Description This query retrieve the previous month status of SoE
	 * @param districtId
	 * @param month
	 * @param year
	 */
	@Override
	@Query("select nsus from NGOSoEUploadsStatus nsus JOIN nsus.ngo as n where n.districtId.areaId=:districtId and nsus.month=:month and nsus.year=:year")
	List<NGOSoEUploadsStatus> getNgoUploadSoEStatusForPreviousMonth(@Param("districtId") int districtId,
			@Param("month") int month, @Param("year") int year);

	/**
	 * @Description Getting the previous month SoE template by passing the parameters
	 * 
	 * @param ngoId
	 * @param month
	 */
	@Override
	@Query(value = "select status.ngo_soe_upload_status_id_pk,status.created_date,status.reject_count,status.status_id_fk,"
			+ "status.deadline_date as statusDeadline,nsr.action_taken_date as actionTakenDate,nsr.deadline_date as nsrDeadline "
			+ "from  ngo_soe_upload_status status left outer join (select nsr.* from ngo_soe_report nsr,ngo_soe_upload_status status"
			+ "  where (is_latest=true OR status.reject_count = 1) and status.ngo_id_fk=:ngoId and status.month = :month and status.year = :year) nsr"
			+ " on status.ngo_soe_upload_status_id_pk = nsr.ngo_soe_upload_status_id_fk where status.ngo_id_fk=:ngoId and "
			+ "status.month = :month and status.year = :year", nativeQuery = true)
	public List<Object[]> getDataForPreviousMonth(@Param("ngoId") int ngoId, @Param("month") int month,
			@Param("year") int year);
	
	/**
	 * @Description Retrieve count value of ngo if available in NGOSoEUploadsStatus table by passing ngoId
	 * 
	 * @param ngoId
	 */
	@Override
	@Query(value="select count(*) from ngo_soe_upload_status where ngo_id_fk =:ngoId",nativeQuery = true)
	int findRecord(@Param("ngoId") int ngoId); 
}
