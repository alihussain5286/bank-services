package com.userfront.service;

import org.springframework.http.HttpMethod;

import com.example.utility.exception.ApiException;
import com.example.utility.model.Request;
import com.example.utility.model.RequestType;



@FunctionalInterface
public interface RestService {
	
	public  <T> T getRestResponse(Request request, RequestType requestType, HttpMethod method, String url) throws ApiException;
	
}