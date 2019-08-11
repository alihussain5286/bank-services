package com.userfront.util;

import javax.validation.constraints.NotNull;

import com.example.utility.validation.ValidationGroups.CheckAmountValidation;

@AmountValidator(groups = {CheckAmountValidation.class})
public class CheckAmountRequest {

	@NotNull(groups= {CheckAmountValidation.class},message="Please enter correct amount!")
	private double amount ;
	
	@NotNull(groups= {CheckAmountValidation.class},message="Please select a account type!")
	private String accountType;
	
	private String userName;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
