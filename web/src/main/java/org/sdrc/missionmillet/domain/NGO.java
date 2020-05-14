package org.sdrc.missionmillet.domain;

import java.io.Serializable;

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
 * Storing all the NGO user and their districtID and blockID
 * 
 */
@Entity
@Table(name="mst_ngo")
public class NGO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ngo_id_pk")
	private Integer id;
	
	@Column(name = "name",length = 100)
	private String name;
	
	@Column(name = "address",length = 200)
	private String address;
	
	@ManyToOne
	@JoinColumn(name = "district_id_fk")
	private Area districtId;
	
	@ManyToOne
	@JoinColumn(name = "block_id_fk")
	private Area blockId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Area getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Area districtId) {
		this.districtId = districtId;
	}

	public Area getBlockId() {
		return blockId;
	}

	public void setBlockId(Area blockId) {
		this.blockId = blockId;
	}

	public NGO(Integer id) {
		this.id = id;
	}
	public NGO() {
		
	}
	
}
