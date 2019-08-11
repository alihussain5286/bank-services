package com.example.payment.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.payment.config.CustomFeignConfiguration;
import com.example.utility.model.Request;
import com.example.utility.model.Response;

@FeignClient(name="statement",configuration = CustomFeignConfiguration.class,url="${statement.service.endpoint.url}")
public interface StatementFeignClient {
	
	@PostMapping("/saveTransaction")
	public Response saveTransaction(@RequestHeader("Authorization")String token,@RequestBody Request request);
	
}
