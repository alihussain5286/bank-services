package com.userfront.service.impl;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.example.utility.ErrorConstant;
import com.example.utility.entity.PrimaryTransaction;
import com.example.utility.entity.SavingsTransaction;
import com.example.utility.exception.ApiException;
import com.example.utility.model.AccountType;
import com.example.utility.model.Request;
import com.example.utility.model.RequestType;
import com.example.utility.model.Response;
import com.example.utility.model.TransactionRequest;
import com.userfront.service.RestService;
import com.userfront.service.TransactionService;

import ch.qos.logback.classic.Logger;

@Service
public class TransactionServiceImpl implements TransactionService {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(TransactionServiceImpl.class);

	@Autowired
	private RestService restService;

	public List<PrimaryTransaction> findPrimaryTransactionList(String username) throws ApiException{
		List<PrimaryTransaction> primaryTransactionList=null;
		try {
			logger.info("<---------------Enter findPrimaryTransactionList----------------------->");
			Request request = new Request();
			request.setUserName(username);
			request.setTransactionRequest(new TransactionRequest(AccountType.PRIMARY.toString()));
			Response response=restService.getRestResponse(request, RequestType.DEFAULT, HttpMethod.POST, "/statement/getTransactions");
			primaryTransactionList =response.getPrimaryTransactionList();
			logger.info("<---------------Exit findPrimaryTransactionList----------------------->");
		}catch(Exception e) {
			logger.error("Excpetion in findPrimaryTransactionList{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		return primaryTransactionList;
	}

	public List<SavingsTransaction> findSavingsTransactionList(String username)throws ApiException {
		logger.info("<---------------Enter findSavingsTransactionList----------------------->");
		List<SavingsTransaction> savingsTransactionList=null;
		try {
			Request request = new Request();
			request.setUserName(username);
			request.setTransactionRequest(new TransactionRequest(AccountType.SAVINGS.toString()));
			Response response=restService.getRestResponse(request, RequestType.DEFAULT, HttpMethod.POST, "/statement/getTransactions");
			savingsTransactionList =response.getSavingsTransactionList();
			logger.info("<---------------Exit findPrimaryTransactionList----------------------->");
		}catch(Exception e) {
			logger.error("Exception in findSavingsTransactionList{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		return savingsTransactionList;
	}
}
