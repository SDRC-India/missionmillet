package org.sdrc.missionmillet.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * 
 * This Entity class will keep all the Time Period details
 * 
 * @since version 1.0.0.0
 * 
 * @author Subrata
 *
 */
@Entity
@Table(name="time_period", uniqueConstraints={
		@UniqueConstraint(columnNames = {"periodicity", "time_period_duration","financial_year","year"})})
public class TimePeriod implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "timeperiod_id_pk")
	private Integer timePeriodId;
	
	@Column(name = "time_period_duration",length=30)
	private String timePeriod;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_date", nullable = false)
	private Date startDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_date", nullable = false)
	private Date endDate;
	
	@Column(name="periodicity",length=30)
	private String periodicity;
	
	@Column(name = "created_date", nullable = false)
	@CreationTimestamp
	private Timestamp createdDate;
	
	@Column(name = "updated_date")
	@UpdateTimestamp
	private Timestamp updatedDate;
	
	@Column(name = "financial_year",length=30)
	private String financialYear;
	
	@Column(name = "year")
	private Integer year;
	

	public Integer getTimePeriodId() {
		return timePeriodId;
	}
	public void setTimePeriodId(Integer timePeriodId) {
		this.timePeriodId = timePeriodId;
	}
	public String getTimePeriod() {
		return timePeriod;
	}
	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getPeriodicity() {
		return periodicity;
	}
	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((periodicity == null) ? 0 : periodicity.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((timePeriod == null) ? 0 : timePeriod.hashCode());
		result = prime * result + ((timePeriodId == null) ? 0 : timePeriodId.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimePeriod other = (TimePeriod) obj;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (periodicity == null) {
			if (other.periodicity != null)
				return false;
		} else if (!periodicity.equals(other.periodicity))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (timePeriod == null) {
			if (other.timePeriod != null)
				return false;
		} else if (!timePeriod.equals(other.timePeriod))
			return false;
		if (timePeriodId == null) {
			if (other.timePeriodId != null)
				return false;
		} else if (!timePeriodId.equals(other.timePeriodId))
			return false;
		return true;
	}
	
	public TimePeriod(Integer timePeriodId) {
		this.timePeriodId = timePeriodId;
	}
	public TimePeriod() {
		
	}
	
}

