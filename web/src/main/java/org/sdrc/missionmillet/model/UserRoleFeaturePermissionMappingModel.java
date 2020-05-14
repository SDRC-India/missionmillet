package org.sdrc.missionmillet.model;



public class UserRoleFeaturePermissionMappingModel {

	private int userRoleFeaturePermissionId;
	
	private RoleFeaturePermissionSchemeModel roleFeaturePermissionSchemeModel;
	
	private ValueObject userDetailModel;
	
	private String updatedDate;
	
	private String updatedBy;
	
	private Integer areaId;

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public int getUserRoleFeaturePermissionId() {
		return userRoleFeaturePermissionId;
	}

	public RoleFeaturePermissionSchemeModel getRoleFeaturePermissionSchemeModel() {
		return roleFeaturePermissionSchemeModel;
	}

	public ValueObject getUserDetailModel() {
		return userDetailModel;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUserRoleFeaturePermissionId(int userRoleFeaturePermissionId) {
		this.userRoleFeaturePermissionId = userRoleFeaturePermissionId;
	}

	public void setRoleFeaturePermissionSchemeModel(RoleFeaturePermissionSchemeModel roleFeaturePermissionSchemeModel) {
		this.roleFeaturePermissionSchemeModel = roleFeaturePermissionSchemeModel;
	}

	public void setUserDetailModel(ValueObject userDetailModel) {
		this.userDetailModel = userDetailModel;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	
}
