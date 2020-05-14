package org.sdrc.missionmillet.springdatajpa;

import java.util.List;

import org.sdrc.missionmillet.domain.CollectUser;
import org.sdrc.missionmillet.repository.CollectUserRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface SpringDataCollectUserRepository extends CollectUserRepository,Repository<CollectUser, Integer>{
	
	@Override
	@Query("SELECT usr FROM CollectUser usr WHERE usr.username=:userId AND usr.emailId=:email")
	public CollectUser findByUsernameAndEmailId(@Param("email")String email, @Param("userId")String userId);
	
	@Override
	@Query(value="select ur.* from mst_collect_user usr, mst_collect_user ur where usr.user_id_pk = ur.parent_id "
			+ "and ur.parent_id=:userId and ur.is_live = true order by ur.name", nativeQuery=true)
	public List<CollectUser> getUsersList(@Param("userId")Integer userId);
	
	@Override
	@Query(value="select cu.* from mst_collect_user cu where cu.ngo_id_fk=:ngoId", nativeQuery=true)
	public CollectUser getNgo(@Param("ngoId")Integer ngoId);

}
