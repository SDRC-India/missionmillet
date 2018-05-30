package org.sdrc.missionmillet.springdatajpa;

import org.sdrc.missionmillet.domain.SoETransaction;
import org.sdrc.missionmillet.repository.SoETransactionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataSoETransactionRepository extends SoETransactionRepository,
		JpaRepository<SoETransaction, Integer> {

	/**
	 * @Description Getting the latest SoE file by passing ngoId and timeperiodId for NGO level user.
	 * 
	 *  @param timeperiodId
	 *  @param ngoId
	 */
	@Override
	@Query(value="select st.* from soe_transaction st,soe_template stemp "
			+ "where stemp.template_id_pk = st.soetemplate_id_fk and stemp.ngo_id_fk = :ngoId "
			+ "and st.timeperiod_id_fk=:timeperiodId and st.is_live=true order by st.soe_transaction_id_pk desc limit 1",nativeQuery=true)
	public SoETransaction getLatestTemplate(@Param("timeperiodId") int timeperiodId,@Param("ngoId") int ngoId);
	
	@Override
	@Transactional
	@Modifying
	@Query("update SoETransaction s set s.isLive = false where s.soeTemplate.templateId =:templateId")
	Integer setSoETransactionStatus(@Param("templateId")Integer templateId);
	
	/**
	 * @Description If month is October getting the updated budget SoE file for the user 
	 * by passing ngoId and timeperiodId(half-yearly).
	 * 
	 * @param ngoId
	 * @param timeperiodId
	 */
	@Override
	@Query(value="select st.* from soe_template t,soe_transaction st "
			+ "where t.template_id_pk = st.soetemplate_id_fk "
			+ "and t.ngo_id_fk = :ngoId and st.is_live=true "
			+ "and st.bi_annual_tp = :timeperiodId", nativeQuery=true)
	public SoETransaction getUpdatedFile(@Param("ngoId")Integer ngoId, @Param("timeperiodId")Integer timePeriodId);
	
	/**
	 * @Description Get blank template for one year
	 * @param ngoId
	 * @param timePeriodId
	 */
	@Override
	@Query(value="select st.* from soe_template t left join soe_transaction st "
			+ "on  t.template_id_pk= st.soetemplate_id_fk where t.ngo_id_fk = :ngoId "
			+ "and t.timeperiod_id_fk=:timeperiodId and st.is_live=true", nativeQuery=true)
	public SoETransaction getYearlyTemplate(@Param("ngoId")Integer ngoId, @Param("timeperiodId")Integer timePeriodId);
	
	/**
	 * @Description Joining SoETemplate and SoETransaction table and getting the SoETransaction object by 
	 * passing the parameters for that time period
	 * 
	 * @param ngoId
	 * @param uuid
	 * @param timeperiodId
	 * @param biAnnualTimeperiodId(half-yearly time period)
	 */
	@Override
	@Query(value = "select stran from  SoETemplate st,SoETransaction stran where "
			+ "st.templateId = stran.soeTemplate.templateId and st.ngo.id=:ngoId and "
			+ "st.uuid =:uuid and stran.timePeriod.timePeriodId =:timeperiodId "
			+ "and stran.biAnnualTp =:biAnnualTimeperiodId and stran.isLive=true")
	public SoETransaction getSoETemplateByMonthAndYear(@Param("ngoId") int ngoId, @Param("uuid") String uuid,
			@Param("timeperiodId") int timeperiodId, @Param("biAnnualTimeperiodId") int biAnnualTimeperiodId);

}
