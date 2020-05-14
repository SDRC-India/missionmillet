package org.sdrc.missionmillet.repository;

/*
 *  @author Subham Ashish(subham@sdrc.co.in)
 */
import java.util.List;

import org.sdrc.missionmillet.domain.WASSANReports;
import org.sdrc.missionmillet.domain.WASSANReportsFile;

public interface WassanReportsFileRepository {

	/**
	 * @Description save the wassanReportsFile details in WASSANReportsFile table
	 * @param wassanReportsFile
	 */
	WASSANReportsFile save(WASSANReportsFile wassanReportsFile);

	/**
	 * @Description it fetches the specific record matches with passed parameter
	 * 
	 * @param wassanReports
	 */
	List<WASSANReportsFile> findByWassanReports(WASSANReports wassanReports);

	/**
	 * @Description it save the list of wASSANReportsFile in WASSANReportsFile table
	 * @param wassanReportFileList
	 */
	Iterable<WASSANReportsFile> save(Iterable<WASSANReportsFile> wassanReportFileList);
	

	WASSANReportsFile getFile(Integer reportId, Integer typeId);

	WASSANReportsFile findFileByWassanSoeReportId(Integer id, Integer userid);

	WASSANReportsFile findBywassanSoeId(Integer id);

	List<WASSANReportsFile> findByReportId(List<Integer> reportIdList);

}
