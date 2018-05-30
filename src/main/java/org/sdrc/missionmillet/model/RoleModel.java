package org.sdrc.missionmillet.model;

import java.util.List;

public class RoleModel {

	private int roleId;
	
	private String roleCode;
	
	private String roleName;
	
	private String description;
	
	private String updatedDate;
	
	private String updatedBy;
	
	private List<ValueObject> roleFeaturePermissionSchemes;

	public int getRoleId() {
		return roleId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public String getRoleName() {
		return roleName;
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

	public List<ValueObject> getRoleFeaturePermissionSchemes() {
		return roleFeaturePermissionSchemes;
	}
	
	public void setRoleFeaturePermissionSchemes(List<ValueObject> roleFeaturePermissionSchemes) {
		this.roleFeaturePermissionSchemes = roleFeaturePermissionSchemes;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

}
