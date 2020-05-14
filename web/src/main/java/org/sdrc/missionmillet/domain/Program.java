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
 * This domain class or entity class will keep all the Programs.
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
 * @author Sarita
 * @since version 1.0.0.0
 *
 */
@Entity
@Table(name="mst_program")
public class Program  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "program_id_pk")
	private int programId;
	
	@Column(name = "program_name", nullable = false, unique = true,length = 60)
	private String programName;
	
	@Column(name="is_live", nullable = false)
	private Boolean isLive;
	
	@Column(name="created_by",length = 60)
	private String createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;
	
	@Column(name="updated_by",length = 60)
	private String updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;
	
	@OneToMany(mappedBy="program")
	private List<Program_XForm_Mapping> program_XForm_Mappings;
	

	public int getProgramId() {
		return programId;
	}

	public void setProgramId(int programId) {
		this.programId = programId;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
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

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<Program_XForm_Mapping> getProgram_XForm_Mappings() {
		return program_XForm_Mappings;
	}

	public void setProgram_XForm_Mappings(
			List<Program_XForm_Mapping> program_XForm_Mappings) {
		this.program_XForm_Mappings = program_XForm_Mappings;
	}

	@Override
	public String toString() {
		return "Program [programId=" + programId + ", programName=" + programName + ", isLive=" + isLive
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", updatedBy=" + updatedBy
				+ ", updatedDate=" + updatedDate + ", program_XForm_Mappings=" + program_XForm_Mappings + "]";
	}
	
}