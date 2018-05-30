package org.sdrc.missionmillet.springdatajpa;

import java.util.List;

import org.sdrc.missionmillet.domain.User_Program_XForm_Mapping;
import org.sdrc.missionmillet.repository.User_Program_XForm_MappingRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface SpringDataUser_Program_XForm_MappingRepository extends User_Program_XForm_MappingRepository, Repository<User_Program_XForm_Mapping, Integer>{
	
	/**
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
	 */
	@Override
	@Query("SELECT upxm FROM User_Program_XForm_Mapping upxm WHERE upxm.collectUser.username = :username"
			+ " AND upxm.isLive = True AND upxm.collectUser.isLive = True AND upxm.program_XForm_Mapping.isLive = True"
			+ " AND upxm.program_XForm_Mapping.program.isLive = True AND upxm.program_XForm_Mapping.xForm.isLive = True "
			+ "order by upxm.program_XForm_Mapping.xForm.formId")
	List<User_Program_XForm_Mapping> findByUser(@Param("username") String username);
	
}