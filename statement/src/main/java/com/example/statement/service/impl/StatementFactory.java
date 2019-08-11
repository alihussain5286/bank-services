package com.example.statement.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.statement.service.StatementService;


@Service
public class StatementFactory {

	private static final Map<String, StatementService> myServiceCache = new HashMap<>();

	@Autowired
	private StatementFactory(List<StatementService> services) {
		for(StatementService service : services) {
			myServiceCache.put(service.getType(), service);
		}
	}

	public static StatementService getService(String type) {
		StatementService service = myServiceCache.get(type);
		if(service == null) { 
			throw new RuntimeException("Unknown service type: " + type);
		}
		return service;
	}
}
