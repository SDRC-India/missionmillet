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
 * storing wassan SOE  details
 */
@Entity
@Table(name = "wassan_soe_report")
public class WASSANSoEReport implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wassan_soe_reports_id_pk")
	private Integer WASSANSoEReportsId;

	@Column(name = "month")
	private int month;

	@Column(name = "year")
	private int year;

	@ManyToOne
	@JoinColumn(name = "uuid_generated_fk", nullable = false)
	private UUIdGenerator UUIdGenerator;

	@ManyToOne
	@JoinColumn(name = "user_id_fk", nullable = false)
	private CollectUser collectUser;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "created_by",length=60)
	private String createdBy;
	
	@ManyToOne
	@JoinColumn(name = "timeperiod_id_fk")
	private TimePeriod timePeriod;

	public Integer getWASSANSoEReportsId() {
		return WASSANSoEReportsId;
	}

	public void setWASSANSoEReportsId(Integer wASSANSoEReportsId) {
		WASSANSoEReportsId = wASSANSoEReportsId;
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

	public CollectUser getCollectUser() {
		return collectUser;
	}

	public void setCollectUser(CollectUser collectUser) {
		this.collectUser = collectUser;
	}

	public UUIdGenerator getUUIdGenerator() {
		return UUIdGenerator;
	}

	public void setUUIdGenerator(UUIdGenerator uUIdGenerator) {
		UUIdGenerator = uUIdGenerator;
	}

	public CollectUser getUser() {
		return collectUser;
	}

	public void setUser(CollectUser collectUser) {
		this.collectUser = collectUser;
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

}
