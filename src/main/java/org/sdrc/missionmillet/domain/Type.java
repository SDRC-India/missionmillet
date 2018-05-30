package org.sdrc.missionmillet.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name="mst_type")
public class Type implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="type_id_pk")
	private int typeId;

	@Column(name = "type_code", nullable = false, length = 60)
	private String typeCode;

	@Column(name = "type_name", length = 60)
	private String typeName;

	@Column(name = "description")
	private String description;

	@Column(name = "updated_date")
	private Timestamp updatedDate;

	@Column(name = "updated_by", length = 60)
	private String updatedBy;
	
	@OneToMany(mappedBy="type")
	private List<TypeDetails> typeDetails;
	
	public String getDescription() {
		return description;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
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

	public List<TypeDetails> getTypeDetails() {
		return typeDetails;
	}

	public void setTypeDetails(List<TypeDetails> typeDetails) {
		this.typeDetails = typeDetails;
	}

	public Type() {
		super();
	}

	public Type(int id) {
		
		this.typeId=id;
	}
	
}
