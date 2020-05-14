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
 *storing wassan_level report details here
 * 
 */
@Entity
@Table(name = "wassan_report")
public class WASSANReports implements Serializable {

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
	@JoinColumn(name = "report_type_id_fk", nullable = false)
	private TypeDetails reportType;
	
	@ManyToOne
	@JoinColumn(name = "timeperiod_id_fk")
	private TimePeriod timePeriod;
	
	@Column(name = "report_name")
	private String reportName;

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

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

}
