package com.example.payment.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.payment.service.AccountService;


@Service
public class AccountFactory {

	private static final Map<String, AccountService> myServiceCache = new HashMap<>();

	@Autowired
	private AccountFactory(List<AccountService> services) {
		for(AccountService service : services) {
			myServiceCache.put(service.getType(), service);
		}
	}

	public static AccountService getService(String type) {
		AccountService service = myServiceCache.get(type);
		if(service == null) {
			throw new RuntimeException("Unknown service type: "+ type);
		}
		return service;
	}

}
