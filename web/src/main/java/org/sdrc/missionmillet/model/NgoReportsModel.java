package org.sdrc.missionmillet.model;

import org.springframework.web.multipart.MultipartFile;

public class NgoReportsModel {

	private Integer reportsId;
	
	private String createdDate;
	
	private String month;
	
	private Integer year;
	
	private MultipartFile uploadedFile;
	
	private byte[] uploadedCertificate;
	
	private String remarks;
	
	private Integer downloadUploadedFile;
	
	private Integer downloadUploadedCertificate;
	
	private Integer reportTypeId;
	
	private String reportType;
	
	private String financialYear;
	
	private Integer ngoId;
	
	private String distName;
	
	private String ngoName;
	
	private boolean isCheck;
	
	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public String getDistName() {
		return distName;
	}

	public String getNgoName() {
		return ngoName;
	}

	public void setNgoName(String ngoName) {
		this.ngoName = ngoName;
	}

	public void setDistName(String distName) {
		this.distName = distName;
	}

	public Integer getNgoId() {
		return ngoId;
	}

	public void setNgoId(Integer ngoId) {
		this.ngoId = ngoId;
	}

	public Integer getReportTypeId() {
		return reportTypeId;
	}

	public void setReportTypeId(Integer reportTypeId) {
		this.reportTypeId = reportTypeId;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public Integer getDownloadUploadedFile() {
		return downloadUploadedFile;
	}

	public void setDownloadUploadedFile(Integer downloadUploadedFile) {
		this.downloadUploadedFile = downloadUploadedFile;
	}

	public Integer getDownloadUploadedCertificate() {
		return downloadUploadedCertificate;
	}

	public void setDownloadUploadedCertificate(Integer downloadUploadedCertificate) {
		this.downloadUploadedCertificate = downloadUploadedCertificate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

//	public byte[] getUploadedFile() {
//		return uploadedFile;
//	}
//
//	public void setUploadedFile(byte[] uploadedFile) {
//		this.uploadedFile = uploadedFile;
//	}

	public byte[] getUploadedCertificate() {
		return uploadedCertificate;
	}

	public MultipartFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(MultipartFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public void setUploadedCertificate(byte[] uploadedCertificate) {
		this.uploadedCertificate = uploadedCertificate;
	}

	public Integer getReportsId() {
		return reportsId;
	}

	public void setReportsId(Integer reportsId) {
		this.reportsId = reportsId;
	}

//	public Timestamp getCreatedDate() {
//		return createdDate;
//	}
//
//	public void setCreatedDate(Timestamp createdDate) {
//		this.createdDate = createdDate;
//	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
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

}
