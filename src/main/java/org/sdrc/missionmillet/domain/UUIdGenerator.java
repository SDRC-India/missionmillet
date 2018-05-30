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

/**
 * 
 * @author Subrata
 * 
 * Storing the UUID here.
 * 
 */
@Entity
@Table(name="uuid_generator")
public class UUIdGenerator implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "uuid_generator_id_pk")
	private Integer uuidGeneratorId;
	
	@ManyToOne
	@JoinColumn(name="user_id_fk", nullable = false)
	private CollectUser collectUser;

	@Column(name = "month")
	private int month;
	
	@Column(name = "year")
	private int year;
	
	@Column(name = "uuid",length=50)	
	private String uuid;
	
	@Column(name = "created_date")	
	private Timestamp createdDate;
	
	@ManyToOne
	@JoinColumn(name = "timeperiod_id_fk")
	private TimePeriod timePeriod;
	
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public UUIdGenerator() {
	}
	public UUIdGenerator(Integer i) {
		this.uuidGeneratorId = i;
	}

	public Integer getUuidGeneratorId() {
		return uuidGeneratorId;
	}
	public void setUuidGeneratorId(Integer uuidGeneratorId) {
		this.uuidGeneratorId = uuidGeneratorId;
	}
	public CollectUser getCollectUser() {
		return collectUser;
	}

	public void setCollectUser(CollectUser collectUser) {
		this.collectUser = collectUser;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public TimePeriod getTimePeriod() {
		return timePeriod;
	}
	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}
	
}
