package org.sdrc.missionmillet.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

/**
 * 
 * @author Subrata
 * 
 * Storing the cutOffdays set by the admin.
 * 
 */
@Entity
@Table(name="configuration")
public class Configuration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "configuration_id_pk")
	private int configurationId;
	
	@Column(name="ngo_cut_off_days")
	private int ngoCutOffDays;
	
	@Column(name="created_date")
	@CreationTimestamp
	private Timestamp createdDate;
	
	@Column(name="is_live")
	private boolean isLive;
	
	@Column(name = "created_by",length = 60)
	private String createdBy;
	
	@ManyToOne
	@JoinColumn(name = "type_detail_id_fk")
	private TypeDetails typeDetails;

	public int getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(int configurationId) {
		this.configurationId = configurationId;
	}

	public int getNgoCutOffDays() {
		return ngoCutOffDays;
	}

	public void setNgoCutOffDays(int ngoCutOffDays) {
		this.ngoCutOffDays = ngoCutOffDays;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public TypeDetails getTypeDetails() {
		return typeDetails;
	}

	public void setTypeDetails(TypeDetails typeDetails) {
		this.typeDetails = typeDetails;
	}
	
}
