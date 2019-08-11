/**
 * 
 */
package com.example.utility.model;

/**
 * @author admin
 *
 */
public class Request {
	
	private String userName;

	private TransactionRequest transactionRequest;
	
	public Request() {
		//Default Constructor
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Request(TransactionRequest transactionRequest){
		this.transactionRequest=transactionRequest;
	}

	public TransactionRequest getTransactionRequest() {
		return transactionRequest;
	}

	public void setTransactionRequest(TransactionRequest transactionRequest) {
		this.transactionRequest = transactionRequest;
	}

}
