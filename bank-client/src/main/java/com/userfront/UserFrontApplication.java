package com.userfront;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.zalando.logbook.Logbook;

import com.example.utility.entity.Role;
import com.userfront.dao.RoleRepository;

@SpringBootApplication
@EntityScan("com.example.utility.*")
@SuppressWarnings("deprecation")
public class UserFrontApplication {

	@Value("${trust.store.password}")
	private String trustStorePassword;

	@Value("${spring.profiles.active}")
	private String springProfileActive;
	
	@Value("${read.timeout}")
	private Integer readTimeOut;
	
	@Value("${connection.timeout}")
	private Integer connectionTimeOut;

	public static void main(String[] args) {
		SpringApplication.run(UserFrontApplication.class, args);
	}
	
	@Autowired
	private RoleRepository roleRepository;

	@Bean
	public RestTemplate restTemplate() throws Exception {
		return new RestTemplate(clientHttpRequestFactory());
	}

	@Bean
	public ClientHttpRequestFactory clientHttpRequestFactory() throws Exception {
		HttpComponentsClientHttpRequestFactory factory = ("DOCKER").equalsIgnoreCase(springProfileActive)? new HttpComponentsClientHttpRequestFactory()
				: new HttpComponentsClientHttpRequestFactory(httpClient());
		factory.setReadTimeout(readTimeOut);
		factory.setConnectTimeout(connectionTimeOut);
		return factory;
	}


	@Profile("!DOCKER")
	@Bean
	public HttpClient httpClient() throws Exception {
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		ClassPathResource classPathResource = new ClassPathResource("keystore/example.p12");
		trustStore.load(classPathResource.getInputStream(), trustStorePassword.toCharArray());
		SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE);

		return HttpClients.custom().setSSLSocketFactory(sslsf).build();
	}

	@Bean
	public Logbook logBook() {
		return Logbook.create();
	}

	@Bean
	InitializingBean sendDatabase() {
		return () ->{
			Role role= roleRepository.findByName("ROLE_USER");
			if(null== role) {
				role= new Role();
				role.setName("ROLE_USER");
				roleRepository.save(role);
			}
		};
	}
	
}
