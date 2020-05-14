package org.sdrc.missionmillet.repository;

import java.sql.Timestamp;
import java.util.List;

import org.sdrc.missionmillet.domain.TimePeriod;
import org.springframework.transaction.annotation.Transactional;

public interface TimePeriodRepository {

	List<TimePeriod> findAll();

	/**
	 * @param currentDate
	 * @param yearlyPeriodicity
	 * @param financialYear
	 * @return yearly timeperiod details of current year from TimePeriod table
	 */
	TimePeriod getTimePeriodData(Timestamp currentDate,String yearlyPeriodicity, String financialYear);

	/**
	 * @Description Saving the TimePeriod object in TimePeriod table.
	 * 
	 * @param timePeriod
	 */
	@Transactional
	void save(TimePeriod timePeriod);

	/**
	 * @Description Getting TimePeriod object by passing timeperiodId
	 * 
	 * @param timeperiodId
	 * @return
	 */
	TimePeriod findByTimePeriodId(int timeperiodId);

	/**
	 * @param timeperiod
	 * @param year
	 * @Description get time periodId for specific year and timeperiod
	 */
	TimePeriod getTimePeriod(String timeperiod, int year);

	/**
	 * @Description Getting list of TimePeriod by passing the periodicity as the parameter.
	 * 
	 * @param periodicity
	 * @return List<TimePeriod>
	 */
	List<TimePeriod> findByPeriodicity(String periodicity);

	List<TimePeriod> getAllTimePeriods(Timestamp date, List<String> list);


	/**
	 * @param periodicity
	 * @param financeyear
	 * @param currentdate
	 * @return int timeperiodId when job call in every 12 months to insert blank
	 *         SoE template for District user
	 */
	int getTimePeriodId(String periodicity, String financeyear,Timestamp currentdate);

	/**
	 * @param periodicitysix
	 * @param currentdate
	 * @return get BiAnnual time period details of current time from timeperiod table 
	 */
	TimePeriod getTimePeriodofSixPeriodicity(String periodicitysix,Timestamp currentdate);

	List<TimePeriod> getAllYearlyDataUptoCurrentFinancialYear(String yearlyPeriodicity, Integer timePeriodId);

	List<TimePeriod> findByPeriodicityOrderByFinancialYearDesc(String message);

	TimePeriod findByFinancialYearAndPeriodicity(String financialYear,String message);

}
