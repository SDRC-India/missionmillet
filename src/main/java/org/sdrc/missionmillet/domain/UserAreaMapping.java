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
 * This Entity class will keep all the User Area Mapping details
 * 
 * @since version 1.0.0.0
 * 
 * @author Subrata
 *
 */
@Entity
@Table(name="mst_user_area_mapping")
public class UserAreaMapping implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_area_mapping_id_pk")
	private Integer userAreaMappingId;
	
	@Column(name="created_date")
	private Timestamp createdDate;
	
	@Column(name="is_live")
	private Boolean isLive;
	
//	********  bi-directional many-to-one association to Area *******
	@ManyToOne
	@JoinColumn(name="area_id_fk")
	private Area area;
	
//	********  bi-directional many-to-one association to User *******
	@ManyToOne
	@JoinColumn(name="user_id_fk")
	private CollectUser collectUser;
	
//	********bi-directional one-to-many association to UserRoleFeaturePermissionMapping *******
	@OneToMany(mappedBy="userAreaMapping",fetch=FetchType.EAGER)
	private List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings;

	public Integer getUserAreaMappingId() {
		return userAreaMappingId;
	}

	public void setUserAreaMappingId(Integer userAreaMappingId) {
		this.userAreaMappingId = userAreaMappingId;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Boolean getIsLive() {
		return isLive;
	}

	public void setIsLive(Boolean isLive) {
		this.isLive = isLive;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public CollectUser getUser() {
		return collectUser;
	}

	public void setUser(CollectUser collectUser) {
		this.collectUser = collectUser;
	}

	public CollectUser getCollectUser() {
		return collectUser;
	}

	public void setCollectUser(CollectUser collectUser) {
		this.collectUser = collectUser;
	}

	public List<UserRoleFeaturePermissionMapping> getUserRoleFeaturePermissionMappings() {
		return userRoleFeaturePermissionMappings;
	}

	public void setUserRoleFeaturePermissionMappings(
			List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings) {
		this.userRoleFeaturePermissionMappings = userRoleFeaturePermissionMappings;
	}
	
}
