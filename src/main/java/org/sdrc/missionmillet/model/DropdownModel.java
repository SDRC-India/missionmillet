package org.sdrc.missionmillet.model;

import java.util.List;

public class DropdownModel {

	private List<TypeDetailsModel> typeDetailsModel;

	private List<ValueObject> timePeriodList;

	public List<ValueObject> getTimePeriodList() {
		return timePeriodList;
	}

	public void setTimePeriodList(List<ValueObject> timePeriodList) {
		this.timePeriodList = timePeriodList;
	}

	public List<TypeDetailsModel> getTypeDetailsModel() {
		return typeDetailsModel;
	}

	public void setTypeDetailsModel(List<TypeDetailsModel> typeDetailsModel) {
		this.typeDetailsModel = typeDetailsModel;
	}
}
