package org.sdrc.missionmillet.springdatajpa;

import java.util.ArrayList;
import java.util.List;

import org.sdrc.missionmillet.domain.Configuration;
import org.sdrc.missionmillet.repository.ConfigurationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataConfigurationRepository extends ConfigurationRepository,
		JpaRepository<Configuration, Integer> {
	/**
	 * @Description Getting the configuration object by passing the parameters.
	 * 
	 * @param typeDetails
	 */
	@Override
	@Query(value="select con.* from configuration con "
			+ "where con.is_live = true and con.type_detail_id_fk in (:typeDetails)", nativeQuery=true)
	public List<Configuration> getAllIsLiveTypeData(@Param("typeDetails") ArrayList<Integer> typeDetails);

}
