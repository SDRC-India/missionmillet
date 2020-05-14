package org.sdrc.missionmillet.repository;

import java.sql.Timestamp;
import java.util.List;

import org.sdrc.missionmillet.domain.NGOSoEReport;
import org.sdrc.missionmillet.domain.NgoReports;
import org.sdrc.missionmillet.domain.NgoReportsFile;
import org.springframework.transaction.annotation.Transactional;

public interface NgoReportsFileRepository {

	/**
	 * @Description Saving more then one NgoReportsFile in Database for a NGO user.
	 * 
	 * @param ngoReportsFile
	 * @return
	 */
	@Transactional
	Iterable<NgoReportsFile> save(Iterable<NgoReportsFile> ngoReportsFile);
	
	/**
	 * @Description 
	 * @param ngoReportsFile
	 */
	@Transactional
	NgoReportsFile save(NgoReportsFile ngoReportsFile);
	
	NgoReportsFile getReportFile(int reportId, int typeId, int reportTypeId, int userId);
	
	NgoReportsFile getCertificateFile(int reportId, int typeId, int userId);

	/**
	 * @param ngoSoEReport
	 * @Description find SoE report file
	 */
	NgoReportsFile findByNgoSoeReport(NGOSoEReport ngoSoEReport);
	
	List<NgoReportsFile> findByNgoReports(NgoReports ngoReport);

	NgoReportsFile getFileByNgoIdReportTypeIdReportId(int reportId, int typeId,	int reportTypeId, int ngoId);

	
	/**
	 * @Description Getting all the records from NgoReportsFile
	 * @return List<NgoReportsFile>
	 */
	List<NgoReportsFile> findAll();

	List<NgoReportsFile> findByIsLiveTrue();

	/**
	 * @param areaId
	 * @param currentdate
	 * @Description find Reports of all ngos of specific district
	 */
	List<Object[]> findNgoReportByDistrictId(Integer areaId, Timestamp currentdate);

	/**
	 * @param reportsfileid
	 * @Description get reportfile by reportfileId
	 */
	NgoReportsFile getByReportsFileId(Integer reportsfileid);

	/**
	 * @param soereportId
	 * @Description get latest file from ngo reportfile table
	 */
	NgoReportsFile getLatestReportFileBySoEReportId(Integer soereportId);

}
