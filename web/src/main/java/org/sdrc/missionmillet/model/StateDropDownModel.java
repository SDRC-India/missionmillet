package org.sdrc.missionmillet.model;

/*
 *  @author Subham Ashish(subham@sdrc.co.in)
 */
import java.util.List;

public class StateDropDownModel {

	List<TypeDetailsModel> typeDetailsModel;

	List<String> monthDetails;

	public List<TypeDetailsModel> getTypeDetailsModel() {
		return typeDetailsModel;
	}

	public void setTypeDetailsModel(List<TypeDetailsModel> typeDetailsModel) {
		this.typeDetailsModel = typeDetailsModel;
	}

	public List<String> getMonthDetails() {
		return monthDetails;
	}

	public void setMonthDetails(List<String> monthDetails) {
		this.monthDetails = monthDetails;
	}

}
