package org.sdrc.missionmillet.repository;

import java.util.List;

import org.sdrc.missionmillet.domain.CollectUser;
import org.sdrc.missionmillet.domain.NGO;
import org.springframework.transaction.annotation.Transactional;

public interface CollectUserRepository {

	CollectUser findByUsernameAndIsLiveTrue(String username);

	CollectUser findByUsernameAndPassword(String username, String password);

	List<CollectUser> findAll();

	@Transactional
	CollectUser save(CollectUser collectUser);

	CollectUser findByUsernameAndEmailId(String email, String userId);

	List<CollectUser> getUsersList(Integer userId);

	CollectUser findByUserId(Integer userId);

	List<CollectUser> findByNgoIn(List<NGO> ngoList);

}
