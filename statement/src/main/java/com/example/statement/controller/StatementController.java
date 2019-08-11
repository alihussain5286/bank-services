package com.example.statement.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.statement.service.StatementService;
import com.example.statement.service.impl.StatementFactory;
import com.example.utility.Constant;
import com.example.utility.ErrorConstant;
import com.example.utility.exception.ApiException;
import com.example.utility.model.Request;
import com.example.utility.model.Response;

@RestController
public class StatementController {
	private static final Logger logger= LoggerFactory.getLogger(StatementController.class);

	private StatementService statementService;

	@PostMapping("/getTransactions")
	public Response getTransactionList(@RequestBody @Valid Request request,BindingResult bindingResult) throws ApiException {
		logger.info("<-------------------------------StatementController| getTransactionList--------------------->");
		if(bindingResult.hasErrors()) {
			throw new ApiException(ErrorConstant.INVALID_RQUEST_DATA);
		}
		statementService= StatementFactory.getService(request.getTransactionRequest().getAccountType());
		return (Response) statementService.getTransactionsList(request);
	}


	@PostMapping("/saveTransaction")
	public Response saveTransaction(@RequestBody @Valid Request request,BindingResult bindingResult) throws ApiException {
		logger.info("<-------------------------------StatementController| saveTransaction--------------------->");
		if(bindingResult.hasErrors()) {
			throw new ApiException(ErrorConstant.INVALID_RQUEST_DATA);
		}
		statementService= StatementFactory.getService(request.getTransactionRequest().getAccountType());
		statementService.saveTransaction(request);
		return new Response(Constant.SUCCESS);
	}

}
