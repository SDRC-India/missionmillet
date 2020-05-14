package org.sdrc.missionmillet.springdatajpa;

import java.sql.Timestamp;
import java.util.List;

import org.sdrc.missionmillet.domain.SoETemplate;
import org.sdrc.missionmillet.repository.SoETemplateRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataSoETemplateRepository extends SoETemplateRepository, Repository<SoETemplate, Integer> {

	/**
	 * @author Maninee Mahapatra(maninee@sdrc.co.in)
	 * 
	 * @Description This query is used to retrieve all the ngo list present in
	 *              district
	 * @param districtId
	 * @param periodicity
	 * @param financialYear
	 * @param currentdate
	 */
	@Query(value = "select r.ngo_id_pk,r.name,s.status,tp.time_period_duration,tp.year,tp.timeperiod_id_pk as x from soe_template s "
			+ "left join mst_ngo as r on s.ngo_id_fk=r.ngo_id_pk"
			+ " left join time_period as tp  on s.timeperiod_id_fk=tp.timeperiod_id_pk"
			+ "  where r.ngo_id_pk=s.ngo_id_fk and r.district_id_fk=:districtId and tp.timeperiod_id_pk="
			+ "(SELECT timeperiod_id_pk from time_period where :currentdate BETWEEN start_date\\:\\:date AND end_date\\:\\:date "
			+ "and periodicity=:periodicity and financial_year =:financialYear)", nativeQuery = true)
	List<Object[]> findByDistrictIdFk(@Param("districtId") Integer districtId, @Param("periodicity") String periodicity,
			@Param("financialYear") String financialYear, @Param("currentdate") Timestamp currentdate);

	/**
	 * @Description This query gives blank soe template file to district user for
	 *              corresponding ngo by passing the parameter
	 *              ngoId,currentDate,year,periodicity
	 * @param ngoId
	 * @param currentDate
	 * @param year
	 * @param periodicity
	 */
	@Override
	@Query("SELECT s FROM SoETemplate s join s.timePeriod time WHERE s.ngo.id=:ngoId"
			+ " AND time.periodicity =:periodicity AND time.startDate <=:currentDate"
			+ " AND time.endDate >=:currentDate AND time.year=:year")
	SoETemplate findByNgoIdAndTimePeriod(@Param("ngoId") Integer ngoId,
			@Param("currentDate") java.util.Date currentDate, @Param("year") int year,
			@Param("periodicity") String periodicity);

	/**
	 * @Description This query retrieve uuid of specific ngo of its corresponding
	 *              district by passing parameter ngoId,timePeriodId
	 * @param ngoId
	 * @param timePeriodId
	 */
	@Override
	@Query("select s from SoETemplate s where s.ngo.id=:ngoId and s.timePeriod.timePeriodId=:timePeriodId")
	SoETemplate getUuidByNgoId(@Param("ngoId") Integer ngoId, @Param("timePeriodId") Integer timePeriodId);

	/**
	 * @Description This query used to reset the SOE template status to true after
	 *              by passing parameter ngoId
	 * @param ngoId
	 */
	@Override
	@Transactional
	@Modifying
	@Query("update SoETemplate s set s.status = true where s.ngo.id=:ngoId")
	Integer setSoETemplateStatus(@Param("ngoId") Integer ngoId);

	/**
	 * @Description set status false in every October where status is true of all
	 *              ngo
	 */
	@Override
	@Transactional
	@Modifying
	@Query("update SoETemplate s set s.status = false where s.status = true")
	void setSoETemplateStatusFalse();


	/**
	 * @Description Getting list of NGO by passing the currentDate(whose isLive is
	 *              true)
	 * 
	 * @param currentDate
	 */
	@Override
	@Query("select stemp.ngo.id from SoETransaction st,SoETemplate stemp "
			+ "where st.soeTemplate.templateId = stemp.templateId and st.timePeriod.timePeriodId =:currentDate and st.isLive=true")
	public List<Integer> getNgoDetailsFromSoeTemplate(@Param("currentDate") int currentDate);
}
