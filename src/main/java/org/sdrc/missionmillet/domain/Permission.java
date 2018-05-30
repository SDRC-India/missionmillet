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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Subrata
 * 
 */
@Entity
@Table(name="mst_permission")
public class Permission implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="permission_id_pk")
	private Integer permissionId;
	
	@Column(name="permission_code",length = 50)
	private String permissionCode;
	
	@Column(name="permission_name",length = 50)
	private String permissionName;
	
	@Column(name="description")
	private String description;
	
	@Column(name="updated_date")
	private Timestamp updatedDate;
	
	@Column(name="updated_by",length = 60)
	private String updatedBy;
	
	@OneToMany(mappedBy="permission",fetch=FetchType.LAZY)
	private List<FeaturePermissionMapping> featurePermissionMappings;

	public Integer getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	public String getPermissionCode() {
		return permissionCode;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public List<FeaturePermissionMapping> getFeaturePermissionMappings() {
		return featurePermissionMappings;
	}

	public void setFeaturePermissionMappings(List<FeaturePermissionMapping> featurePermissionMappings) {
		this.featurePermissionMappings = featurePermissionMappings;
	}

}
