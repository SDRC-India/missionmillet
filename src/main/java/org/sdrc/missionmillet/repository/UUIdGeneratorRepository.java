package org.sdrc.missionmillet.repository;

import org.sdrc.missionmillet.domain.UUIdGenerator;
import org.springframework.transaction.annotation.Transactional;

public interface UUIdGeneratorRepository {

	
	UUIdGenerator getUUIDdetailsByMonthAndYear(int userId,String uuid,Integer month,Integer year);
	
	/**
	 * @param uuidGenerator
	 * @return
	 * @Description save uuid details in uuidgenerated table
	 */
	@Transactional
	UUIdGenerator save(UUIdGenerator uuidGenerator);

	UUIdGenerator findByUuidAndMonthAndYear(String cellValue, Integer month,
			Integer year);

	/**
	 * @param userId
	 * @param sheetuuid
	 * @param timePeriodId
	 * @Description Get biannualtimeperiodId
	 */
	UUIdGenerator getUUIDdetailsForDistrict(Integer userId, String sheetuuid, Integer timePeriodId);

}
