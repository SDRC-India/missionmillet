package org.sdrc.missionmillet.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * This table contains all the custom hiring center name.
 * 
 * @author Subrata
 * 
 */

@Entity
@Table(name="mst_custom_hiring_center_name")
public class CustomHiringCenterName implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="custom_hiring_center_id")
	private Integer customHiringCenterId; 
	
	@Column(name="custom_hiring_center_name")
	private String customHiringCenterName;

	public Integer getCustomHiringCenterId() {
		return customHiringCenterId;
	}

	public void setCustomHiringCenterId(Integer customHiringCenterId) {
		this.customHiringCenterId = customHiringCenterId;
	}

	public String getCustomHiringCenterName() {
		return customHiringCenterName;
	}

	public void setCustomHiringCenterName(String customHiringCenterName) {
		this.customHiringCenterName = customHiringCenterName;
	}

}
