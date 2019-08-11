package com.example.test.userfront;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.example.utility.Constant;
import com.example.utility.EncryptUtil;
import com.example.utility.model.Encrypted;
import com.example.utility.model.Request;
import com.example.utility.model.RequestType;
import com.example.utility.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userfront.service.impl.RestServicesImpl;

@RunWith(MockitoJUnitRunner.Silent.class)
@SuppressWarnings("unchecked")
public class RestServiceTest {

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private RestServicesImpl restServiceImpl;	

	@Mock
	private ObjectMapper objectMapper;

	private String eurekaUrl="https://localhost:8301/service";

	@Before
	public void setup() {
		ReflectionTestUtils.setField(restServiceImpl, "eurekaUrl", eurekaUrl);
		ReflectionTestUtils.setField(restServiceImpl, "clientId", "admin");
		ReflectionTestUtils.setField(restServiceImpl, "clientSecret", "admin");

	}  

	@Test
	public void givenMockingIsDoneByMockito_whenGetIsCalled_shouldReturnMockedObject() throws  Exception {
		Response response = new Response(Constant.SUCCESS);
		ObjectMapper objMapper = new ObjectMapper();
		String jsonValue= objMapper.writeValueAsString(response);
		Encrypted encrypted = new Encrypted(EncryptUtil.encrypt(jsonValue));
		JSONObject accessToken = new JSONObject();
		accessToken.put("access_token", "asdasd-xcxc-asdsdv-asdsd");
		ResponseEntity<String> myEntity = new ResponseEntity<String>(accessToken.toString(), HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.contains("/authserver/oauth/token"), eq(HttpMethod.POST),any(HttpEntity.class),isA(Class.class))).thenReturn(myEntity);
		Mockito.when(restTemplate.exchange(Mockito.contains("/payment/deposit"), eq(HttpMethod.POST),any(HttpEntity.class),isA(Class.class)))
		.thenReturn(new ResponseEntity<Encrypted>(encrypted, HttpStatus.OK));
		Mockito.when(objectMapper.readValue(Mockito.anyString(), eq(Response.class))).thenReturn(response);
		Response response2=restServiceImpl.getRestResponse(new Request(), RequestType.DEFAULT, HttpMethod.POST, "/payment/deposit/");
		assertSame(Constant.SUCCESS,response2.getStatus());
	}

}

