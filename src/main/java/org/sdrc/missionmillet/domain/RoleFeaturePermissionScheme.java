package org.sdrc.missionmillet.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Subrata
 * 
 */
@Entity
@Table(name="mst_role_feature_permission_scheme")
public class RoleFeaturePermissionScheme implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="role_feature_permission_scheme_id_pk")
	private Integer roleFeaturePermissionSchemeId;
	
	@Column(name="scheme_name", length = 60)
	private String schemeName;
	
	@ManyToOne
	@JoinColumn(name="role_id_fk", nullable = false)
	private Role role;
	
	@ManyToOne
	@JoinColumn(name="feature_permission_id_fk")
	private FeaturePermissionMapping featurePermissionMapping;
	
	@Column(name="updated_date")
	private Timestamp updatedDate;
	
	@Column(name="updated_by", length = 60)
	private String updatedBy;
	
	@OneToMany(mappedBy="roleFeaturePermissionScheme",fetch=FetchType.LAZY)
	private List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings;

	public Integer getRoleFeaturePermissionSchemeId() {
		return roleFeaturePermissionSchemeId;
	}

	public void setRoleFeaturePermissionSchemeId(Integer roleFeaturePermissionSchemeId) {
		this.roleFeaturePermissionSchemeId = roleFeaturePermissionSchemeId;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public FeaturePermissionMapping getFeaturePermissionMapping() {
		return featurePermissionMapping;
	}

	public void setFeaturePermissionMapping(FeaturePermissionMapping featurePermissionMapping) {
		this.featurePermissionMapping = featurePermissionMapping;
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

	public List<UserRoleFeaturePermissionMapping> getUserRoleFeaturePermissionMappings() {
		return userRoleFeaturePermissionMappings;
	}

	public void setUserRoleFeaturePermissionMappings(List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings) {
		this.userRoleFeaturePermissionMappings = userRoleFeaturePermissionMappings;
	}
	
}
