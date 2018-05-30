package org.sdrc.missionmillet.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Subrata This Entity class will keep all the area details
 * 
 * @since version 1.0.0.0
 *
 */
@Entity
@Table(name = "mst_area")
public class Area implements Serializable {

	private static final long serialVersionUID = 5164426154605633668L;

	@Id
	@Column(name = "area_id_pk", nullable = false)
	private Integer areaId;

	@Column(name = "area_name", nullable = false, length = 60)
	private String areaName;

	@Column(name = "area_code", nullable = false, length = 30)
	private String areaCode;

	@Column(name = "parent_area_id", nullable = false)
	private Integer parentAreaId;

	@Column(name = "is_live", nullable = false)
	private Boolean isLive;

	@Column(name = "created_by", length = 60)
	private String createdBy;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@ManyToOne
	@JoinColumn(name = "area_level_id_fk", nullable = false)
	private AreaLevel areaLevel;

	// **** bi-directional one-to-many association to UserAreaMapping *****
	@OneToMany(mappedBy = "area", fetch = FetchType.EAGER)
	private List<UserAreaMapping> userAreaMappings;

	public Area(int areaId) {
		this.areaId = areaId;
	}

	public Area() {
		super();
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Integer getParentAreaId() {
		return parentAreaId;
	}

	public void setParentAreaId(Integer parentAreaId) {
		this.parentAreaId = parentAreaId;
	}

	public Boolean getIsLive() {
		return isLive;
	}

	public void setIsLive(Boolean isLive) {
		this.isLive = isLive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public AreaLevel getAreaLevel() {
		return areaLevel;
	}

	public void setAreaLevel(AreaLevel areaLevel) {
		this.areaLevel = areaLevel;
	}

}
