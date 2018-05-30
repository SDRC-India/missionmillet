package org.sdrc.missionmillet.model;

import java.util.Date;

/*
 *  @author Subham Ashish(subham@sdrc.co.in)
 */
public class YearAndMonthModel {

	private String previousMonth;

	private Integer previousYear;

	private String currentMonth;

	private String financialYear;
	
	private Date startDate;
	
	private Date endDate;
	
	private Date currentDate;
	
	private Integer month;
	
	private Integer year;
	


	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public String getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(String currentMonth) {
		this.currentMonth = currentMonth;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public String getPreviousMonth() {
		return previousMonth;
	}

	public void setPreviousMonth(String previousMonth) {
		this.previousMonth = previousMonth;
	}

	public Integer getPreviousYear() {
		return previousYear;
	}

	public void setPreviousYear(Integer previousYear) {
		this.previousYear = previousYear;
	}

}
