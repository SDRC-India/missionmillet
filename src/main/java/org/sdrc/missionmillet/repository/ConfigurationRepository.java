package org.sdrc.missionmillet.repository;

import java.util.ArrayList;
import java.util.List;

import org.sdrc.missionmillet.domain.Configuration;
import org.springframework.transaction.annotation.Transactional;

public interface ConfigurationRepository {

	/**
	 * @Description Saving the configuration object
	 * @param configuration
	 */
	@Transactional
	void save(Configuration configuration);

	Configuration findByTypeDetailsTypeDetailIdAndIsLiveTrue(int typeDetailsId);


	/**
	 * @Description Getting the configuration object by passing the typeDetailsId
	 * 
	 * @param typeDetailsId
	 * @return
	 */
	List<Configuration> getAllIsLiveTypeData(ArrayList<Integer> list);

}
