package org.sdrc.missionmillet.springdatajpa;

/*
 *  @author Subham Ashish(subham@sdrc.co.in)
 */

import java.sql.Timestamp;
import java.util.List;

import org.sdrc.missionmillet.domain.WASSANSoEReport;
import org.sdrc.missionmillet.repository.WassanSoeReportRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

@RepositoryDefinition(domainClass = WASSANSoEReport.class, idClass = Integer.class)
public interface SpringDataWassanSoeReportRepository extends
		WassanSoeReportRepository {

	/**
	 * @Description This query fetches the last updated record present for the selected year
	 * 
	 * @param year
	 */
	@Query("select file from WASSANSoEReport file where file.year=:year and file.createdDate="
			+ "(select max(file.createdDate) from WASSANSoEReport file where file.year=:year)")
	WASSANSoEReport findByMaxCreatedDateAndYear(@Param("year") int year);

	/**
	 * @Description This query fetches the last update record of every month for the same finnacial year
	 * 
	 *  
	 */
	
	@Query("select file from WASSANSoEReport file where file.createdDate IN"
			+ "(select max(file.createdDate) from WASSANSoEReport file GROUP BY file.month,file.year) ORDER BY file.year Desc")
	List<WASSANSoEReport> findByuniqueMonthAndMaxCreatedDate();

	
	/**
	 * @Description This query  fetches the record between start date and end date i.e. for one financial year record
	 * 
	 * @Param startDate
	 * @param endDate
	 */
	@Override
	@Query("select file from WASSANSoEReport file where file.createdDate IN"
			+ "(select max(file.createdDate) from WASSANSoEReport file where  file.createdDate between :startDate AND :endDate GROUP BY file.month,file.year)")
	List<WASSANSoEReport> findDataByFinancialYear(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

	
	/**
	 * @Description This query fetches the last updated record present for the selected month and financial year
	 * 
	 * @param month
	 * @param financialyear
	 */
	@Override
	@Query("select file from WASSANSoEReport file where file.year=:financialyear and file.month=:month and file.createdDate="
			+ "(select max(file.createdDate) from WASSANSoEReport file where file.year=:financialyear and file.month=:month)")
	WASSANSoEReport findByMaxCreatedDateAndMonthAndYear(@Param("month") Integer month,@Param("financialyear") int financialyear);

}
