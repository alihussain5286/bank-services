package com.example.payment.config;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.utility.ErrorConstant;
import com.example.utility.Util;
import com.example.utility.exception.ApiException;
import com.example.utility.exception.ErrorInfo;

@Component
@ControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

	@Autowired
	protected MessageSource messageSource;

	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	public Object handleException(HttpServletRequest request, Exception ex) {
		LOGGER.error("Final Exception log::{}",ex);
		return new ErrorInfo(ErrorConstant.SERVICE_EXCEPTION,Util.getMessageByLocale(ErrorConstant.SERVICE_EXCEPTION, messageSource));
	}

	@ExceptionHandler({ ApiException.class })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	public Object handleCustomException(HttpServletRequest request, ApiException ex) {
		LOGGER.error("Final ApiException log::{}",ex);
		return new ErrorInfo(ex.getErrorCode(), Util.getMessageByLocale(ErrorConstant.SERVICE_EXCEPTION, messageSource));
	}
}