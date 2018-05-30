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
@Table(name="mst_feature")
public class Feature {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "feature_id_pk")
	private int featureId;

	@Column(name = "feature_code",length = 50)
	private String featureCode;

	@Column(name = "feature_name",length = 50)
	private String featureName;

	@Column(name = "description")
	private String description;

	@Column(name = "updated_date")
	private Timestamp updatedDate;

	@Column(name = "updated_by",length = 60)
	private String updatedBy;

	@OneToMany(mappedBy = "feature", fetch = FetchType.LAZY)
	private List<FeaturePermissionMapping> featurePermissionMappings;

	public int getFeatureId() {
		return featureId;
	}

	public void setFeatureId(int featureId) {
		this.featureId = featureId;
	}

	public String getFeatureCode() {
		return featureCode;
	}

	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
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
