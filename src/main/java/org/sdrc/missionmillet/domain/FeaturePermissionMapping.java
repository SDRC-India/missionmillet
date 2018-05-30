package org.sdrc.missionmillet.domain;

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
@Table(name="mst_feature_permission_mapping")
public class FeaturePermissionMapping {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="feature_permission_mapping_id_pk")
	private Integer featurePermissionId;
	
	@ManyToOne
	@JoinColumn(name="feature_id_fk")
	private Feature feature;
	
	@ManyToOne
	@JoinColumn(name="permission_id_fk")
	private Permission permission;
	
	@Column(name="updated_date")
	private Timestamp updatedDate;
	
	@Column(name="updated_by",length = 60)
	private String updatedBy;
	
	
	@Column(name="description")
	private String description;
	
	@OneToMany(mappedBy="featurePermissionMapping",fetch=FetchType.LAZY)
	private List<RoleFeaturePermissionScheme> roleFeaturePermissionSchemes;

	public Integer getFeaturePermissionId() {
		return featurePermissionId;
	}

	public void setFeaturePermissionId(Integer featurePermissionId) {
		this.featurePermissionId = featurePermissionId;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
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

	public List<RoleFeaturePermissionScheme> getRoleFeaturePermissionSchemes() {
		return roleFeaturePermissionSchemes;
	}

	public void setRoleFeaturePermissionSchemes(List<RoleFeaturePermissionScheme> roleFeaturePermissionSchemes) {
		this.roleFeaturePermissionSchemes = roleFeaturePermissionSchemes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
