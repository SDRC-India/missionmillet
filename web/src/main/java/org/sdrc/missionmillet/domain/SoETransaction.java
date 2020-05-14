package org.sdrc.missionmillet.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author Subrata
 * 
 * SoETransaction table will store when the district user uploads the excel for NGO.
 * 
 */
@Entity
@Table(name = "soe_transaction")
public class SoETransaction implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "soe_transaction_id_pk")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "soetemplate_id_fk", nullable = false)
	private SoETemplate soeTemplate;

	@Column(name = "uploaded_template")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] uploadedTemplate;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "created_by",length=60)
	private String createdBy;

	@Column(name = "is_live")
	private boolean isLive;

	@ManyToOne
	@JoinColumn(name = "timeperiod_id_fk", nullable = false)
	private TimePeriod timePeriod;
	
	@Column(name = "bi_annual_tp")
	private Integer biAnnualTp;

	public Integer getBiAnnualTp() {
		return biAnnualTp;
	}

	public void setBiAnnualTp(Integer biAnnualTp) {
		this.biAnnualTp = biAnnualTp;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SoETemplate getSoeTemplate() {
		return soeTemplate;
	}

	public void setSoeTemplate(SoETemplate soeTemplate) {
		this.soeTemplate = soeTemplate;
	}

	public byte[] getUploadedTemplate() {
		return uploadedTemplate;
	}

	public void setUploadedTemplate(byte[] uploadedTemplate) {
		this.uploadedTemplate = uploadedTemplate;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}

}
