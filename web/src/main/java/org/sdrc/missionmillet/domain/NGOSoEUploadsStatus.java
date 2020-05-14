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
import javax.persistence.UniqueConstraint;

/**
 * 
 * @author Subrata
 * 
 */
@Entity
@Table(name="ngo_soe_upload_status", uniqueConstraints={
		@UniqueConstraint(columnNames = {"month", "ngo_id_fk","year", "timeperiod_id_fk"})})
public class NGOSoEUploadsStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ngo_soe_upload_status_id_pk")
	private Integer ngoSoEUploadsStatusId;
	
	@ManyToOne
	@JoinColumn(name = "ngo_id_fk")
	private NGO ngo;
	
	@Column(name = "month")
	private int month;
	
	@Column(name = "year")
	private int year;
	
	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@ManyToOne
	@JoinColumn(name = "status_id_fk", nullable = false)
	private TypeDetails status;  
	
	@Column(name = "reject_count")
	private Integer rejectCount;
	
	@Column(name = "last_uploaded_date")
	private Timestamp lastUploadedDate;  
	
	@Column(name = "deadline_date")
	private Timestamp deadlineDate;
	
	@ManyToOne
	@JoinColumn(name = "timeperiod_id_fk")
	private TimePeriod timePeriod;
	
	@Column(name = "new_entry")
	private boolean newEntry;
	
	public Integer getNgoSoEUploadsStatusId() {
		return ngoSoEUploadsStatusId;
	}

	public boolean isNewEntry() {
		return newEntry;
	}

	public void setNewEntry(boolean newEntry) {
		this.newEntry = newEntry;
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getRejectCount() {
		return rejectCount;
	}

	public void setRejectCount(Integer rejectCount) {
		this.rejectCount = rejectCount;
	}

	public void setNgoSoEUploadsStatusId(Integer ngoSoEUploadsStatusId) {
		this.ngoSoEUploadsStatusId = ngoSoEUploadsStatusId;
	}

	public NGO getNgo() {
		return ngo;
	}

	public void setNgo(NGO ngo) {
		this.ngo = ngo;
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

	public TypeDetails getStatus() {
		return status;
	}

	public void setStatus(TypeDetails status) {
		this.status = status;
	}

	public Timestamp getLastUploadedDate() {
		return lastUploadedDate;
	}

	public void setLastUploadedDate(Timestamp lastUploadedDate) {
		this.lastUploadedDate = lastUploadedDate;
	}

	public Timestamp getDeadlineDate() {
		return deadlineDate;
	}

	public void setDeadlineDate(Timestamp deadlineDate) {
		this.deadlineDate = deadlineDate;
	}
}
