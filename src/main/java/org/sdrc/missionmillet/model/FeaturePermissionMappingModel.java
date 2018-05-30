package org.sdrc.missionmillet.model;


import java.util.List;

public class FeaturePermissionMappingModel {
	
	private int featurePermissionId;
	
	private FeatureModel feature;
	
	private PermissionModel permission;
	
	private String updatedDate;
	
	private String updatedBy;
	
	private List<ValueObject> roleFeaturePermissionSchemeModels;

	public int getFeaturePermissionId() {
		return featurePermissionId;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public List<ValueObject> getRoleFeaturePermissionSchemeModels() {
		return roleFeaturePermissionSchemeModels;
	}
	
	public void setRoleFeaturePermissionSchemeModels(
			List<ValueObject> roleFeaturePermissionSchemeModels) {
		this.roleFeaturePermissionSchemeModels = roleFeaturePermissionSchemeModels;
	}

	public void setFeaturePermissionId(int featurePermissionId) {
		this.featurePermissionId = featurePermissionId;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public FeatureModel getFeature() {
		return feature;
	}

	public PermissionModel getPermission() {
		return permission;
	}

	public void setFeature(FeatureModel feature) {
		this.feature = feature;
	}

	public void setPermission(PermissionModel permission) {
		this.permission = permission;
	}

}
