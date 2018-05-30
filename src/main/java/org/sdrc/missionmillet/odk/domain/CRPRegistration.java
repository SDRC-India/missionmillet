package org.sdrc.missionmillet.odk.domain;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="crp_registration_05022018_v1_core")
public class CRPRegistration implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="_URI", nullable=false)
	private String URI;
	
	@Column(name="_CREATOR_URI_USER", nullable=false)
	private String CREATOR_URI_USER;
	
	@Column(name="_CREATION_DATE", nullable=false)
	private Date CREATION_DATE;
	
	@Column(name="_LAST_UPDATE_URI_USER")
	private String LAST_UPDATE_URI_USER;
	
	@Column(name="_LAST_UPDATE_DATE", nullable=false)
	private Date LAST_UPDATE_DATE;

	@Column(name="_MODEL_VERSION")
	private Integer MODEL_VERSION;
	
	@Column(name="_UI_VERSION")
	private Integer UI_VERSION;
	
	@Column(name="_IS_COMPLETE")
	private Boolean IS_COMPLETE;
	
	@Column(name="_SUBMISSION_DATE")
	private Date SUBMISSION_DATE;
	
	@Column(name="_MARKED_AS_COMPLETE_DATE")
	private Date MARKED_AS_COMPLETE_DATE;
	
	@Column(name="QS_11")
	private String QS_11;
	
	@Column(name="QS_13")
	private String QS_13;
	
	@Column(name="MONITORING_DATE")
	private String MONITORING_DATE;
	
	@Column(name="PHONE")
	private String PHONE;
	
	@Column(name="BG_STATE")
	private String BG_STATE;
	
	@Column(name="META_INSTANCE_ID")
	private String META_INSTANCE_ID;
	
	@Column(name="DISTRICT")
	private String DISTRICT;
	
	@Column(name="GRAMPANCHAYAT")
	private String GRAMPANCHAYAT;
	
	@Column(name="GEOPOINT_ALT")
	private Double GEOPOINT_ALT;
	
	@Column(name="GEOPOINT_LNG")
	private Double GEOPOINT_LNG;
	
	@Column(name="GEOPOINT_LAT")
	private Double GEOPOINT_LAT;
	
	@Column(name="BG_COUNTRY")
	private String BG_COUNTRY;
	
	@Column(name="BLOCK")
	private String BLOCK;
	
	@Column(name="VILLAGE1")
	private String VILLAGE1;
	
	@Column(name="NAME")
	private String NAME;
	
	@Column(name="VILLAGE")
	private String VILLAGE;
	
	@Column(name="QS_3")
	private String QS_3;
	
	@Column(name="DATE")
	private String DATE;
	
	@Column(name="QS_1")
	private String QS_1;
	
	@Column(name="QS_2")
	private String QS_2;
	
	@Column(name="DEVICEID")
	private String DEVICEID;
	
	@Column(name="QS_9")
	private String QS_9;
	
	@Column(name="QS_7")
	private String QS_7;
	
	@Column(name="QS_8")
	private String QS_8;
	
	@Column(name="GEOPOINT_ACC")
	private Double GEOPOINT_ACC;
	
	@Column(name="QS_5")
	private String QS_5;
	
	@Column(name="QS_6")
	private String QS_6;

	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}

	public String getCREATOR_URI_USER() {
		return CREATOR_URI_USER;
	}

	public void setCREATOR_URI_USER(String cREATOR_URI_USER) {
		CREATOR_URI_USER = cREATOR_URI_USER;
	}

	public Date getCREATION_DATE() {
		return CREATION_DATE;
	}

	public void setCREATION_DATE(Date cREATION_DATE) {
		CREATION_DATE = cREATION_DATE;
	}

	public String getLAST_UPDATE_URI_USER() {
		return LAST_UPDATE_URI_USER;
	}

	public void setLAST_UPDATE_URI_USER(String lAST_UPDATE_URI_USER) {
		LAST_UPDATE_URI_USER = lAST_UPDATE_URI_USER;
	}

	public Date getLAST_UPDATE_DATE() {
		return LAST_UPDATE_DATE;
	}

	public void setLAST_UPDATE_DATE(Date lAST_UPDATE_DATE) {
		LAST_UPDATE_DATE = lAST_UPDATE_DATE;
	}

	public Integer getMODEL_VERSION() {
		return MODEL_VERSION;
	}

	public void setMODEL_VERSION(Integer mODEL_VERSION) {
		MODEL_VERSION = mODEL_VERSION;
	}

	public Integer getUI_VERSION() {
		return UI_VERSION;
	}

	public void setUI_VERSION(Integer uI_VERSION) {
		UI_VERSION = uI_VERSION;
	}

	public Boolean getIS_COMPLETE() {
		return IS_COMPLETE;
	}

	public void setIS_COMPLETE(Boolean iS_COMPLETE) {
		IS_COMPLETE = iS_COMPLETE;
	}

	public Date getSUBMISSION_DATE() {
		return SUBMISSION_DATE;
	}

	public void setSUBMISSION_DATE(Date sUBMISSION_DATE) {
		SUBMISSION_DATE = sUBMISSION_DATE;
	}

	public Date getMARKED_AS_COMPLETE_DATE() {
		return MARKED_AS_COMPLETE_DATE;
	}

	public void setMARKED_AS_COMPLETE_DATE(Date mARKED_AS_COMPLETE_DATE) {
		MARKED_AS_COMPLETE_DATE = mARKED_AS_COMPLETE_DATE;
	}

	public String getQS_11() {
		return QS_11;
	}

	public void setQS_11(String qS_11) {
		QS_11 = qS_11;
	}

	public String getQS_13() {
		return QS_13;
	}

	public void setQS_13(String qS_13) {
		QS_13 = qS_13;
	}

	public String getMONITORING_DATE() {
		return MONITORING_DATE;
	}

	public void setMONITORING_DATE(String mONITORING_DATE) {
		MONITORING_DATE = mONITORING_DATE;
	}

	public String getPHONE() {
		return PHONE;
	}

	public void setPHONE(String pHONE) {
		PHONE = pHONE;
	}

	public String getBG_STATE() {
		return BG_STATE;
	}

	public void setBG_STATE(String bG_STATE) {
		BG_STATE = bG_STATE;
	}

	public String getMETA_INSTANCE_ID() {
		return META_INSTANCE_ID;
	}

	public void setMETA_INSTANCE_ID(String mETA_INSTANCE_ID) {
		META_INSTANCE_ID = mETA_INSTANCE_ID;
	}

	public String getDISTRICT() {
		return DISTRICT;
	}

	public void setDISTRICT(String dISTRICT) {
		DISTRICT = dISTRICT;
	}

	public String getGRAMPANCHAYAT() {
		return GRAMPANCHAYAT;
	}

	public void setGRAMPANCHAYAT(String gRAMPANCHAYAT) {
		GRAMPANCHAYAT = gRAMPANCHAYAT;
	}

	public Double getGEOPOINT_ALT() {
		return GEOPOINT_ALT;
	}

	public void setGEOPOINT_ALT(Double gEOPOINT_ALT) {
		GEOPOINT_ALT = gEOPOINT_ALT;
	}

	public Double getGEOPOINT_LNG() {
		return GEOPOINT_LNG;
	}

	public void setGEOPOINT_LNG(Double gEOPOINT_LNG) {
		GEOPOINT_LNG = gEOPOINT_LNG;
	}

	public Double getGEOPOINT_LAT() {
		return GEOPOINT_LAT;
	}

	public void setGEOPOINT_LAT(Double gEOPOINT_LAT) {
		GEOPOINT_LAT = gEOPOINT_LAT;
	}

	public String getBG_COUNTRY() {
		return BG_COUNTRY;
	}

	public void setBG_COUNTRY(String bG_COUNTRY) {
		BG_COUNTRY = bG_COUNTRY;
	}

	public String getBLOCK() {
		return BLOCK;
	}

	public void setBLOCK(String bLOCK) {
		BLOCK = bLOCK;
	}

	public String getVILLAGE1() {
		return VILLAGE1;
	}

	public void setVILLAGE1(String vILLAGE1) {
		VILLAGE1 = vILLAGE1;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getVILLAGE() {
		return VILLAGE;
	}

	public void setVILLAGE(String vILLAGE) {
		VILLAGE = vILLAGE;
	}

	public String getQS_3() {
		return QS_3;
	}

	public void setQS_3(String qS_3) {
		QS_3 = qS_3;
	}

	public String getDATE() {
		return DATE;
	}

	public void setDATE(String dATE) {
		DATE = dATE;
	}

	public String getQS_1() {
		return QS_1;
	}

	public void setQS_1(String qS_1) {
		QS_1 = qS_1;
	}

	public String getQS_2() {
		return QS_2;
	}

	public void setQS_2(String qS_2) {
		QS_2 = qS_2;
	}

	public String getDEVICEID() {
		return DEVICEID;
	}

	public void setDEVICEID(String dEVICEID) {
		DEVICEID = dEVICEID;
	}

	public String getQS_9() {
		return QS_9;
	}

	public void setQS_9(String qS_9) {
		QS_9 = qS_9;
	}

	public String getQS_7() {
		return QS_7;
	}

	public void setQS_7(String qS_7) {
		QS_7 = qS_7;
	}

	public String getQS_8() {
		return QS_8;
	}

	public void setQS_8(String qS_8) {
		QS_8 = qS_8;
	}

	public Double getGEOPOINT_ACC() {
		return GEOPOINT_ACC;
	}

	public void setGEOPOINT_ACC(Double gEOPOINT_ACC) {
		GEOPOINT_ACC = gEOPOINT_ACC;
	}

	public String getQS_5() {
		return QS_5;
	}

	public void setQS_5(String qS_5) {
		QS_5 = qS_5;
	}

	public String getQS_6() {
		return QS_6;
	}

	public void setQS_6(String qS_6) {
		QS_6 = qS_6;
	}
	
}
