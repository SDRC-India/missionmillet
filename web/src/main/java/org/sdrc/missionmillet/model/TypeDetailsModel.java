package org.sdrc.missionmillet.model;

import java.sql.Timestamp;

public class TypeDetailsModel {

	private Integer typeDetailId;
	private String typeDetailName;
	private String createdBy;
	private Timestamp createdDate;
	private Integer typeId;
	private boolean monthlyAvailability;
	
	public boolean isMonthlyAvailability() {
		return monthlyAvailability;
	}
	public void setMonthlyAvailability(boolean monthlyAvailability) {
		this.monthlyAvailability = monthlyAvailability;
	}
	public Integer getTypeDetailId() {
		return typeDetailId;
	}
	public void setTypeDetailId(Integer typeDetailId) {
		this.typeDetailId = typeDetailId;
	}
	public String getTypeDetailName() {
		return typeDetailName;
	}
	public void setTypeDetailName(String typeDetailName) {
		this.typeDetailName = typeDetailName;
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
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
}
