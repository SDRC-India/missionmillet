package org.sdrc.missionmillet.model;

import java.util.Map;

public class UserProgramXFormMappingModel {
	private Integer userId;
	private String username;
	private String email;
	private Map<Integer,ProgramXFormsModel> programWithXFormsModel;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Map<Integer, ProgramXFormsModel> getProgramWithXFormsModel() {
		return programWithXFormsModel;
	}
	public void setProgramWithXFormsModel(
			Map<Integer, ProgramXFormsModel> programWithXFormsModel) {
		this.programWithXFormsModel = programWithXFormsModel;
	}
	
}
