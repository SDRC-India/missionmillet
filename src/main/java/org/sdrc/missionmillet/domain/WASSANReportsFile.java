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
 * 
 * @author Subrata
 * 
 * keeping wassan level reports file info here,
 * is report file path and SOE Bytearray
 * 
 */
@Entity
@Table(name="wassan_report_file")
public class WASSANReportsFile implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_file_id_pk")
	private Integer reportsFileId;
	
	@ManyToOne
	@JoinColumn(name = "wassan_soe_reports_id_fk")
	private WASSANSoEReport wassanSoeReport; 
	
	@ManyToOne
	@JoinColumn(name = "wassan_report_id_fk")
	private WASSANReports wassanReports; 
	
	@ManyToOne
	@JoinColumn(name="user_id_fk", nullable = false)
	private CollectUser collectUser;
	
	@Column(name = "report_file")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] reportsFile;

	@Column(name = "is_live")
	private boolean isLive;
	
	@ManyToOne
	@JoinColumn(name = "type_detail_id_fk", nullable = false)
	private TypeDetails status;
	
	@Column(name = "report_file_path")
	private String filePath;
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getReportsFileId() {
		return reportsFileId;
	}

	public void setReportsFileId(Integer reportsFileId) {
		this.reportsFileId = reportsFileId;
	}

	public WASSANSoEReport getWassanSoeReport() {
		return wassanSoeReport;
	}

	public void setWassanSoeReport(WASSANSoEReport wassanSoeReport) {
		this.wassanSoeReport = wassanSoeReport;
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

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public WASSANReports getWassanReports() {
		return wassanReports;
	}

	public void setWassanReports(WASSANReports wassanReports) {
		this.wassanReports = wassanReports;
	}
	
	public TypeDetails getStatus() {
		return status;
	}

	public void setStatus(TypeDetails status) {
		this.status = status;
	}

}
