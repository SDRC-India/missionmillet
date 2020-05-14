package org.sdrc.missionmillet.model;

public class DistrictUserTemplateModel {
       private Integer templateId;
       private byte[] templateFile;
       private Integer typeDetailId;
       private Integer timePeriodId;
       private boolean status;
       private Integer uuIdGeneratorId;
       private Integer ngoId;
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
	public Integer getTypeDetailId() {
		return typeDetailId;
	}
	public void setTypeDetailId(Integer typeDetailId) {
		this.typeDetailId = typeDetailId;
	}
	public Integer getTimePeriodId() {
		return timePeriodId;
	}
	public void setTimePeriodId(Integer timePeriodId) {
		this.timePeriodId = timePeriodId;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Integer getUuIdGeneratorId() {
		return uuIdGeneratorId;
	}
	public void setUuIdGeneratorId(Integer uuIdGeneratorId) {
		this.uuIdGeneratorId = uuIdGeneratorId;
	}
	public Integer getNgoId() {
		return ngoId;
	}
	public void setNgoId(Integer ngoId) {
		this.ngoId = ngoId;
	}
       
       
}
