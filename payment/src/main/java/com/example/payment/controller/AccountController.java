package com.example.payment.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.payment.service.AccountService;
import com.example.payment.service.impl.AccountFactory;
import com.example.utility.Constant;
import com.example.utility.ErrorConstant;
import com.example.utility.exception.ApiException;
import com.example.utility.model.Request;
import com.example.utility.model.Response;

@RestController
public class AccountController {

	private static final Logger logger= LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private List<AccountService> services;

	@PostMapping("/depositAmount")
	public Response deposit(HttpServletRequest httpRequest,@RequestBody @Valid Request request, BindingResult bindingResult) throws ApiException {
		logger.info("<-------------------------------AccountController| deposit--------------------->");
		if(bindingResult.hasErrors()) {
			throw new ApiException(ErrorConstant.INVALID_RQUEST_DATA);
		}
		AccountService accountService= AccountFactory.getService(request.getTransactionRequest().getAccountType());
		return accountService.depositAmmount(httpRequest,request);
	}

	@PostMapping("/withdrawAmount")
	public Response withdraw(HttpServletRequest httpRequest,@RequestBody @Valid Request request, BindingResult bindingResult) throws ApiException {
		logger.info("<-------------------------------AccountController| withdraw--------------------->");
		if(bindingResult.hasErrors()) {
			throw new ApiException(ErrorConstant.INVALID_RQUEST_DATA);
		}
		AccountService accountService= AccountFactory.getService(request.getTransactionRequest().getAccountType());
		return accountService.withdrawAount(httpRequest,request);
	}

	@GetMapping("/createAccount")
	public Response createAccount() throws ApiException {
		logger.info("<-------------------------------AccountController| createAccount--------------------->");
		Response response=new Response();
		for(AccountService  accountService: services) {
			accountService.createAccount(response);
		}
		response.setStatus(Constant.SUCCESS);
		return response;
	}

	@PostMapping("/checkBalanceAmount")
	public Response checkBalanceAmount(@RequestBody @Validated Request request, BindingResult bindingResult) throws ApiException {
		logger.info("<-------------------------------AccountController| checkBalanceAmount--------------------->");
		if(bindingResult.hasErrors()) {
			throw new ApiException(ErrorConstant.INVALID_RQUEST_DATA);
		}
		AccountService accountService= AccountFactory.getService(request.getTransactionRequest().getAccountType());
		return accountService.checkBalanceAmount(request);
	}

}
