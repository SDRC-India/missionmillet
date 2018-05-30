package org.sdrc.missionmillet.springdatajpa;

import java.sql.Timestamp;

import org.sdrc.missionmillet.domain.UserLoginMeta;
import org.sdrc.missionmillet.repository.UserLoginMetaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/** 
* @author Subrata
*/

@RepositoryDefinition(domainClass = UserLoginMeta.class, idClass = Long.class)
public interface SpringDataUserLoginMetaRepository extends UserLoginMetaRepository{

	/**
	 * @Description when a user logout, updating loggedOutDateTime for that user 
	 * 
	 * @param loggedOutDateTime
	 * @param userLogInMetaId
	 */
	@Override
	@Modifying 
	@Transactional
	@Query("UPDATE UserLoginMeta logInMeta SET logInMeta.loggedOutDateTime = :loggedOutDateTime , "
			+ "logInMeta.isLoggedIn =FALSE WHERE logInMeta.userLogInMetaId = :userLogInMetaId ")
	void updateStatus(@Param("loggedOutDateTime")Timestamp loggedOutDateTime, @Param("userLogInMetaId")long userLogInMetaId);
	
	/**
	 * @Description when a user logout, updating loggedOutDateTime for that user 
	 * 
	 * @param loggedOutDateTime  
	 */
	@Override
	@Modifying 
	@Transactional
	@Query("UPDATE UserLoginMeta logInMeta SET logInMeta.loggedOutDateTime = :loggedOutDateTime , "
			+ "logInMeta.isLoggedIn =FALSE ")
	void updateStatusForAll(@Param("loggedOutDateTime")Timestamp loggedOutDateTime);
}
