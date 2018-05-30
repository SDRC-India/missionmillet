package org.sdrc.missionmillet.springdatajpa;

import org.sdrc.missionmillet.domain.UserAreaMapping;
import org.sdrc.missionmillet.repository.UserAreaMappingRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserAreaMappingRepository extends UserAreaMappingRepository,JpaRepository<UserAreaMapping, Integer>  {

}
