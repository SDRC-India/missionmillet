package org.sdrc.missionmillet.model;

import java.sql.Timestamp;


public class AreaModel {

private Integer areaId;
	
	private String areaName;
	
	private String areaCode;
	
	private Integer parentAreaId;
	
	private Boolean isLive;
	
	private String createdBy;

	private Timestamp createdDate;
	
	private String updatedBy;

	private Timestamp updatedDate;
	
	private Integer areaLevelId;
	
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Integer getParentAreaId() {
		return parentAreaId;
	}

	public void setParentAreaId(Integer parentAreaId) {
		this.parentAreaId = parentAreaId;
	}

	public Boolean getIsLive() {
		return isLive;
	}

	public void setIsLive(Boolean isLive) {
		this.isLive = isLive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getAreaLevelId() {
		return areaLevelId;
	}

	public void setAreaLevelId(Integer areaLevelId) {
		this.areaLevelId = areaLevelId;
	}
}
