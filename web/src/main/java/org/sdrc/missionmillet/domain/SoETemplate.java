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
import javax.persistence.UniqueConstraint;

/**
 * 
 * @author Subrata
 * 
 * Storing the blank SoE_template 
 * 
 */
@Entity
@Table(name = "soe_template", uniqueConstraints={
		@UniqueConstraint(columnNames = {"timeperiod_id_fk", "ngo_id_fk"})})
public class SoETemplate implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "template_id_pk")
	private Integer templateId;

	@Column(name = "template_file")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] templateFile;

	@ManyToOne
	@JoinColumn(name = "timeperiod_id_fk", nullable = false)
	private TimePeriod timePeriod;

	@Column(name = "status")
	private boolean status;

	@Column(name = "uuid",length=50)
	private String uuid;

	@ManyToOne
	@JoinColumn(name = "ngo_id_fk", nullable = false)
	private NGO ngo;
	
	@Column(name = "created_date")
	private Timestamp createdDate;
	

	public SoETemplate() {

	}

	public SoETemplate(Integer templateId) {

		this.templateId = templateId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public byte[] getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(byte[] templateFile) {
		this.templateFile = templateFile;
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public NGO getNgo() {
		return ngo;
	}

	public void setNgo(NGO ngo) {
		this.ngo = ngo;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

}
