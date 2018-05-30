package org.sdrc.missionmillet.repository;

import java.util.List;

import org.sdrc.missionmillet.domain.NGO;

public interface NGORepository {

	/**
	 * @Description Get all ngo list from NGO table 
	 * 
	 * @return List<NGO>
	 * 
	 */
	List<NGO> findAll();
	

	/**
	 * @Description Getting NGO details by passing ngoId for a NGO level user.
	 * 
	 * @param ngoId
	 */
	NGO findById(Integer ngoId);

/**
 * @Description Get NGO list which are newly added in October Month.
 * @return List<NGO>
 */
	List<NGO> findListOfNewNgos();
}
