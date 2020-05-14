package org.sdrc.missionmillet.repository;

import java.sql.Timestamp;

import org.sdrc.missionmillet.domain.UserLoginMeta;
import org.springframework.transaction.annotation.Transactional;

/** 
* @author Subrata
*/
public interface UserLoginMetaRepository {
	
	@Transactional
	UserLoginMeta save(UserLoginMeta userLoginMeta);

	void updateStatus(Timestamp loggedOutDateTime, long userLogInMetaId);
	
	void updateStatusForAll(Timestamp loggedOutDateTime);

}
