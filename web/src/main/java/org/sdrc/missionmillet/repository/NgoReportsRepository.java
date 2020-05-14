package org.sdrc.missionmillet.repository;

import java.util.List;

import org.sdrc.missionmillet.domain.CollectUser;
import org.sdrc.missionmillet.domain.NgoReports;
import org.springframework.transaction.annotation.Transactional;

public interface NgoReportsRepository {
	

	@Transactional
	NgoReports save(NgoReports ngoReports);
	
	NgoReports findByReportsId(Integer reportId);

	/**
	 * @Description Getting list of NgoReports passing collectUser as the parameter
	 * @param collectUser
	 */
	List<NgoReports> findByCollectUserAndIsLiveTrue(CollectUser collectUser);

	List<Object[]> findByIsLiveTrueOrderByDistNameNgoNameYearAndMinCreatedDate();
	

}
