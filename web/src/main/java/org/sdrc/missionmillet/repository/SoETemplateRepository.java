package org.sdrc.missionmillet.repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.sdrc.missionmillet.domain.SoETemplate;
import org.springframework.transaction.annotation.Transactional;

public interface SoETemplateRepository {

	/**
	 * @param soelist
	 * @return
	 * @Description save all the Ngo and their corresponding SoE template in SoETemplate Table
	 */
	@Transactional
	Iterable<SoETemplate> save(Iterable<SoETemplate> soelist);
	
	
	/**
	 * @param districtId
	 * @param periodicity
	 * @param financialYear
	 * @param currentdate
	 * @return list of ngo in corresponding login district
	 */
	List<Object[]> findByDistrictIdFk(Integer districtId, String periodicity, String financialYear, Timestamp currentdate);

	/**
	 * @param ngoId
	 * @param timePeriodId
	 * @Description get uuid by timeperiodid and ngoid
	 */
	SoETemplate getUuidByNgoId(Integer ngoId, Integer timePeriodId);

	/**
	 * @param ngoId
	 * @Description set status true after upload the SoE
	 */
	Integer setSoETemplateStatus(Integer ngoId);

	
	/**
	 * @param ngoId
	 * @param currentDate
	 * @param year
	 * @param periodicity
	 * @return get blank SoE template of corresponding ngo for specific time period from soetemplate table
	 */
	SoETemplate findByNgoIdAndTimePeriod(Integer ngoId, Date currentDate, int year, String periodicity);
	
	
	List<Integer> getNgoDetailsFromSoeTemplate(int timeperiodId);

	/**
	 * @Description set status false in every October 
	 */
	void setSoETemplateStatusFalse();

}
