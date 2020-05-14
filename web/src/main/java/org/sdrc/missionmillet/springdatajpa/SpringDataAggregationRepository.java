package org.sdrc.missionmillet.springdatajpa;

import java.util.List;

import org.sdrc.missionmillet.domain.AggregatedData;
import org.sdrc.missionmillet.domain.TimePeriod;
import org.sdrc.missionmillet.repository.AggregationRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface SpringDataAggregationRepository extends AggregationRepository, Repository<AggregatedData, Integer> {
	/**
	 * @Description Getting timeperiods for which data already aggregated. 
	 */
	@Override
	@Query(value="select distinct ag.timeperiod_id_fk from aggregated_data ag",nativeQuery=true)
	public List<TimePeriod> getAllTimePeriodsAggregated();
}
