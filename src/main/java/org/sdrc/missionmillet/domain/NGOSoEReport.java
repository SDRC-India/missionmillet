package org.sdrc.missionmillet.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This Entity class will keep all the NGO SOE report details 
 * and the excel file will be stored in the NgoReportsFile
 * 
 * @since version 1.0.0.0
 *
 * @author Subrata
 *
 */
@Entity
@Table(name="ngo_soe_report")
public class NGOSoEReport implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ngo_soe_report_id_pk")
	private Integer ngoSoEReportsId;
	
	@Column(name = "month")
	private int month;
	
	@Column(name = "year")
	private int year;
	
	@ManyToOne
	@JoinColumn(name = "soe_template_id_fk")
	private SoETemplate soETemplate;
	
	@ManyToOne
	@JoinColumn(name = "ngo_id_fk", nullable = false)
	private NGO ngo;
	
	@ManyToOne
	@JoinColumn(name="user_id_fk", nullable = false)
	private CollectUser collectUser;
	
	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "remarks", length = 250)
	private String remarks;
	
	@ManyToOne
	@JoinColumn(name = "re_entry_status")
	private TypeDetails reEntryStatus;
	
	@ManyToOne
	@JoinColumn(name="action_taken_by_user_id_fk")
	private CollectUser actionTakenByUserId;
	
	@Column(name = "action_taken_date")
	private Timestamp actionTakenDate;
	
	@Column(name = "is_live")
	private boolean isLive;
	
	@ManyToOne
	@JoinColumn(name = "ngo_soe_upload_status_id_fk", nullable = false)
	private NGOSoEUploadsStatus nGOSoEUploadsStatus;
	
	@ManyToOne
	@JoinColumn(name = "timeperiod_id_fk")
	private TimePeriod timePeriod;
	
	@Column(name = "deadline_date")
	private Timestamp deadlineDate;
	
	@Column(name = "is_latest")
	private Boolean isLatest;
	
	@Column(name = "first_deadline_date")
	private Timestamp firstDeadlineDate;
	
	public Timestamp getFirstDeadlineDate() {
		return firstDeadlineDate;
	}
	public void setFirstDeadlineDate(Timestamp firstDeadlineDate) {
		this.firstDeadlineDate = firstDeadlineDate;
	}
	public Boolean isLatest() {
		return isLatest;
	}
	public void setLatest(Boolean isLatest) {
		this.isLatest = isLatest;
	}
	public NGOSoEReport(Integer ngoSoEReportsId) {
		this.ngoSoEReportsId = ngoSoEReportsId;
	}
	public NGOSoEReport() {
	}

	public Timestamp getDeadlineDate() {
		return deadlineDate;
	}
	public void setDeadlineDate(Timestamp deadlineDate) {
		this.deadlineDate = deadlineDate;
	}
	public NGO getNgo() {
		return ngo;
	}

	public void setNgo(NGO ngo) {
		this.ngo = ngo;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public TypeDetails getReEntryStatus() {
		return reEntryStatus;
	}

	public void setReEntryStatus(TypeDetails reEntryStatus) {
		this.reEntryStatus = reEntryStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public CollectUser getCollectUser() {
		return collectUser;
	}

	public void setCollectUser(CollectUser collectUser) {
		this.collectUser = collectUser;
	}

	public Integer getNgoSoEReportsId() {
		return ngoSoEReportsId;
	}

	public void setNgoSoEReportsId(Integer ngoSoEReportsId) {
		this.ngoSoEReportsId = ngoSoEReportsId;
	}

	public SoETemplate getTemplate() {
		return soETemplate;
	}

	public void setTemplate(SoETemplate soETemplate) {
		this.soETemplate = soETemplate;
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

	public SoETemplate getSoETemplate() {
		return soETemplate;
	}

	public void setSoETemplate(SoETemplate soETemplate) {
		this.soETemplate = soETemplate;
	}

	public NGOSoEUploadsStatus getnGOSoEUploadsStatus() {
		return nGOSoEUploadsStatus;
	}

	public void setnGOSoEUploadsStatus(NGOSoEUploadsStatus nGOSoEUploadsStatus) {
		this.nGOSoEUploadsStatus = nGOSoEUploadsStatus;
	}
	public CollectUser getActionTakenByUserId() {
		return actionTakenByUserId;
	}

	public void setActionTakenByUserId(CollectUser actionTakenByUserId) {
		this.actionTakenByUserId = actionTakenByUserId;
	}

	public Timestamp getActionTakenDate() {
		return actionTakenDate;
	}

	public void setActionTakenDate(Timestamp actionTakenDate) {
		this.actionTakenDate = actionTakenDate;
	}
	public TimePeriod getTimePeriod() {
		return timePeriod;
	}
	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}
	
}
