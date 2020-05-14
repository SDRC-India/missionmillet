package org.sdrc.missionmillet.model;

public class NGOSoEUploadsStatusModel {

	private Integer ngoSoEUploadsStatusId;
	private Integer ngo_id_fk;
	private Integer monthValue;
	private String month;
	private Integer year;
	private Integer statusId;  
	private String lastUploadedDate;  
	private String deadlineDate;
	private Integer userId;
	private String financialYear;
	private boolean isSubmitted;
	private boolean onHalt;
	private String minMonth;
	
	public boolean isOnHalt() {
		return onHalt;
	}
	public void setOnHalt(boolean onHalt) {
		this.onHalt = onHalt;
	}
	public String getMinMonth() {
		return minMonth;
	}
	public void setMinMonth(String minMonth) {
		this.minMonth = minMonth;
	}
	public boolean isSubmitted() {
		return isSubmitted;
	}
	public void setSubmitted(boolean isSubmitted) {
		this.isSubmitted = isSubmitted;
	}
	public String getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}
	public Integer getMonthValue() {
		return monthValue;
	}
	public void setMonthValue(Integer monthValue) {
		this.monthValue = monthValue;
	}
	public Integer getNgoSoEUploadsStatusId() {
		return ngoSoEUploadsStatusId;
	}
	public void setNgoSoEUploadsStatusId(Integer ngoSoEUploadsStatusId) {
		this.ngoSoEUploadsStatusId = ngoSoEUploadsStatusId;
	}
	public Integer getNgo_id_fk() {
		return ngo_id_fk;
	}
	public void setNgo_id_fk(Integer ngo_id_fk) {
		this.ngo_id_fk = ngo_id_fk;
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
	public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	public String getLastUploadedDate() {
		return lastUploadedDate;
	}
	public void setLastUploadedDate(String lastUploadedDate) {
		this.lastUploadedDate = lastUploadedDate;
	}
	public String getDeadlineDate() {
		return deadlineDate;
	}
	public void setDeadlineDate(String deadlineDate) {
		this.deadlineDate = deadlineDate;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
