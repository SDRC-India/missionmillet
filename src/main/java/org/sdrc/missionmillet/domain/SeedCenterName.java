package org.sdrc.missionmillet.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * This table contains all the seed center name
 * 
 * @author Subrata
 * 
 */

@Entity
@Table(name="mst_seed_center_name")
public class SeedCenterName implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="seed_center_id")
	private Integer seedCenterId; 
	
	@Column(name="seed_center_name")
	private String seedCenterName;

	public Integer getSeedCenterId() {
		return seedCenterId;
	}

	public void setSeedCenterId(Integer seedCenterId) {
		this.seedCenterId = seedCenterId;
	}

	public String getSeedCenterName() {
		return seedCenterName;
	}

	public void setSeedCenterName(String seedCenterName) {
		this.seedCenterName = seedCenterName;
	}
	
}
