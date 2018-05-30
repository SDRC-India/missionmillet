package org.sdrc.missionmillet.springdatajpa;

import java.sql.Timestamp;
import java.util.List;

import org.sdrc.missionmillet.domain.TimePeriod;
import org.sdrc.missionmillet.repository.TimePeriodRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface SpringDataTimePeriodRepository extends Repository<TimePeriod, Integer>, TimePeriodRepository {

	/**
	 * @Description getting TimePeriod object by passing the parameters.
	 * 
	 * @param currentDate
	 * @param yearlyPeriodicity
	 * @param financialYear
	 */
	@Override
	@Query(value = "select tp.* from time_period tp where tp.periodicity=:yearlyPeriodicity "
			+ "and :currentDate >=tp.start_date\\:\\:date and :currentDate <=tp.end_date\\:\\:date "
			+ "and tp.financial_year=:financialYear", nativeQuery = true)
	TimePeriod getTimePeriodData(@Param("currentDate") Timestamp currentDate,
			@Param("yearlyPeriodicity") String yearlyPeriodicity, @Param("financialYear") String financialYear);

	/**
	 * @Description get time periodId for specific year and timeperiod
	 * @param timeperiod
	 * @param year
	 */
	@Override
	@Query("select tp from TimePeriod tp where tp.timePeriod=:timeperiod and tp.year=:year and tp.periodicity='1'")
	TimePeriod getTimePeriod(@Param("timeperiod") String timeperiod, @Param("year") int year);

	/**
	 * @Description Getting all the timeperiods by passing the currentdate and
	 *              periodicity.
	 * 
	 * @param currentDate
	 * @param list
	 */
	@Override
	@Query(value = "select tp.* from time_period tp where tp.periodicity in (:list) "
			+ "and tp.end_date\\:\\:date < cast(:currentDate as date) order by tp.timeperiod_id_pk", nativeQuery = true)
	List<TimePeriod> getAllTimePeriods(@Param("currentDate") Timestamp currentDate, @Param("list") List<String> list);

	/**
	 * @Description This query used to retrieve the timeperiod Id of SOE when the
	 *              job call for upload the blank template for the district user in
	 *              every 12 month by passing the parameter
	 *              periodicity,financialYear and currentdate
	 * @param periodicity
	 * @param financialYear
	 * @param currentdate
	 */
	@Override
	@Query(value = "select timeperiod_id_pk from time_period where :currentdate\\:\\:timestamp BETWEEN start_date "
			+ "AND end_date and periodicity=:periodicity and financial_year =:financialYear ", nativeQuery = true)
	public int getTimePeriodId(@Param("periodicity") String periodicity, @Param("financialYear") String financialYear,
			@Param("currentdate") Timestamp currentdate);

	/**
	 * @Description getting TimePeriod object by passing periodicity and current
	 *              date.
	 * 
	 * @param periodicity
	 * @param currentdate
	 */
	@Override
	@Query(value = "select tp.* from time_period tp where tp.periodicity=:periodicitysix and cast(:currentdate as date)\\:\\:date "
			+ "BETWEEN start_date\\:\\:date AND end_date\\:\\:date", nativeQuery = true)
	TimePeriod getTimePeriodofSixPeriodicity(@Param("periodicitysix") String periodicitysix,
			@Param("currentdate") Timestamp currentdate);

	/**
	 * @Description In descending order we are fetching all the time periods by
	 *              passing yearlyPeriodicity and timePeriodId.
	 * 
	 * @param timePeriodId
	 * @param yearlyPeriodicity
	 */
	@Override
	@Query("select tp from TimePeriod tp where tp.periodicity = :yearlyPeriodicity and tp.timePeriodId <= :timePeriodId order by tp.timePeriodId desc ")
	public List<TimePeriod> getAllYearlyDataUptoCurrentFinancialYear(
			@Param("yearlyPeriodicity") String yearlyPeriodicity, @Param("timePeriodId") Integer timePeriodId);
}
