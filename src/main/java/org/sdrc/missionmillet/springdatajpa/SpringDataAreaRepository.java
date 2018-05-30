package org.sdrc.missionmillet.springdatajpa;

import java.util.List;

import org.sdrc.missionmillet.domain.Area;
import org.sdrc.missionmillet.repository.AreaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface SpringDataAreaRepository extends AreaRepository, Repository<Area, Integer> {

	/**
	 * @Description Get district name of ngo by providing districtId
	 * @param districtId
	 */
	@Override
    @Query(value="select area_name from mst_area where area_id_pk=:districtId",nativeQuery=true)
    String getDistrictNameByNgoId(@Param("districtId")Area districtId);
	
	/**
	 * @Description Get area name by using blockId
	 * @param blockId
	 */
	@Override
	@Query(value="select area_name from mst_area where area_id_pk=:blockId",nativeQuery=true)
    String getBlockNameByNgoId(@Param("blockId")Area blockId);
			
	/**
	 * Getting all areaCode and areaName 
	 */
	@Override
	@Query(value="select area_code, area_name from mst_area", nativeQuery=true)
	public List<Object[]> getArea();
	
	/**
	 * Getting all farmer name from mst_farmer table 
	 */
	@Override
	@Query(value="select fr.* from mst_farmer fr",nativeQuery=true)
	public List<Object[]> getAllFarmers();
	
	/**
	 * Getting all seed center name from mst_seed_center_name table 
	 */
	@Override
	@Query(value="select scn.* from mst_seed_center_name scn",nativeQuery=true)
	public List<Object[]> getAllSeedCenterName();
	
	/**
	 * Getting all entrepreneur name from mst_entrepreneur_name table 
	 */
	@Override
	@Query(value="select en.* from mst_entrepreneur_name en",nativeQuery=true)
	public List<Object[]> getAllEntrepreneurName();
	
	/**
	 * Getting all custom hiring center name from mst_custom_hiring_center_name table 
	 */
	@Override
	@Query(value="select chcn.* from mst_custom_hiring_center_name chcn",nativeQuery=true)
	public List<Object[]> getAllcustomHiringCenterName();
}