package com.example.utility.model;

import java.util.List;

import com.example.utility.entity.PrimaryAccount;
import com.example.utility.entity.PrimaryTransaction;
import com.example.utility.entity.SavingsAccount;
import com.example.utility.entity.SavingsTransaction;


public class Response {

	private String status;
	
	private List<PrimaryTransaction> primaryTransactionList;
	
	private List<SavingsTransaction> savingsTransactionList;
	
	private PrimaryAccount primaryAccount;
	
	private SavingsAccount savingsAccount;
	
	private	Boolean checkAmount;

	public Response() {
		super();
	}

	public Response(String status) {
		this.status=status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<PrimaryTransaction> getPrimaryTransactionList() {
		return primaryTransactionList;
	}

	public void setPrimaryTransactionList(List<PrimaryTransaction> primaryTransactionList) {
		this.primaryTransactionList = primaryTransactionList;
	}

	public List<SavingsTransaction> getSavingsTransactionList() {
		return savingsTransactionList;
	}

	public void setSavingsTransactionList(List<SavingsTransaction> savingsTransactionList) {
		this.savingsTransactionList = savingsTransactionList;
	}

	public PrimaryAccount getPrimaryAccount() {
		return primaryAccount;
	}

	public void setPrimaryAccount(PrimaryAccount primaryAccount) {
		this.primaryAccount = primaryAccount;
	}

	public SavingsAccount getSavingsAccount() {
		return savingsAccount;
	}

	public void setSavingsAccount(SavingsAccount savingsAccount) {
		this.savingsAccount = savingsAccount;
	}

	public Boolean getCheckAmount() {
		return checkAmount;
	}

	public void setCheckAmount(Boolean checkAmount) {
		this.checkAmount = checkAmount;
	}

}
