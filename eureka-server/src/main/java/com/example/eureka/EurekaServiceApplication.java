package com.example.eureka;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

@EnableEurekaServer
@EnableZuulProxy
@SpringBootApplication
public class EurekaServiceApplication {

	private static final Logger LOGGER= LoggerFactory.getLogger(EurekaServiceApplication.class);

	@Value("${ssl.trust-store-password}")
	private String trustStorePassword;
	@Value("${ssl.trust-store-type}")
	private String trustStoreType;

	public static void main(String[] args) {
		SpringApplication.run(EurekaServiceApplication.class, args);
	}
	@Profile("!DOCKER")
	@PostConstruct
	void postConstruct() throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("keystore/example.p12");
		String path=getTrustStorePath(classPathResource, "eureka");
		System.setProperty("javax.net.ssl.trustStore",path);
		System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
		System.setProperty("javax.net.ssl.keyStoreType", trustStoreType);
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
