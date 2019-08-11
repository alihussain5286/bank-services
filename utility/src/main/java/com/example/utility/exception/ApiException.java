package com.example.utility.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiException extends Exception {

	private static final Logger logger= LoggerFactory.getLogger(ApiException.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String errorCode;
	private final String errorMessage;
	private final Exception exception;

	public ApiException(String errorCode) {
		logger.error("ApiExcption with ErrorrCode ::{}",errorCode);
		this.errorCode = errorCode;
		this.errorMessage = null;
		this.exception=null;
	}

	public ApiException(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.exception=null;
	}

	public ApiException(ApiException e) {
		logger.error("ApiExcption with ErrorrCode ::{}::ErrorMessage ::{}",e.getErrorCode(),e.getErrorMessage());
		this.exception=e;
		this.errorCode = e.getErrorCode();
		this.errorMessage = e.getErrorMessage();
	}
	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Exception getException() {
		return exception;
	}
}
