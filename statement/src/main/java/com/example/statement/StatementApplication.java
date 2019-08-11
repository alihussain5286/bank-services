package com.example.statement;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.zalando.logbook.Logbook;

import com.example.utility.Util;

@SpringBootApplication
@EnableEurekaClient
@EntityScan("com.example.utility.*")
public class StatementApplication {
	
	@Value("${ssl.trust-store-password}")
	private String trustStorePassword;
	@Value("${ssl.trust-store-type}")
	private String trustStoreType;
	
	public static void main(String[] args) {
		SpringApplication.run(StatementApplication.class, args);
	}
	
	@Bean
	public Logbook logBook() {
		return Logbook.create();
	}
	
	@Profile("!DOCKER")
	@PostConstruct
    void postConstruct() throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("keystore/example.p12");
		String path=Util.getTrustStorePath(classPathResource, "statement");
		System.setProperty("javax.net.ssl.trustStore",path);
        System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
        System.setProperty("javax.net.ssl.keyStoreType", trustStoreType);
	}
}
