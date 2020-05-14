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

import org.springframework.data.annotation.CreatedDate;

/**
 * 
 * @author Subrata
 * Storing monthly expenditure data(only approved) for each NGOs.
 */

@Entity
@Table(name="raw_data_soe")
public class RawData implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="raw_data_soe_id_pk")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="ngo_id_fk", nullable = false)
	private NGO ngo;
	
	@Column(name = "created_date")
	@CreatedDate
	private Timestamp createdDate;
	
	@ManyToOne
	@JoinColumn(name="timeperiod_id_fk", nullable = false) 
	private TimePeriod timePeriod;
	
	@Column(name="opening_balance")
	private double openingBalance;
	
	@Column(name="total_balance")
	private double totalBalance;
	
	@Column(name="previous_month_amount_spent")
	private double previousMonthAmountSpent;
	
	@Column(name="current_month_amount_spent")
	private double currentMonthAmountSpent;
	
	@Column(name="cumulative_amount_spent")
	private double CumulativeAmountSpent;
	
	@Column(name="closing_balance")
	private double closingBalance;
	
	@Column(name="amount_received")
	private double amountReceived;
	
	@Column(name="physical_unit")
	private Integer physicalUnit;
	
	@Column(name="finance")
	private double finance;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public NGO getNgo() {
		return ngo;
	}

	public void setNgo(NGO ngo) {
		this.ngo = ngo;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}

	public double getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = openingBalance;
	}

	public double getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(double totalBalance) {
		this.totalBalance = totalBalance;
	}

	public double getPreviousMonthAmountSpent() {
		return previousMonthAmountSpent;
	}

	public void setPreviousMonthAmountSpent(double previousMonthAmountSpent) {
		this.previousMonthAmountSpent = previousMonthAmountSpent;
	}

	public double getCurrentMonthAmountSpent() {
		return currentMonthAmountSpent;
	}

	public void setCurrentMonthAmountSpent(double currentMonthAmountSpent) {
		this.currentMonthAmountSpent = currentMonthAmountSpent;
	}

	public double getCumulativeAmountSpent() {
		return CumulativeAmountSpent;
	}

	public void setCumulativeAmountSpent(double cumulativeAmountSpent) {
		CumulativeAmountSpent = cumulativeAmountSpent;
	}

	public double getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(double closingBalance) {
		this.closingBalance = closingBalance;
	}

	public double getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(double amountReceived) {
		this.amountReceived = amountReceived;
	}

	public Integer getPhysicalUnit() {
		return physicalUnit;
	}

	public void setPhysicalUnit(Integer physicalUnit) {
		this.physicalUnit = physicalUnit;
	}

	public double getFinance() {
		return finance;
	}

	public void setFinance(double finance) {
		this.finance = finance;
	}

}
