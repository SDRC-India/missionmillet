package org.sdrc.missionmillet.springdatajpa;

import org.sdrc.missionmillet.domain.WASSANReports;
import org.sdrc.missionmillet.repository.WassanReportsRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = WASSANReports.class, idClass = Integer.class)
public interface SpringDataWassanReportsRepository extends
		WassanReportsRepository {
	
}
