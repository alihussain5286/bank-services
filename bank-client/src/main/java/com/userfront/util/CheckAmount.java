package com.userfront.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import com.example.utility.EncryptUtil;
import com.example.utility.exception.ApiException;
import com.example.utility.model.AccountType;
import com.example.utility.model.Request;
import com.example.utility.model.RequestType;
import com.example.utility.model.Response;
import com.example.utility.model.TransactionRequest;
import com.userfront.service.RestService;

public class CheckAmount implements  ConstraintValidator<AmountValidator, Object> {

	private static final Logger logger = LoggerFactory.getLogger(CheckAmount.class);

	private RestService restService;

	public CheckAmount(RestService restService) {
		this.restService = restService;
	}

	@Override
	public void initialize(AmountValidator constraintAnnotation) { 
		//nothing to initialize
	}

	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context){   
		Boolean isValid= Boolean.FALSE;
		try {
			Request request= new Request();
			CheckAmountRequest checkAmount = (CheckAmountRequest) obj ;
			isValid =checkGeneralValidation(checkAmount,context);
			if(isValid) {
				TransactionRequest transRequest= new TransactionRequest(checkAmount.getAccountType());
				transRequest.setAmount(checkAmount.getAmount());
				request.setTransactionRequest(transRequest);
				request.setUserName(EncryptUtil.decrypt(checkAmount.getUserName()));
				Response response =restService.getRestResponse(request, RequestType.DEFAULT, HttpMethod.POST, "/payment/checkBalanceAmount");
				isValid=response.getCheckAmount();
				if(!isValid){
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
					.addPropertyNode( "amount" ).addConstraintViolation();
				}
			}
		}catch(ApiException e) {
			logger.error("Exception in CheckAmount|isValid ::{}",e);
		}
		return isValid;
	}

	private Boolean checkGeneralValidation(CheckAmountRequest checkAmount, ConstraintValidatorContext context) {
		if(!(checkAmount.getAccountType() != null && (checkAmount.getAccountType().contains(AccountType.PRIMARY.toString()) ||checkAmount.getAccountType().contains(AccountType.SAVINGS.toString())))){
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
			.addPropertyNode( "accountType" ).addConstraintViolation();
			return Boolean.FALSE;
		}

		if(checkAmount.getAmount() == 0) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Please enter correct Account Type!")
			.addPropertyNode( "amount" ).addConstraintViolation();
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}
