package org.sdrc.missionmillet.model;

import java.util.List;

import org.sdrc.missionmillet.domain.NGO;

public class CollectUserModel {

	private Integer userId;
	private String name;
	private String password;
	private String username;
	private String emailId;
	private boolean isLive;
	private String phoneNumber;
	private Integer typeOfUser;
	private NGO ngo;
	private Integer parentId;
	private List<UserAreaModel> userAreaModels;
	private Long userLoginMetaId;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getTypeOfUser() {
		return typeOfUser;
	}

	public void setTypeOfUser(Integer typeOfUser) {
		this.typeOfUser = typeOfUser;
	}

	public List<UserAreaModel> getUserAreaModels() {
		return userAreaModels;
	}

	public void setUserAreaModels(List<UserAreaModel> userAreaModels) {
		this.userAreaModels = userAreaModels;
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

	public Long getUserLoginMetaId() {
		return userLoginMetaId;
	}

	public void setUserLoginMetaId(Long userLoginMetaId) {
		this.userLoginMetaId = userLoginMetaId;
	}
	
}
