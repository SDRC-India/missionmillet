package org.sdrc.missionmillet.util;

/**
 * 
 * @author Subrata
 * 
 */

public interface StateManager {

	Object getValue(String key);
	
	void setValue(String Key, Object Value);

}