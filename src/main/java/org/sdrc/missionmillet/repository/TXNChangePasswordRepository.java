package org.sdrc.missionmillet.repository;

import org.sdrc.missionmillet.domain.TXNChangePassword;
import org.springframework.transaction.annotation.Transactional;

public interface TXNChangePasswordRepository {

	@Transactional
	void save(TXNChangePassword txnChangePassword);

}
