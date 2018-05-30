package org.sdrc.missionmillet.repository;

import org.sdrc.missionmillet.domain.SoETransaction;

public interface SoETransactionRepository {
	
	/**
	 * @param timeperiodId
	 * @param ngoId
	 * @Description get latest template from soetransaction table by using ngoid and timeperiodIds
	 */
	SoETransaction getLatestTemplate(int timeperiodId,int ngoId);

     void save(SoETransaction soeTransactiontemp);

	Integer setSoETransactionStatus(Integer ngoId);

	SoETransaction getUpdatedFile(Integer ngoId, Integer timePeriodId);

	/**
	 * @param ngoId
	 * @param timePeriodId
	 * @Description Get blank template for one year
	 */
	SoETransaction getYearlyTemplate(Integer ngoId, Integer timePeriodId);
	
	SoETransaction getSoETemplateByMonthAndYear(int ngoId,String uuid,int timeperiodId,int biAnnualTimeperiodId);
}
