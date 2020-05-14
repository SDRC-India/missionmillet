package org.sdrc.missionmillet.repository;

import java.util.List;

import org.sdrc.missionmillet.domain.AggregatedData;
import org.sdrc.missionmillet.domain.TimePeriod;
import org.springframework.transaction.annotation.Transactional;

public interface AggregationRepository {

	@Transactional
	Iterable<AggregatedData> save(Iterable<AggregatedData> listAggregatedDatas);

	List<TimePeriod> getAllTimePeriodsAggregated();

}
