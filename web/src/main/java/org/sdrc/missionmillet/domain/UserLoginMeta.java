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
 * @author Subrata
 *	Contains all the meta information about the user login - logout.
 */
@Entity
@Table(name="user_login_meta")
public class UserLoginMeta implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_log_in_meta_id_pk")
	private Long userLogInMetaId;
	
	@Column(name="logged_in_datetime")
	private Timestamp loggedInDateTime;
	
	@Column(name="logged_out_datetime")
	private Timestamp loggedOutDateTime;
	
	@Column(name="user_ip_address",length=30)
	private String userIpAddress;
	
	@Column(name="user_agent",length=255)
	private String userAgent;
	
	@Column(name="is_logged_in")
	private boolean isLoggedIn;
	
	@ManyToOne
	@JoinColumn(name="user_id_fk")
	private CollectUser collectUser;
	
	public Long getUserLogInMetaId() {
		return userLogInMetaId;
	}

	public Timestamp getLoggedInDateTime() {
		return loggedInDateTime;
	}

	public Timestamp getLoggedOutDateTime() {
		return loggedOutDateTime;
	}

	public String getUserIpAddress() {
		return userIpAddress;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserLogInMetaId(Long userLogInMetaId) {
		this.userLogInMetaId = userLogInMetaId;
	}

	public void setLoggedInDateTime(Timestamp loggedInDateTime) {
		this.loggedInDateTime = loggedInDateTime;
	}

	public void setLoggedOutDateTime(Timestamp loggedOutDateTime) {
		this.loggedOutDateTime = loggedOutDateTime;
	}

	public void setUserIpAddress(String userIpAddress) {
		this.userIpAddress = userIpAddress;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public UserLoginMeta(long userLogInMetaId) {
		super();
		this.userLogInMetaId = userLogInMetaId;
	}

	public UserLoginMeta() {
		super();
	}
	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public CollectUser getCollectUser() {
		return collectUser;
	}

	public void setCollectUser(CollectUser collectUser) {
		this.collectUser = collectUser;
	}

}