package com.example.payment.service;

import javax.servlet.http.HttpServletRequest;

import com.example.utility.exception.ApiException;
import com.example.utility.model.Request;
import com.example.utility.model.Response;

public interface AccountService {
	
	public String getType();
	
	public Response depositAmmount(HttpServletRequest httpRequest,Request request) throws ApiException;
	
	public Response withdrawAount(HttpServletRequest httpRequest,Request request) throws ApiException;
	
	public Response createAccount(Response response) throws ApiException;
	
	public Response checkBalanceAmount(Request request) throws ApiException;
	
}
