package org.sdrc.missionmillet.model;

public class DistrictUserNgoListModel {
	private String financialyear;
	private int ngoId;
	private String ngoName;
	private boolean status;
	private Integer monthInt;
	private String month;
	private Integer year;
	private String date;
	private Integer StatusId;
	private String statusDetail;
	private String remark;
	private int districtId;
	private int reentryStatusId;
	private String actionTakenDate;
	private int actionTakenBy;
	private String reentryStatus;
	private String timePeriod;
	private Integer timePeriodYear;
	private Integer timePeriodId;
	private Integer reportId;
	private String actionTakenByName;
	private boolean uploadStatus;
	private Integer reportFileId;
	private Integer utilizationCertificateId;
	private boolean ifFalseInOctober;
	

	public boolean isIfFalseInOctober() {
		return ifFalseInOctober;
	}

	public void setIfFalseInOctober(boolean ifFalseInOctober) {
		this.ifFalseInOctober = ifFalseInOctober;
	}

	public Integer getTimePeriodId() {
		return timePeriodId;
	}

	public void setTimePeriodId(Integer timePeriodId) {
		this.timePeriodId = timePeriodId;
	}

	public Integer getReportFileId() {
		return reportFileId;
	}

	public void setReportFileId(Integer reportFileId) {
		this.reportFileId = reportFileId;
	}

	public Integer getUtilizationCertificateId() {
		return utilizationCertificateId;
	}

	public void setUtilizationCertificateId(Integer utilizationCertificateId) {
		this.utilizationCertificateId = utilizationCertificateId;
	}

	public boolean isUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(boolean uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public String getActionTakenByName() {
		return actionTakenByName;
	}

	public void setActionTakenByName(String actionTakenByName) {
		this.actionTakenByName = actionTakenByName;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public String getFinancialyear() {
		return financialyear;
	}

	public void setFinancialyear(String financialyear) {
		this.financialyear = financialyear;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	public Integer getTimePeriodYear() {
		return timePeriodYear;
	}

	public void setTimePeriodYear(Integer timePeriodYear) {
		this.timePeriodYear = timePeriodYear;
	}

	private String districtName;

	public int getNgoId() {
		return ngoId;
	}

	public void setNgoId(int ngoId) {
		this.ngoId = ngoId;
	}

	public String getNgoName() {
		return ngoName;
	}

	public void setNgoName(String ngoName) {
		this.ngoName = ngoName;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatusDetail() {
		return statusDetail;
	}

	public void setStatusDetail(String statusDetail) {
		this.statusDetail = statusDetail;
	}

	public Integer getMonthInt() {
		return monthInt;
	}

	public void setMonthInt(Integer monthInt) {
		this.monthInt = monthInt;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getStatusId() {
		return StatusId;
	}

	public void setStatusId(Integer statusId) {
		StatusId = statusId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public int getReentryStatusId() {
		return reentryStatusId;
	}

	public void setReentryStatusId(int reentryStatusId) {
		this.reentryStatusId = reentryStatusId;
	}

	public String getActionTakenDate() {
		return actionTakenDate;
	}

	public void setActionTakenDate(String actionTakenDate) {
		this.actionTakenDate = actionTakenDate;
	}

	public int getActionTakenBy() {
		return actionTakenBy;
	}

	public void setActionTakenBy(int actionTakenBy) {
		this.actionTakenBy = actionTakenBy;
	}

	public String getReentryStatus() {
		return reentryStatus;
	}

	public void setReentryStatus(String reentryStatus) {
		this.reentryStatus = reentryStatus;
	}

}
