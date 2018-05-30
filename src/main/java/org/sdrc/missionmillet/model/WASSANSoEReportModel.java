package org.sdrc.missionmillet.model;

/*
 *  @author Subham Ashish(subham@sdrc.co.in)
 */
public class WASSANSoEReportModel {

	private Integer reportsFileId;

	private String month;

	private Integer year;

	private String createdDate;

	private String uuid;

	private Integer uuidGeneratorId;

	private Integer userId;

	private String createdBy;

	private String financialYear;

	public Integer getReportsFileId() {
		return reportsFileId;
	}

	public void setReportsFileId(Integer reportsFileId) {
		this.reportsFileId = reportsFileId;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getUuidGeneratorId() {
		return uuidGeneratorId;
	}

	public void setUuidGeneratorId(Integer uuidGeneratorId) {
		this.uuidGeneratorId = uuidGeneratorId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

}
