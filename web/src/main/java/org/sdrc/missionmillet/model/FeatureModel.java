package org.sdrc.missionmillet.model;

import java.util.List;

public class FeatureModel {

	private int featureId;
	
	private String featureCode;
	
	private String featureName;
	
	private String description;
	
	private String updatedDate;
	
	private String updatedBy;
	
	private List<ValueObject> featurePermissionMappings;

	public int getFeatureId() {
		return featureId;
	}

	public String getFeatureCode() {
		return featureCode;
	}

	public String getFeatureName() {
		return featureName;
	}

	public String getDescription() {
		return description;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setFeatureId(int featureId) {
		this.featureId = featureId;
	}

	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public List<ValueObject> getFeaturePermissionMappings() {
		return featurePermissionMappings;
	}

	public void setFeaturePermissionMappings(List<ValueObject> featurePermissionMappings) {
		this.featurePermissionMappings = featurePermissionMappings;
	}
}
