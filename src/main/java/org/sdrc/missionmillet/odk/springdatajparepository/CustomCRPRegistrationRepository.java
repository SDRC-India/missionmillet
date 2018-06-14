package org.sdrc.missionmillet.odk.springdatajparepository;

import java.util.List;

/**
 * @author subham
 * This is added due to limited future of spring data
 */
public interface CustomCRPRegistrationRepository{
	

	List<Object[]> findAllRecords(String tableName);

}
