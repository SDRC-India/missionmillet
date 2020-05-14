package org.sdrc.missionmillet.repository;

import java.util.List;

import org.sdrc.missionmillet.domain.Area;

/**
 * @author SDRC_DEV
 *
 */
public interface AreaRepository {
	/**
	 * @param districtId
	 * @return get district name by provide districtId from area table
	 */
	String getDistrictNameByNgoId(Area districtId);

	/**
	 * @param blockId
	 * @return get block name by provide blockId from area table
	 */
	String getBlockNameByNgoId(Area blockId);

	List<Object[]> getArea();

	List<Object[]> getAllFarmers();

	List<Object[]> getAllSeedCenterName();

	List<Object[]> getAllEntrepreneurName();

	List<Object[]> getAllcustomHiringCenterName();

	
}
