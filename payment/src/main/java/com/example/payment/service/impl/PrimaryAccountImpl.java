package com.example.payment.service.impl;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.payment.repository.PrimaryAccountRepository;
import com.example.payment.repository.UserRepository;
import com.example.payment.service.AccountService;
import com.example.payment.service.StatementFeignClient;
import com.example.utility.Constant;
import com.example.utility.ErrorConstant;
import com.example.utility.entity.PrimaryAccount;
import com.example.utility.entity.User;
import com.example.utility.exception.ApiException;
import com.example.utility.model.AccountType;
import com.example.utility.model.Request;
import com.example.utility.model.Response;
import com.example.utility.model.TransactionRequest;

@Service
public class PrimaryAccountImpl implements AccountService {

	private static final Logger logger= LoggerFactory.getLogger(PrimaryAccountImpl.class);

	@Autowired
	private PrimaryAccountRepository primaryAccountRepository;

	@Autowired
	private StatementFeignClient statementFeignClient;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Response depositAmmount(HttpServletRequest httpRequest,Request request) throws ApiException {
		logger.info("<-----------Enter PrimaryAccountServiceImpl|depositAmount-------------->");
		try {
			TransactionRequest transactionRequest = request.getTransactionRequest();
			PrimaryAccount primaryAccount= primaryAccountRepository.findById(transactionRequest.getPrimaryAccountId()).orElseThrow(()-> new RuntimeException("No data"));
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(BigDecimal.valueOf(transactionRequest.getAmount())));
			primaryAccountRepository.save(primaryAccount);
			transactionRequest.setDescription( "Deposit to Primary Account");
			transactionRequest.setType("Account");
			transactionRequest.setStatus("Finished");
			transactionRequest.setAvailableBalance(primaryAccount.getAccountBalance());
			request.setTransactionRequest(transactionRequest);
			logger.info("<-----------Calling Statement FeignClient Started-------------->");
			statementFeignClient.saveTransaction(httpRequest.getHeader("Authorization"),request);
		}catch(Exception e) {
			logger.error("Exception in PrimaryAccountServiceImpl|depositAmmount::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		logger.info("<-----------Exit PrimaryAccountServiceImpl|depositAmount-------------->");
		return new Response("success");
	}



	@Override
	public Response withdrawAount(HttpServletRequest httpRequest,Request request) throws ApiException {
		logger.info("<-----------Enter PrimaryAccountServiceImpl|withdrawAount-------------->");
		try {
			TransactionRequest transactionRequest = request.getTransactionRequest();
			PrimaryAccount primaryAccount= primaryAccountRepository.findById(transactionRequest.getPrimaryAccountId()).orElseThrow(()-> new RuntimeException("No data"));
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(BigDecimal.valueOf(transactionRequest.getAmount())));
			primaryAccountRepository.save(primaryAccount);
			transactionRequest.setDescription( "Withdrawn from Primary Account");
			transactionRequest.setType("Account");
			transactionRequest.setStatus("Completed");
			transactionRequest.setAvailableBalance(primaryAccount.getAccountBalance());
			logger.info("<-----------Calling Statement FeignClient Started-------------->");
			statementFeignClient.saveTransaction(httpRequest.getHeader("Authorization"),request);
		}catch(Exception e) {
			logger.error("Exception in PrimaryAccountServiceImpl|withdrawAount::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		logger.info("<-----------Exit PrimaryAccountServiceImpl|withdrawAount-------------->");
		return  new Response("success");
	}

	@Override
	public String getType() {
		return AccountType.PRIMARY.toString();
	}

	@Override
	public Response createAccount(Response response) throws ApiException {
		logger.info("<-----------Enter PrimaryAccountServiceImpl|createAccount-------------->");
		try {
			PrimaryAccount primaryAccount = new PrimaryAccount();
			primaryAccount.setAccountBalance(BigDecimal.valueOf(0.0));
			Integer lastAccountNumber=primaryAccountRepository.getLastAccountNumber();
			primaryAccount.setAccountNumber(lastAccountNumber == null ? 10000000:lastAccountNumber+1);
			response.setPrimaryAccount(primaryAccountRepository.save(primaryAccount));
		}catch(Exception e) {
			logger.error("Exception in PrimaryAccountServiceImpl|createAccount::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		logger.info("<-----------Exit PrimaryAccountServiceImpl|createAccount-------------->");
		return response;
	}



	@Override
	public Response checkBalanceAmount(Request request) throws ApiException {
		Response response=null;
		logger.info("<-----------Enter PrimaryAccountServiceImpl|checkBalanceAmount-------------->");
		try {
			User user= userRepository.findByUsername(request.getUserName());
			Double balanceAmount= Double.parseDouble(user.getPrimaryAccount().getAccountBalance().toString());
			Boolean checkAmount= balanceAmount> request.getTransactionRequest().getAmount()?Boolean.TRUE:Boolean.FALSE;
			response = new Response(Constant.SUCCESS);
			response.setCheckAmount(checkAmount);
		}catch(Exception e) {
			logger.error("Exception in PrimaryAccountServiceImpl|checkBalanceAmount::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		logger.info("<-----------Exit PrimaryAccountServiceImpl|checkBalanceAmount-------------->");
		return response;
	}

}
