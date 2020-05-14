package org.sdrc.missionmillet.repository;

/*
 *  @author Subham Ashish(subham@sdrc.co.in)
 */
import java.sql.Timestamp;
import java.util.List;

import org.sdrc.missionmillet.domain.WASSANSoEReport;

public interface WassanSoeReportRepository {

	/**
	 * @Description save wassanSoeReport details in WASSANSoEReport table
	 * 
	 * @param wassanSoeReport
	 * 
	 */
	WASSANSoEReport save(WASSANSoEReport wassanSoeReport);

	WASSANSoEReport findByMaxCreatedDateAndYear(int month);

	List<WASSANSoEReport> findByuniqueMonthAndMaxCreatedDate();

	WASSANSoEReport findByMaxCreatedDateAndMonthAndYear(Integer month,int financialyear);
	
	List<WASSANSoEReport> findDataByFinancialYear(Timestamp startDate, Timestamp endDate);

}
