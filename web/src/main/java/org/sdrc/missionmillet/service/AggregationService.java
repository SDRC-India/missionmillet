package org.sdrc.missionmillet.service;

import java.util.List;

import org.sdrc.missionmillet.model.ValueObject;

/**
 * 
 * @author Subrata
 * 
 */
public interface AggregationService {

	String aggregate(int monthId, int periodicity);

	List<ValueObject> getPendingList(int monthId, int periodicity);

}
