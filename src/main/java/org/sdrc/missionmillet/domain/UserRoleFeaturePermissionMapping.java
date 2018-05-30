package org.sdrc.missionmillet.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author Subrata
 * 
 */
@Entity
@Table(name="mst_user_role_feature_permission")
public class UserRoleFeaturePermissionMapping implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_role_feature_permission_id_pk")
	private Integer userRoleFeaturePermissionId;
	
	@ManyToOne
	@JoinColumn(name="role_feature_permission_scheme_id_fk", nullable = false)
	private RoleFeaturePermissionScheme roleFeaturePermissionScheme;
	
	@ManyToOne
	@JoinColumn(name="user_area_mapping_id_fk")
	private UserAreaMapping userAreaMapping;
	
	@Column(name="updated_date")
	private Timestamp updatedDate;
	
	@Column(name="updated_by", length = 60)
	private String updatedBy;

	public Integer getUserRoleFeaturePermissionId() {
		return userRoleFeaturePermissionId;
	}

	public void setUserRoleFeaturePermissionId(Integer userRoleFeaturePermissionId) {
		this.userRoleFeaturePermissionId = userRoleFeaturePermissionId;
	}

	public RoleFeaturePermissionScheme getRoleFeaturePermissionScheme() {
		return roleFeaturePermissionScheme;
	}

	public void setRoleFeaturePermissionScheme(RoleFeaturePermissionScheme roleFeaturePermissionScheme) {
		this.roleFeaturePermissionScheme = roleFeaturePermissionScheme;
	}

	public UserAreaMapping getUserAreaMapping() {
		return userAreaMapping;
	}

	public void setUserAreaMapping(UserAreaMapping userAreaMapping) {
		this.userAreaMapping = userAreaMapping;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	
}
