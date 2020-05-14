package org.sdrc.missionmillet.springdatajpa;

import java.sql.Timestamp;
import java.util.List;

import org.sdrc.missionmillet.domain.NgoReportsFile;
import org.sdrc.missionmillet.repository.NgoReportsFileRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataNgoReportsFileRepository
		extends NgoReportsFileRepository, JpaRepository<NgoReportsFile, Integer> {

	/**
	 * @Description Joining NgoReportsFile and NgoReports table and getting the
	 *              NgoReportsFile by passing reportId, typeId, reportTypeId and
	 *              userId.
	 * 
	 * @param reportId
	 * @param typeId
	 * @param reportTypeId
	 * @param userId
	 */
	@Override
	@Query("select nrf from NgoReportsFile nrf,NgoReports nr "
			+ "where nr.reportsId = nrf.ngoReports.reportsId and nrf.ngoReports.reportsId=:reportId "
			+ "and nrf.status.typeDetailId=:typeId and nr.reportType.typeDetailId =:reportTypeId "
			+ "and nrf.collectUser.userId=:userId and nr.isLive=true")
	NgoReportsFile getReportFile(@Param("reportId") int reportId, @Param("typeId") int typeId,
			@Param("reportTypeId") int reportTypeId, @Param("userId") int userId);

	/**
	 * @Description Getting the SoE file passing the parameters reportId, typeId and
	 *              userId for NGO level user.
	 * 
	 * @param reportId
	 * @param typeId
	 * @param userId
	 */
	@Override
	@Query(value = "select data from NgoReportsFile data where ngoSoeReport.ngoSoEReportsId=:reportId "
			+ "and status.typeDetailId=:typeId and collectUser.userId=:userId")
	NgoReportsFile getCertificateFile(@Param("reportId") int reportId, @Param("typeId") int typeId,
			@Param("userId") int userId);

	@Override
	@Query("select nrf from NgoReportsFile nrf,NgoReports nr "
			+ "where nr.reportsId = nrf.ngoReports.reportsId and nr.ngo.id=:ngoId and nrf.ngoReports.reportsId=:reportId "
			+ "and nrf.status.typeDetailId=:typeId and nr.reportType.typeDetailId =:reportTypeId "
			+ "and nr.isLive=true")
	NgoReportsFile getFileByNgoIdReportTypeIdReportId(@Param("reportId") int reportId, @Param("typeId") int typeId,
			@Param("reportTypeId") int reportTypeId, @Param("ngoId") int ngoId);


	/**
	 * @Description This query used to retrieve all the Ngo list with report history
	 *              of corresponding district by passing parameter areaId and
	 *              currentdate.
	 * @param areaId
	 * @param currentdate
	 */
	@Override
	@Query(" SELECT file.reportsFileId,file.ngoReports,file.status FROM NgoReportsFile file , NGOSoEReport soeFile"
			+ " WHERE file.ngoReports.ngo.districtId.areaId = :areaId AND file.isLive IS TRUE"
			+ "  AND file.ngoReports.isLive IS TRUE " + "  AND soeFile.ngo.id = file.ngoReports.ngo.id "
			+ "  AND soeFile.year = file.ngoReports.year " + "  AND soeFile.month=file.ngoReports.month "
			+ "  AND soeFile.deadlineDate < :currentdate " + "  ORDER BY file.ngoReports.reportsId ASC")
	List<Object[]> findNgoReportByDistrictId(@Param("areaId") Integer areaId,
			@Param("currentdate") Timestamp currentdate);

	/**
	 * @Description get latest file from ngo_report_file table
	 * 
	 * @param soereportId
	 */
	@Override
	@Query(value = "select s.* from ngo_report_file s where s.ngo_soe_reports_id_fk=:soereportId", nativeQuery = true)
	NgoReportsFile getLatestReportFileBySoEReportId(@Param("soereportId") Integer soereportId);

}
