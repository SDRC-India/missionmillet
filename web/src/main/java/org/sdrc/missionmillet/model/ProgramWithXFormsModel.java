package org.sdrc.missionmillet.model;

import java.util.List;

public class ProgramWithXFormsModel {
	private ProgramModel programModel;
	private List<XFormModel> xFormsModel;

	public ProgramModel getProgramModel() {
		return programModel;
	}

	public void setProgramModel(ProgramModel programModel) {
		this.programModel = programModel;
	}

	public List<XFormModel> getxFormsModel() {
		return xFormsModel;
	}

	public void setxFormsModel(List<XFormModel> xFormsModel) {
		this.xFormsModel = xFormsModel;
	}
}
