package org.sdrc.missionmillet.repository;

import java.util.List;

import org.sdrc.missionmillet.domain.User_Program_XForm_Mapping;

public interface User_Program_XForm_MappingRepository {

	/**
	 * This method will take the user id and give all the forms and programs assigned to the user.  
	 * @param username username of the Collect Android app user
	 * @param password password of the Collect Android app user
	 * @return List<org.sdrc.collect.domain.User_Program_XForm_Mapping>
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
	 * @since version 1.0.0.0
	 */
	
	List<User_Program_XForm_Mapping>  findByUser(String username);
	
}
