package org.sdrc.missionmillet.repository;

import java.util.List;

import org.sdrc.missionmillet.domain.Type;
import org.sdrc.missionmillet.domain.TypeDetails;

public interface TypeDetailsRepository {

	List<TypeDetails> findByTypeTypeId(int typeId);

	TypeDetails findBytypeDetailName(String reportType);
	
	/**
	 * @Descriptino Getting all the type details
	 */
	List<TypeDetails> findAll();

	/**
	 * @Description In ascending order getting type details by passing the ngoreporttype
	 * @param ngoReportType
	 */
	List<TypeDetails> findByTypeTypeIdOrderByTypeDetailIdAsc(Integer ngoReportType);

	TypeDetails findBytypeDetailNameAndType(String reportType, Type type);

}
