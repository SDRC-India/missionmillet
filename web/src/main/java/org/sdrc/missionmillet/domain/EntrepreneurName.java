package org.sdrc.missionmillet.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * This table contains all the entrepreneur name
 * 
 * @author Subrata
 * 
 */

@Entity
@Table(name="mst_entrepreneur_name")
public class EntrepreneurName implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="entrepreneur_id")
	private Integer entrepreneurId; 
	
	@Column(name="entrepreneur_name")
	private String entrepreneurName;

	public Integer getEntrepreneurId() {
		return entrepreneurId;
	}

	public void setEntrepreneurId(Integer entrepreneurId) {
		this.entrepreneurId = entrepreneurId;
	}

	public String getEntrepreneurName() {
		return entrepreneurName;
	}

	public void setEntrepreneurName(String entrepreneurName) {
		this.entrepreneurName = entrepreneurName;
	}

}
