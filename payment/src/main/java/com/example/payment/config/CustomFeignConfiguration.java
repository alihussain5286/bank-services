package com.example.payment.config;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Client;

@SuppressWarnings("deprecation")
@Configuration
public class CustomFeignConfiguration {

	private static final Logger logger= LoggerFactory.getLogger(CustomFeignConfiguration.class);
	
	@Bean
	public Client feignClient()
	{
	    Client trustSSLSockets = new Client.Default(getSSLSocketFactory(), new NoopHostnameVerifier());
	    logger.info("<----------Cusotm Feign Called ------------------->"); 
	    return trustSSLSockets;
	}

	private SSLSocketFactory getSSLSocketFactory() {
	    try {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null,  new TrustSelfSignedStrategy()).build();
	        return sslContext.getSocketFactory();
	    } catch (Exception exception) {
	    	logger.error("Exception in CustomFeignConfiguration::{}",exception); 
	        throw new RuntimeException(exception);
	    }
	}

}
