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
 * @author Subrata
 * This Entity class will keep all the NGO reports 
 * 
 * @since version 1.0.0.0
 *
 */
@Entity
@Table(name="ngo_report")
public class NgoReports implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id_pk")
	private Integer reportsId;
	
	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "month")
	private int month;
	
	@Column(name = "year")
	private int year;
	
	@Column(name = "remarks", length = 250)
	private String remarks;
	
	@Column(name = "is_live")
	private boolean isLive;
	
	@ManyToOne
	@JoinColumn(name="user_id_fk", nullable = false)
	private CollectUser collectUser;
	
	@ManyToOne
	@JoinColumn(name = "ngo_id_fk", nullable = false)
	private NGO ngo;
	
	@ManyToOne
	@JoinColumn(name = "report_type_id_fk", nullable = false)
	private TypeDetails reportType;
	
	@ManyToOne
	@JoinColumn(name = "timeperiod_id_fk")
	private TimePeriod timePeriod;
	
	public CollectUser getCollectUser() {
		return collectUser;
	}

	public void setCollectUser(CollectUser collectUser) {
		this.collectUser = collectUser;
	}

	public NGO getNgo() {
		return ngo;
	}

	public void setNgo(NGO ngo) {
		this.ngo = ngo;
	}

	public TypeDetails getReportType() {
		return reportType;
	}

	public void setReportType(TypeDetails reportType) {
		this.reportType = reportType;
	}

	public Integer getReportsId() {
		return reportsId;
	}

	public void setReportsId(Integer reportsId) {
		this.reportsId = reportsId;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}

	public NgoReports(Integer reportsId) {
		this.reportsId = reportsId;
	}
	public NgoReports() {
		
	}
	
}
