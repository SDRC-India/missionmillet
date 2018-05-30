package org.sdrc.missionmillet.domain;

import java.io.Serializable;

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
 * @author Subrata
 * This Entity class will keep all the NGO reports/SOE files.
 * Here the excel file will be stored in the Database. 
 * But the case of reports file(only the PDF) storing the file path.  
 * 
 * @since version 1.0.0.0
 *
 */
@Entity
@Table(name="ngo_report_file")
public class NgoReportsFile implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_file_id_pk")
	private Integer reportsFileId;
	
	@ManyToOne
	@JoinColumn(name = "ngo_soe_reports_id_fk")
	private NGOSoEReport ngoSoeReport; 
	
	@ManyToOne
	@JoinColumn(name = "ngo_report_id_fk")
	private NgoReports ngoReports; 
	
	@ManyToOne
	@JoinColumn(name="user_id_fk", nullable = false)
	private CollectUser collectUser;
	
	@Column(name = "report_file")
	@Lob 
	@Basic(fetch = FetchType.LAZY)
	private byte[] reportsFile;
	
	@ManyToOne
	@JoinColumn(name = "type_detail_id_fk", nullable = false)
	private TypeDetails status;
	
	@Column(name = "is_live")
	private Boolean isLive;
	
	@Column(name = "file_path")
	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getReportsFileId() {
		return reportsFileId;
	}

	public void setReportsFileId(Integer reportsFileId) {
		this.reportsFileId = reportsFileId;
	}

	public NGOSoEReport getNgoSoeReport() {
		return ngoSoeReport;
	}

	public void setNgoSoeReport(NGOSoEReport ngoSoeReport) {
		this.ngoSoeReport = ngoSoeReport;
	}
	
	public NgoReports getNgoReports() {
		return ngoReports;
	}

	public void setNgoReports(NgoReports ngoReports) {
		this.ngoReports = ngoReports;
	}

	public CollectUser getCollectUser() {
		return collectUser;
	}

	public void setCollectUser(CollectUser collectUser) {
		this.collectUser = collectUser;
	}

	public byte[] getReportsFile() {
		return reportsFile;
	}

	public void setReportsFile(byte[] reportsFile) {
		this.reportsFile = reportsFile;
	}
	
	public TypeDetails getStatus() {
		return status;
	}

	public void setStatus(TypeDetails status) {
		this.status = status;
	}

	public Boolean isLive() {
		return isLive;
	}

	public void setLive(Boolean isLive) {
		this.isLive = isLive;
	}

}
