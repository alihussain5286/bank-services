package com.example.statement.service.impl;

import java.time.LocalDateTime;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.statement.repository.SavingsAccountRepository;
import com.example.statement.repository.SavingsTransactionRepository;
import com.example.statement.repository.UserRepository;
import com.example.statement.service.StatementService;
import com.example.utility.Constant;
import com.example.utility.ErrorConstant;
import com.example.utility.entity.SavingsAccount;
import com.example.utility.entity.SavingsTransaction;
import com.example.utility.entity.User;
import com.example.utility.exception.ApiException;
import com.example.utility.model.AccountType;
import com.example.utility.model.Request;
import com.example.utility.model.Response;
import com.example.utility.model.TransactionRequest;

import ch.qos.logback.classic.Logger;

@Service
public class SavingsStatementServiceImpl implements StatementService {

	private static final Logger logger= (Logger) LoggerFactory.getLogger(SavingsStatementServiceImpl.class);

	@Autowired
	private SavingsTransactionRepository savingsTransactionRepository;

	@Autowired
	private SavingsAccountRepository savingsAccountRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Object getTransactionsList(Request request) throws ApiException {
		logger.info("-----------------Enter SavingsStatementServiceImpl| getTransactionsList -------------");
		Response reponse = new Response();
		try {
			User user=userRepository.findByUsername(request.getUserName());
			reponse.setSavingsTransactionList(user.getSavingsAccount().getSavingsTransactionList());
			reponse.setStatus(Constant.SUCCESS);
		}catch(Exception e) {
			logger.error("Exception in PrimaryStatementServiceImpl| getTransactionsList::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		logger.info("<-----------Exit PrimaryStatementServiceImpl| getTransactionsList-------------->");
		return reponse;
	}

	@Override
	public Object saveTransaction(Request request) throws ApiException {
		logger.info("-----------------Enter SavingsStatementServiceImpl| saveTransaction -------------");
		try {
		TransactionRequest transactionRequest=request.getTransactionRequest();
		SavingsAccount savingsAccount= savingsAccountRepository.findById(transactionRequest.getSavingsAccountId()).orElseThrow(()-> new RuntimeException("No Data Found"));
		SavingsTransaction savingsTransaction = new SavingsTransaction(LocalDateTime.now(), transactionRequest.getDescription(), transactionRequest.getType(), transactionRequest.getStatus(), transactionRequest.getAmount(), savingsAccount.getAccountBalance(), savingsAccount);
		savingsTransactionRepository.save(savingsTransaction);
		}catch(Exception e) {
			logger.error("Exception in SavingsStatementServiceImpl| saveTransaction::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		logger.info("<-----------Exit SavingsStatementServiceImpl| saveTransaction-------------->");
		return new Response(Constant.SUCCESS);
	}

	@Override
	public String getType() {
		return AccountType.SAVINGS.toString();
	}

}
