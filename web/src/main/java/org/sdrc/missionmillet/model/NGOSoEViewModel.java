package org.sdrc.missionmillet.model;

public class NGOSoEViewModel {
	
	private Integer ngoSoEReportsId;
	private String month;
	private Integer year;
	private String createdDate;
	private Integer ngoSoEUploadsStatusId;
	private Integer typeDetailId;
	private String status;
	private Integer userId;
	private String remarks;
	private Integer rejectCount;
	private String actionTakenUser;
	private String financialYear;
	private String actionTakenDate;
	
	public String getActionTakenDate() {
		return actionTakenDate;
	}
	public void setActionTakenDate(String actionTakenDate) {
		this.actionTakenDate = actionTakenDate;
	}
	public String getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}
	public String getActionTakenUser() {
		return actionTakenUser;
	}
	public void setActionTakenUser(String actionTakenUser) {
		this.actionTakenUser = actionTakenUser;
	}
	public Integer getRejectCount() {
		return rejectCount;
	}
	public void setRejectCount(Integer rejectCount) {
		this.rejectCount = rejectCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getNgoSoEReportsId() {
		return ngoSoEReportsId;
	}
	public void setNgoSoEReportsId(Integer ngoSoEReportsId) {
		this.ngoSoEReportsId = ngoSoEReportsId;
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
	public Integer getNgoSoEUploadsStatusId() {
		return ngoSoEUploadsStatusId;
	}
	public void setNgoSoEUploadsStatusId(Integer ngoSoEUploadsStatusId) {
		this.ngoSoEUploadsStatusId = ngoSoEUploadsStatusId;
	}
	public Integer getTypeDetailId() {
		return typeDetailId;
	}
	public void setTypeDetailId(Integer typeDetailId) {
		this.typeDetailId = typeDetailId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
