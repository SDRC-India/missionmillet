package org.sdrc.missionmillet.springdatajpa;

import org.sdrc.missionmillet.domain.UUIdGenerator;
import org.sdrc.missionmillet.repository.UUIdGeneratorRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataUUIdGeneratorRepository extends UUIdGeneratorRepository,
		JpaRepository<UUIdGenerator, Integer> {

	
	/**
	 * @Description getting UUIdGenerator objects by passing the parameters 
	 * for that month and year
	 * 
	 * @param userId
	 * @param uuid
	 * @param month
	 * @param year
	 */
	@Override
	@Query(value="select ug from UUIdGenerator ug where ug.collectUser.userId=:userId and ug.uuid=:uuid "
			+ "and ug.month= :month and ug.year= :year")
	UUIdGenerator getUUIDdetailsByMonthAndYear(@Param("userId") int userId,@Param("uuid") String uuid,@Param("month") Integer month,
			@Param("year") Integer year);
	
	/**
	 * @Description Get biannualtimeperiodId by passing parameter userId,sheetuuid and timePeriodId
	 * @param userId
	 * @param sheetuuid
	 * @param timePeriodId
	 */
	@Override
	@Query("select ug from UUIdGenerator ug where ug.collectUser.userId=:userId and ug.uuid=:uuid "
			+ "and ug.timePeriod.timePeriodId= :timePeriodId")
	public UUIdGenerator getUUIDdetailsForDistrict(@Param("userId") Integer userId, @Param("uuid")String sheetuuid,
					@Param("timePeriodId")Integer timePeriodId);
}
