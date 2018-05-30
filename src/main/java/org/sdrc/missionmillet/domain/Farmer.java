package org.sdrc.missionmillet.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * This table contains all the farmer name
 * 
 * @author Subrata
 * 
 */

@Entity
@Table(name="mst_farmer")
public class Farmer implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="farmer_id")
	private Integer farmerId; 
	
	@Column(name="farmer_name")
	private String farmerName;

	public Integer getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(Integer farmerId) {
		this.farmerId = farmerId;
	}

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}
	
}
