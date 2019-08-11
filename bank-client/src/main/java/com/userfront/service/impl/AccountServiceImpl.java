package com.userfront.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.example.utility.ErrorConstant;
import com.example.utility.entity.User;
import com.example.utility.exception.ApiException;
import com.example.utility.model.AccountType;
import com.example.utility.model.Request;
import com.example.utility.model.RequestType;
import com.example.utility.model.Response;
import com.example.utility.model.TransactionRequest;
import com.userfront.service.AccountService;
import com.userfront.service.RestService;
import com.userfront.service.UserService;
import com.userfront.util.CheckAmountRequest;

@Service
public class AccountServiceImpl implements AccountService {

	private static final Logger logger= LoggerFactory.getLogger(AccountServiceImpl.class);

	@Autowired
	private UserService userService;

	@Autowired
	private RestService restService;

	@Override
	public Response createAccount() throws ApiException {
		logger.info("<-----------Enter AccountServiceImpl |createAccount-------------->");
		return restService.getRestResponse(new Request(), RequestType.DEFAULT, HttpMethod.GET, "/payment/createAccount");
	}

	public Response deposit(TransactionRequest transactionRequest, String userName) throws ApiException {
		logger.info("<-----------Exit AccountServiceImpl|deposit-------------->");
		Response response=null;
		try {
			User user = userService.findByUsername(userName);
			if(user !=null) {
				Request request = new Request();
				checkAndAssignId(transactionRequest.getAccountType(),transactionRequest,user);
				request.setTransactionRequest(transactionRequest);
				response=restService.getRestResponse(request, RequestType.DEFAULT, HttpMethod.POST, "/payment/depositAmount");
			}else {
				logger.error("User Doesnot Exists");
				throw new ApiException(ErrorConstant.USER_DOES_NOT_EXITS);
			}
		}catch(ApiException e) {
			logger.error("ApiException in AccountServiceImpl|deposit::{}",e.getErrorCode());
			throw new ApiException(e);
		}catch(Exception e) {
			logger.error("Exception in AccountServiceImpl|deposit::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		logger.info("<-----------Exit AccountServiceImpl|deposit-------------->");
		return response;
	}

	private void checkAndAssignId(String accountType, TransactionRequest transactionRequest,User user) throws ApiException {
		logger.info("<--------------Enter checkAndAssignId --------------------------->");
		if(AccountType.PRIMARY.name().equalsIgnoreCase(accountType)) {
			transactionRequest.setPrimaryAccountId(user.getPrimaryAccount().getId());
		}else if(AccountType.SAVINGS.name().equalsIgnoreCase(accountType)) {
			transactionRequest.setSavingsAccountId(user.getSavingsAccount().getId());
		}else {
			logger.error("Invalid AccountType");
			throw new ApiException(ErrorConstant.INVALID_ACCOUNT_TYPE);
		}
		logger.info("<--------------Exit checkAndAssignId --------------------------->");
	}

	public Response withdraw(CheckAmountRequest checkAmountRequest, String userName) throws ApiException {
		logger.info("<-----------Exit AccountServiceImpl|withdraw-------------->");
		Response response=null;
		try {
			User user = userService.findByUsername(userName);
			if(user !=null) {
				Request request = new Request();
				TransactionRequest transactionRequest= new TransactionRequest(checkAmountRequest.getAccountType());
				checkAndAssignId(checkAmountRequest.getAccountType(),transactionRequest,user);
				transactionRequest.setAmount(checkAmountRequest.getAmount());
				request.setTransactionRequest(transactionRequest);
				response=restService.getRestResponse(request, RequestType.DEFAULT, HttpMethod.POST, "/payment/withdrawAmount");
			}else {
				logger.error("User Doesnot Exists");
				throw new ApiException(ErrorConstant.USER_DOES_NOT_EXITS);
			}
		}catch(ApiException e) {
			logger.error("ApiExcpetion in AccountServiceImpl|withdraw::{}",e);
			throw new ApiException(e);
		}catch(Exception e) {
			logger.error("Exception in AccountServiceImpl|withdraw::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		logger.info("<-----------Exit AccountServiceImpl|withdraw-------------->");
		return response;
	}
}
