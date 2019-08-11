package com.example.statement.service;

import com.example.utility.exception.ApiException;
import com.example.utility.model.Request;

public interface StatementService {
    
	public Object getTransactionsList(Request request)throws ApiException;
	
	public Object saveTransaction(Request request)throws ApiException;
	
	public String getType();
	

}
