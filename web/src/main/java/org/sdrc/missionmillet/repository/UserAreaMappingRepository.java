package org.sdrc.missionmillet.repository;

import org.sdrc.missionmillet.domain.CollectUser;
import org.sdrc.missionmillet.domain.UserAreaMapping;

public interface UserAreaMappingRepository {

	/**
	 * @param collectUser
	 * @return current login user details from collectUser table
	 */
	UserAreaMapping findByCollectUser(CollectUser collectUser);

}
