package org.sdrc.missionmillet.model;

/*
 *  @author Subham Ashish(subham@sdrc.co.in)
 */
import org.springframework.web.multipart.MultipartFile;

public class WASSANReportsModel {

	private Integer reportsId;

	private String createdDate;

	private String month;

	private String year;

	private String remarks;

	private Integer downloadUploadReport;

	private Integer downloadUploadCertificate;

	private MultipartFile uploadedFile;

	private String reportType;

	private String financialYear;

	private boolean isCheck;
	
	private String reportName;

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
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

	public MultipartFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(MultipartFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public Integer getReportsId() {
		return reportsId;
	}

	public void setReportsId(Integer reportsId) {
		this.reportsId = reportsId;
	}

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

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getDownloadUploadReport() {
		return downloadUploadReport;
	}

	public void setDownloadUploadReport(Integer downloadUploadReport) {
		this.downloadUploadReport = downloadUploadReport;
	}

	public Integer getDownloadUploadCertificate() {
		return downloadUploadCertificate;
	}

	public void setDownloadUploadCertificate(Integer downloadUploadCertificate) {
		this.downloadUploadCertificate = downloadUploadCertificate;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	
}
