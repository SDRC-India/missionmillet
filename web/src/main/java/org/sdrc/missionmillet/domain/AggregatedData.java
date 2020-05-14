package org.sdrc.missionmillet.domain;

import java.io.Serializable;
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
 * Storing the aggregated data value
 */
@Entity
@Table(name="aggregated_data")
public class AggregatedData  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="aggregated_data_id_pk")
	private Integer id;
	
	@Column(name = "created_date")
	@CreationTimestamp
	private Timestamp createdDate;
	
	@Column(name = "created_by",length = 60)
	private String createdBy;
	
	@ManyToOne
	@JoinColumn(name="timeperiod_id_fk") 
	private TimePeriod timePeriod;
	
	@Column(name="data_value")
	private Double dataValue;
	
	@Column(name="ngo_id")
	private Integer ngo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}

	public Double getDataValue() {
		return dataValue;
	}

	public void setDataValue(Double dataValue) {
		this.dataValue = dataValue;
	}

	public Integer getNgo() {
		return ngo;
	}

	public void setNgo(Integer ngo) {
		this.ngo = ngo;
	}

}
