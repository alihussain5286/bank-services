package com.example.statement.service.impl;

import java.time.LocalDateTime;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.statement.repository.PrimaryAccountRepository;
import com.example.statement.repository.PrimaryTransactionRepository;
import com.example.statement.repository.UserRepository;
import com.example.statement.service.StatementService;
import com.example.utility.Constant;
import com.example.utility.ErrorConstant;
import com.example.utility.entity.PrimaryAccount;
import com.example.utility.entity.PrimaryTransaction;
import com.example.utility.entity.User;
import com.example.utility.exception.ApiException;
import com.example.utility.model.AccountType;
import com.example.utility.model.Request;
import com.example.utility.model.Response;
import com.example.utility.model.TransactionRequest;

import ch.qos.logback.classic.Logger;

@Service
public class PrimaryStatementServiceImpl implements StatementService{

	private static final Logger logger= (Logger) LoggerFactory.getLogger(PrimaryStatementServiceImpl.class);

	@Autowired
	private PrimaryAccountRepository primaryAccountRepository;

	@Autowired
	private PrimaryTransactionRepository primaryTransactionRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Object getTransactionsList(Request request) throws ApiException {
		logger.info("-----------------Enter PrimaryStatementServiceImpl| getTransactionsList -------------");
		Response reponse = new Response();
		try {
			User user=userRepository.findByUsername(request.getUserName());
			reponse.setPrimaryTransactionList(user.getPrimaryAccount().getPrimaryTransactionList());
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
		logger.info("-----------------Enter PrimaryStatementServiceImpl| saveTransaction -------------");
		try {
			TransactionRequest transactionRequest=request.getTransactionRequest();
			PrimaryAccount primaryAccount= primaryAccountRepository.findById(transactionRequest.getPrimaryAccountId()).orElseThrow(()-> new RuntimeException("No Data Found"));
			PrimaryTransaction primaryTransaction = new PrimaryTransaction(LocalDateTime.now(), transactionRequest.getDescription(), transactionRequest.getType(), transactionRequest.getStatus(), transactionRequest.getAmount(), primaryAccount.getAccountBalance(), primaryAccount);
			primaryTransactionRepository.save(primaryTransaction);
		}catch(Exception e) {
			logger.error("Exception in SavingsStatementServiceImpl| saveTransaction::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		logger.info("<-----------Exit SavingsStatementServiceImpl| saveTransaction-------------->");
		return new Response(Constant.SUCCESS);
	}

	@Override
	public String getType() {
		return AccountType.PRIMARY.toString();
	}


}
