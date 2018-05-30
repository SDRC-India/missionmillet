package org.sdrc.missionmillet.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Subrata
 * This Entity class will keep all the area level available in our system
 * @since version 1.0.0.0
 *
 */
@Entity
@Table(name="mst_area_level")
public class AreaLevel implements Serializable{
	
	private static final long serialVersionUID = 1519381375815795764L;

	@Id
	@Column(name = "area_level_id_pk")
	private Integer areaLevelId;
	
	@Column(name = "area_level_name", nullable = false,length = 30)
	private String areaLevelName;
	
	@OneToMany(mappedBy="areaLevel")
	private List<Area> areas;

	public Integer getAreaLevelId() {
		return areaLevelId;
	}

	public void setAreaLevelId(Integer areaLevelId) {
		this.areaLevelId = areaLevelId;
	}

	public String getAreaLevelName() {
		return areaLevelName;
	}

	public void setAreaLevelName(String areaLevelName) {
		this.areaLevelName = areaLevelName;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
	
}
