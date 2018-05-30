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

import org.hibernate.annotations.CreationTimestamp;


@Entity
@Table(name="txn_change_password")
public class TXNChangePassword implements Serializable {

	private static final long serialVersionUID = 7689465322051863525L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="txn_change_password_id_pk")
	private Integer txnChangePasswordId;
	
	@ManyToOne
	@JoinColumn(name="sub_user_id_fk", nullable = false)
	private CollectUser collectUser;
	
	@ManyToOne
	@JoinColumn(name="changed_by_user_id_fk", nullable = false)
	private CollectUser user;
	
	@Column(name="user_ip", nullable = false,length=30)
	private String userIp;
	
	@Column(name="password_updated_date", nullable = false)
	private Timestamp passwordUpdatedDate;
	
	@Column(name="is_live")
	private boolean isLive;
	
	@Column(name="created_by",length=60)
	private String createdBy;

	@CreationTimestamp
	@Column(name="created_date")
	private Timestamp createdDate;

	public Integer getTxnChangePasswordId() {
		return txnChangePasswordId;
	}

	public void setTxnChangePasswordId(Integer txnChangePasswordId) {
		this.txnChangePasswordId = txnChangePasswordId;
	}

	public CollectUser getCollectUser() {
		return collectUser;
	}

	public void setCollectUser(CollectUser collectUser) {
		this.collectUser = collectUser;
	}

	public CollectUser getUser() {
		return user;
	}

	public void setUser(CollectUser user) {
		this.user = user;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public Timestamp getPasswordUpdatedDate() {
		return passwordUpdatedDate;
	}

	public void setPasswordUpdatedDate(Timestamp passwordUpdatedDate) {
		this.passwordUpdatedDate = passwordUpdatedDate;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
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

}
