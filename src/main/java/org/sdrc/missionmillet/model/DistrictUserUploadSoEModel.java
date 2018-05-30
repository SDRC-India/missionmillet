package org.sdrc.missionmillet.model;

public class DistrictUserUploadSoEModel {
	private Integer ngoId;
	private Integer month;
	private Integer year;
	private Integer typedetailId;
	private Integer reportFileId;
	private String ngoName;
	private String reMark;

	public String getReMark() {
		return reMark;
	}

	public void setReMark(String reMark) {
		this.reMark = reMark;
	}

	public String getNgoName() {
		return ngoName;
	}

	public void setNgoName(String ngoName) {
		this.ngoName = ngoName;
	}

	public Integer getReportFileId() {
		return reportFileId;
	}

	public void setReportFileId(Integer reportFileId) {
		this.reportFileId = reportFileId;
	}

	public Integer getNgoId() {
		return ngoId;
	}

	public void setNgoId(Integer ngoId) {
		this.ngoId = ngoId;
	}

	public Integer getTypedetailId() {
		return typedetailId;
	}

	public void setTypedetailId(Integer typedetailId) {
		this.typedetailId = typedetailId;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

}
