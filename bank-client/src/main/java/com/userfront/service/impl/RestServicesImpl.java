package com.userfront.service.impl;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.utility.EncryptUtil;
import com.example.utility.ErrorConstant;
import com.example.utility.exception.ApiException;
import com.example.utility.model.Encrypted;
import com.example.utility.model.Request;
import com.example.utility.model.RequestType;
import com.example.utility.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userfront.service.RestService;

@Service
@PropertySource("classpath:application.properties")
public class RestServicesImpl implements RestService
{

	private static final Logger logger = LoggerFactory.getLogger(RestServicesImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper mapper;

	@Value("${eureka.endpoint.url}")
	private String eurekaUrl;

	@Value("${cas.client.id}")
	private String clientId;

	@Value("${cas.client.secret}")
	private String clientSecret;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getRestResponse(Request request, RequestType requestType,HttpMethod method,String url) throws ApiException {
		try
		{
			StringBuilder finalUrl= new StringBuilder(eurekaUrl).append(url);
			HttpHeaders headers = createHeaders(requestType);
			HttpEntity<Object> entity = createEntity(request, requestType, headers);
			Class<T> responseClazz = getClassResponseType(requestType);
			logger.info("Headers :{} ", headers);
			logger.info("REST URL ::{}",finalUrl);
			ResponseEntity<T> serviceResponse = restTemplate.exchange(finalUrl.toString(), method, entity, responseClazz);
			if(serviceResponse.getStatusCode().equals(HttpStatus.OK) && "OATH_TOKEN".equalsIgnoreCase(requestType.toString())){
				return serviceResponse.getBody();
			}else if(serviceResponse.getStatusCode().equals(HttpStatus.OK)) {
				Encrypted encryptedvalue=(Encrypted) serviceResponse.getBody();
				return (T) mapper.readValue(EncryptUtil.decrypt(encryptedvalue.getEncryptedValue()), Response.class);
			}
		}  catch (Exception e) {
			logger.error("Exception in getRestResponse::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		return null;	
	}

	/**
	 * This method is used to create Request body for the rest api call based on different request type
	 * @param productServicesRequest
	 * @param requestType
	 * @param headers
	 * @return
	 * @throws ApiException
	 */
	@SuppressWarnings("unchecked")
	private <V> HttpEntity<V> createEntity(Request request, RequestType requestType, HttpHeaders headers) throws ApiException 
	{
		HttpEntity<V> httpEntity = null;
		String restRequest= "";
		try {

			switch(requestType) {
			case OATH_TOKEN:
				MultiValueMap<String, String> args = new LinkedMultiValueMap<>();
				args.set("grant_type", "client_credentials");
				httpEntity = (HttpEntity<V>) new HttpEntity<>(args, headers);
				break;	

			default:
				String json=mapper.writeValueAsString(request);
				Encrypted encrypt= new Encrypted(EncryptUtil.encrypt(json));
				restRequest = mapper.writeValueAsString(encrypt);
				logger.info("{}:{}",requestType, restRequest);
				httpEntity = (HttpEntity<V>) new HttpEntity<>(restRequest, headers);
				break;
			} 
		} catch (Exception e) {
			logger.error("Exception in createEntity::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		return httpEntity;
	}

	/**
	 * This method creating headers for the rest api call based on different request type.
	 * 
	 * @param productServicesRequest
	 * @param requestType
	 * @param request
	 * @return
	 * @throws ApiException
	 */
	private HttpHeaders createHeaders(RequestType requestType) throws ApiException 
	{
		HttpHeaders headers = new HttpHeaders();
		try {
			if(RequestType.OATH_TOKEN==requestType) {
				headers.set(HttpHeaders.AUTHORIZATION,"Basic "+new String(Base64.encodeBase64((clientId+":"+clientSecret).getBytes())));
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			}else{
				headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+getOAuthToken());
				headers.setContentType(MediaType.APPLICATION_JSON);
			}
		} catch (Exception e) {
			logger.error("Exception in createHeaders::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}
		return headers;
	}

	private String getOAuthToken() throws ApiException{
		String result;
		try {
			String json=getRestResponse(new Request(), RequestType.OATH_TOKEN, HttpMethod.POST,"/authserver/oauth/token");
			JSONObject jsonObject = new JSONObject(json);
			result=jsonObject.getString("access_token");	
		} catch (Exception e) {
			logger.error("Exception in getOAuthToken::{}",e);
			throw new ApiException(ErrorConstant.SERVICE_EXCEPTION);
		}

		return result;
	}

	    @SuppressWarnings({ "unused", "unchecked" })
		private <T> T getClassResponseType(RequestType requestType)
	    {
			Class<T> clazz = (Class<T>) Encrypted.class;
	        switch (requestType) {
	        case OATH_TOKEN:
	            clazz = (Class<T>) String.class;
	            break;
	        default:
	            break;
	        }
	        return (T) clazz;
	    }
}