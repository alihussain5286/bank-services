package com.example.utility;

public class ErrorConstant {

	public static final String SERVICE_EXCEPTION="SERVICE_EXCEPTION";
	public static final String INVALID_RQUEST_DATA="INVALID_RQUEST_DATA";
	public static final String INVALID_ACCOUNT_TYPE="INVALID_ACCCOUNT_TYPE";
	public static final String USER_DOES_NOT_EXITS=" USER_DOES_NOT_EXITS";
	
	private ErrorConstant() {
		throw new IllegalArgumentException("Cannot Instantitate ErrorConstant");
	}
}
