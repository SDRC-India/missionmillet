package org.sdrc.missionmillet.model;

import java.util.List;

public class ModelToCollectApplication {
	private List<ProgramXFormsModel> programXFormModelList;
	private List<MediaFilesToUpdate> listOfMediaFilesToUpdate;

	public List<ProgramXFormsModel> getProgramXFormModelList() {
		return programXFormModelList;
	}
	public void setProgramXFormModelList(
			List<ProgramXFormsModel> programXFormModelList) {
		this.programXFormModelList = programXFormModelList;
	}
	public List<MediaFilesToUpdate> getListOfMediaFilesToUpdate() {
		return listOfMediaFilesToUpdate;
	}
	public void setListOfMediaFilesToUpdate(
			List<MediaFilesToUpdate> listOfMediaFilesToUpdate) {
		this.listOfMediaFilesToUpdate = listOfMediaFilesToUpdate;
	}
}
