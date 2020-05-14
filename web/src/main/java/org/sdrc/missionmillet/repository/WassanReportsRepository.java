package org.sdrc.missionmillet.repository;

/*
 *  @author Subham Ashish(subham@sdrc.co.in)
 */
import java.util.List;

import org.sdrc.missionmillet.domain.WASSANReports;

public interface WassanReportsRepository {

	/**
	 * @Description save wassanReports details in WASSANReports table
	 * @param wassanReports
	 * 
	 */
	WASSANReports save(WASSANReports wassanReports);
	
	/**
	 * @Description fetching a specific report details by passing a reportId as a parameter
	 * @param wassanReportId
	 * 
	 */
	WASSANReports findByReportsId(Integer wassanReportId);

	/**
	 * @Description It fetches all the record present in WASSANReports whose isLive value is true 
	 * 
	 */
	List<WASSANReports> findByIsLiveTrueOrderByYearDesc();

	

}
