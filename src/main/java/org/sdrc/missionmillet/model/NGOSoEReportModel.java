package org.sdrc.missionmillet.model;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

public class NGOSoEReportModel {

	private Integer ngoSoEReportsId;
	private int month;
	private int year;
	private String uuid;
	private Integer uuidGeneratorId;
	private Integer areaId;
	private Integer userId;
	private Timestamp createdDate;
	private String createdBy;
	private String remarks;
	private Integer rejectCount;
	private boolean isLive;
	private boolean isLatest;
	private MultipartFile[] soeFile;
	private Integer statusType;
	
	public boolean isLatest() {
		return isLatest;
	}
	public void setLatest(boolean isLatest) {
		this.isLatest = isLatest;
	}
	public Integer getStatusType() {
		return statusType;
	}
	public void setStatusType(Integer statusType) {
		this.statusType = statusType;
	}
	public MultipartFile[] getSoeFile() {
		return soeFile;
	}
	public void setSoeFile(MultipartFile[] soeFile) {
		this.soeFile = soeFile;
	}
	public boolean isLive() {
		return isLive;
	}
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
	public Integer getRejectCount() {
		return rejectCount;
	}
	public void setRejectCount(Integer rejectCount) {
		this.rejectCount = rejectCount;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getNgoSoEReportsId() {
		return ngoSoEReportsId;
	}
	public void setNgoSoEReportsId(Integer ngoSoEReportsId) {
		this.ngoSoEReportsId = ngoSoEReportsId;
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
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
}
