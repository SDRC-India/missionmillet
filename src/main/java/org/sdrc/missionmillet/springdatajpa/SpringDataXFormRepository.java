package org.sdrc.missionmillet.springdatajpa;

import org.sdrc.missionmillet.domain.XForm;
import org.sdrc.missionmillet.repository.XFormRepository;
import org.springframework.data.repository.Repository;

public interface SpringDataXFormRepository extends XFormRepository, Repository<XForm, String>{
	
}