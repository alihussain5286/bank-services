package com.userfront.service;

import com.example.utility.exception.ApiException;
import com.example.utility.model.Response;
import com.example.utility.model.TransactionRequest;
import com.userfront.util.CheckAmountRequest;

public interface AccountService {
	
    Response createAccount() throws ApiException;
    
    Response deposit(TransactionRequest transactionRequest, String username) throws ApiException;
    
    Response withdraw(CheckAmountRequest checkAmountRequest, String username) throws ApiException ;
    
}
