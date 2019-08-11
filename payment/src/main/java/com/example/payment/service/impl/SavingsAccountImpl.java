package com.example.payment.service.impl;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.payment.repository.SavingsAccountRepository;
import com.example.payment.repository.UserRepository;
import com.example.payment.service.AccountService;
import com.example.payment.service.StatementFeignClient;
import com.example.utility.Constant;
import com.example.utility.ErrorConstant;
import com.example.utility.entity.SavingsAccount;
import com.example.utility.entity.User;
import com.example.utility.exception.ApiException;
import com.example.utility.model.AccountType;
import com.example.utility.model.Request;
import com.example.utility.model.Response;
import com.example.utility.model.TransactionRequest;

@Service
public class SavingsAccountImpl implements AccountService {

	private static final Logger logger= LoggerFactory.getLogger(SavingsAccountImpl.class);

	@Autowired
	private SavingsAccountRepository savingsAccountRepository;

	@Autowired
	private StatementFeignClient statementFeignClient;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Response depositAmmount(HttpServletRequest httpRequest,Request request) throws ApiException {
		logger.info("<-----------Enter SavingsAccountImpl|depositAmmount-------------->");
		try {
			TransactionRequest transactionRequest = request.getTransactionRequest();
			SavingsAccount savingsAccount = savingsAccountRepository.findById(transactionRequest.getSavingsAccountId()).orElseThrow(()-> new RuntimeException("No data"));
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(BigDecimal.valueOf(transactionRequest.getAmount())));
			savingsAccountRepository.save(savingsAccount);
			transactionRequest.setDescription("Deposit to savings Account");
			transactionRequest.setType("Account");
			transactionRequest.setStatus("Finished");
			transactionRequest.setAvailableBalance(savingsAccount.getAccountBalance());
			statementFeignClient.saveTransaction(httpRequest.getHeader("Authorization"),request);
		}catch(Exception e) {
			logger.error("Exception in SavingsAccountImpl|depositAmmount::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		logger.info("<-----------Exit SavingsAccountImpl|depositAmmount-------------->");
		return new Response(Constant.SUCCESS);
	}

	@Override
	public Response withdrawAount(HttpServletRequest httpRequest,Request request) throws ApiException {
		logger.info("<-----------Enter SavingsAccountImpl|withdrawAount-------------->");
		try {
			TransactionRequest transactionRequest = request.getTransactionRequest();
			SavingsAccount savingsAccount = savingsAccountRepository.findById(transactionRequest.getSavingsAccountId()).orElseThrow(()-> new RuntimeException("No data"));
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(BigDecimal.valueOf(transactionRequest.getAmount())));
			savingsAccountRepository.save(savingsAccount);
			transactionRequest.setDescription("Withdraw from savings Account");
			transactionRequest.setType("Account");
			transactionRequest.setStatus("Completed");
			transactionRequest.setAvailableBalance(savingsAccount.getAccountBalance());
			statementFeignClient.saveTransaction(httpRequest.getHeader("Authorization"),request);
		}catch(Exception e) {
			logger.error("Exception in SavingsAccountImpl|withdrawAount::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		logger.info("<-----------Exit SavingsAccountImpl|withdrawAount-------------->");
		return new Response(Constant.SUCCESS);
	}

	@Override
	public String getType() {
		return AccountType.SAVINGS.toString();
	}

	@Override
	public Response createAccount(Response response) throws ApiException {
		logger.info("<-----------Enter SavingsAccountImpl|createAccount-------------->");
		try {
			SavingsAccount savingsAccount = new SavingsAccount();
			savingsAccount.setAccountBalance(BigDecimal.valueOf(0.0));
			Integer lastAccountNumber=savingsAccountRepository.getLastAccountNumber();
			savingsAccount.setAccountNumber(lastAccountNumber== null?100000000:lastAccountNumber+1);
			response.setSavingsAccount(savingsAccountRepository.save(savingsAccount));
		}catch(Exception e) {
			logger.error("Exception in SavingsAccountImpl|createAccount::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		logger.info("<-----------Exit SavingsAccountImpl|createAccount-------------->");
		return response;
	}

	@Override
	public Response checkBalanceAmount(Request request) throws ApiException {
		logger.info("<-----------Enter SavingsAccountImpl|checkBalanceAmount-------------->");
		Response response=null;
		try {
			User user= userRepository.findByUsername(request.getUserName());
			Double balanceAmount= Double.parseDouble(user.getSavingsAccount().getAccountBalance().toString());
			Boolean checkAmount= balanceAmount> request.getTransactionRequest().getAmount()?Boolean.TRUE:Boolean.FALSE;
			response = new Response(Constant.SUCCESS);
			response.setCheckAmount(checkAmount);
		}catch(Exception e) {
			logger.error("Exception in SavingsAccountImpl|checkBalanceAmount::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		logger.info("<-----------Exit SavingsAccountImpl|checkBalanceAmount-------------->");
		return response;
	}

}
