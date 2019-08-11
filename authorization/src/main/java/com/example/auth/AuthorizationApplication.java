package com.example.auth;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class AuthorizationApplication {
	
	private static final Logger LOGGER= LoggerFactory.getLogger(AuthorizationApplication.class);
	
	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationApplication.class, args);
	}

	@Profile("!DOCKER")
	@PostConstruct
	void postConstruct() throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("keystore/example.p12");
		String path=getTrustStorePath(classPathResource, "auth");
		System.setProperty("javax.net.ssl.trustStore",path);
		System.setProperty("javax.net.ssl.trustStorePassword", env.getProperty("ssl.trust-store-password"));
		System.setProperty("javax.net.ssl.keyStoreType", env.getProperty("ssl.trust-store-type"));
	}
	
	private String getTrustStorePath(ClassPathResource classPathResource,String prefix) {
		File somethingFile = null;
		try {
			try(InputStream inputStream = classPathResource.getInputStream();) {
				somethingFile = File.createTempFile(prefix, ".p12");
				java.nio.file.Files.copy(inputStream, somethingFile.toPath(),java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception e) {
			LOGGER.error("Exception in Util| getTrustStorePath::{}",e);
		}
		return somethingFile.getPath();
	}
}
