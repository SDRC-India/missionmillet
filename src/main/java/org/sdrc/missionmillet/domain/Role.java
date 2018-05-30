package org.sdrc.missionmillet.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Subrata
 * 
 */
@Entity
@Table(name="mst_role")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id_pk")
	private Integer roleId;

	@Column(name = "role_code",length = 60)
	private String roleCode;

	@Column(name = "role_name",length = 60)
	private String roleName;

	@Column(name = "description")
	private String description;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "last_updated_date")
	private Timestamp lastUpdatedDate;
	
	@OneToMany(mappedBy = "role", fetch=FetchType.EAGER)
	private List<RoleFeaturePermissionScheme> roleFeaturePermissionSchemes;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public List<RoleFeaturePermissionScheme> getRoleFeaturePermissionSchemes() {
		return roleFeaturePermissionSchemes;
	}

	public void setRoleFeaturePermissionSchemes(
			List<RoleFeaturePermissionScheme> roleFeaturePermissionSchemes) {
		this.roleFeaturePermissionSchemes = roleFeaturePermissionSchemes;
	}

}
