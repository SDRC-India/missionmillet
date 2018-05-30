package org.sdrc.missionmillet.repository;

import java.util.List;

import org.sdrc.missionmillet.domain.RawData;
import org.springframework.transaction.annotation.Transactional;

public interface RawDataRepository {

	List<RawData> getMonthlyData(int monthId);

	List<Object[]> getRawData(int monthId, String period);

	double getAllNgoMonthlyData(int monthId);

	double getAllNgoData(int monthId, String period);

	/**
	 * @param allAutoApprovedData
	 * @Description save details in rawdata table
	 */
	@Transactional
	Iterable<RawData> save(Iterable<RawData> allAutoApprovedData);

}
