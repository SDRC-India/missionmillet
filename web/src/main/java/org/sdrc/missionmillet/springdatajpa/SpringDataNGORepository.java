package org.sdrc.missionmillet.springdatajpa;

import java.util.List;

import org.sdrc.missionmillet.domain.NGO;
import org.sdrc.missionmillet.repository.NGORepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface SpringDataNGORepository extends NGORepository, Repository<NGO, Integer> {
	
	/**
	 * @Description This query retrieve all the newly added NgoList which are added in October month
	 */
	@Override
	@Query(value = "SELECT ngo.* FROM mst_ngo ngo WHERE ngo_id_pk NOT IN(SELECT ngo_id_fk FROM soe_template)", nativeQuery = true)
	List<NGO> findListOfNewNgos();
}
