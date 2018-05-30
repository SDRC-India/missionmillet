package org.sdrc.missionmillet.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Subrata
 * This Entity class will keep all the User details
 * 
 * @since version 1.0.0.0
 *
 */

@Entity
@Table(name="mst_collect_user")
public class CollectUser implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id_pk")
	private Integer userId;
	
	@Column(name="name", nullable = false,length = 60)
	private String name;
	
	@Column(name="user_name", nullable = false, unique = true,length = 50)
	private String username;
	
	@Column(name="password", nullable = false,length = 40)
	private String password;
	
	@Column(name="email_id",length = 60)
	private String emailId;
	
	@Column(name="is_live")
	private boolean isLive;
	
	@Column(name="is_locked")
	private boolean isLocked;
	
	@Column(name="created_by", length = 60)
	private String createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;
	
	@Column(name="updated_by",length = 60)
	private String updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;
	
	@OneToMany(mappedBy="collectUser", fetch = FetchType.EAGER)
	private List<UserAreaMapping> userAreaMappings;
	
	@OneToMany(mappedBy="collectUser")
	private List<User_Program_XForm_Mapping> user_Program_XForm_Mappings;
	
	@OneToOne
	@JoinColumn(name="ngo_id_fk")
	private NGO ngo;
	
	@Column(name="type_of_user")
	private Integer typeOfUser;
	
	@Column(name="parent_id")
	private Integer parentId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
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

	public List<UserAreaMapping> getUserAreaMappings() {
		return userAreaMappings;
	}

	public void setUserAreaMappings(List<UserAreaMapping> userAreaMappings) {
		this.userAreaMappings = userAreaMappings;
	}

	public List<User_Program_XForm_Mapping> getUser_Program_XForm_Mappings() {
		return user_Program_XForm_Mappings;
	}

	public void setUser_Program_XForm_Mappings(
			List<User_Program_XForm_Mapping> user_Program_XForm_Mappings) {
		this.user_Program_XForm_Mappings = user_Program_XForm_Mappings;
	}

	public Integer getTypeOfUser() {
		return typeOfUser;
	}

	public void setTypeOfUser(Integer typeOfUser) {
		this.typeOfUser = typeOfUser;
	}

	public CollectUser(Integer userId) {
		this.userId = userId;
	}
	public CollectUser() {
		
	}

	public NGO getNgo() {
		return ngo;
	}

	public void setNgo(NGO ngo) {
		this.ngo = ngo;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
}
