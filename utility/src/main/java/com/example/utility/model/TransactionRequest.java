/**
 * 
 */
package com.example.utility.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.example.utility.validation.ValidationGroups.GeneralValidation;

/**
 * @author admin
 *
 */
public class TransactionRequest {

	@NotNull(groups= {GeneralValidation.class} ,message = "Please enter proper amount value")
	private double amount ;
	private Long primaryAccountId; 
	private Long savingsAccountId;
	private String status;
	private String description;
	@NotNull(groups= {GeneralValidation.class},message="Please select a account type!")
	private String accountType;
	private String type;
	private BigDecimal availableBalance;


	public TransactionRequest() {

	}

	public TransactionRequest(String accountType) {
		this.accountType=accountType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Long getPrimaryAccountId() {
		return primaryAccountId;
	}
	public void setPrimaryAccountId(Long primaryAccountId) {
		this.primaryAccountId = primaryAccountId;
	}
	public Long getSavingsAccountId() {
		return savingsAccountId;
	}
	public void setSavingsAccountId(Long savingsAccountId) {
		this.savingsAccountId = savingsAccountId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
}
