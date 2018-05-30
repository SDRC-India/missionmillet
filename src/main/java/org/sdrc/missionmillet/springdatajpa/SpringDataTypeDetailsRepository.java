package org.sdrc.missionmillet.springdatajpa;

import org.sdrc.missionmillet.domain.TypeDetails;
import org.sdrc.missionmillet.repository.TypeDetailsRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTypeDetailsRepository extends JpaRepository<TypeDetails, Integer>,
		TypeDetailsRepository {

}
