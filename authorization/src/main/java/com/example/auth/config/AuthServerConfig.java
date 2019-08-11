package com.example.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import com.example.auth.util.Constants;


/* this is to set up your token generation end point i.e. 
 * if you provide the properties security.oauth2.client.client-id and security.oauth2.client.client-secret,
 *  Spring will give you an authentication server, providing standard Oauth2 tokens at the endpoint /oauth/token
 * */
@EnableAuthorizationServer
@Configuration
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {   

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	//Default
	@Value("${cas.client.id}")
	private String clientId;
	@Value("${cas.client.secret}")
	private String clientSecret;
	
	@Value("${redis.ip}")
	private String redisIp;
	
	@Value("${redis.port}")
	private int redisPort;

	/**
	 * This can be used to configure security of your authorization server itself
	 *	i.e. which user can generate tokens , changing default realm etc
	 *
	 */
	@Override
	public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer
		.tokenKeyAccess(Constants.PERMIT_ALL)
		.checkTokenAccess(Constants.IS_AUTHENTICATED);
	}

	/*Here you will specify about `ClientDetailsService` i.e.
    information about OAuth2 clients & where their info is located - memory , DB , LDAP etc.*/
	@Override
	public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {	
		clients.inMemory().withClient(clientId).secret("{noop}"+clientSecret).scopes("read").authorizedGrantTypes("client_credentials");
	}

	/**
	 * The below will do non-security configs for end points associated with your Authorization Server
	 *	and can specify details about authentication manager, token generation etc.
	 */
	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(false);
		defaultTokenServices.setAccessTokenValiditySeconds(1 * 60 * 60);
		defaultTokenServices.setAuthenticationManager(authenticationManager);
		endpoints.tokenServices(defaultTokenServices);
	}


	@Bean
	public TokenStore tokenStore() {
		return new RedisTokenStore(lettuceConnectionFactory());
	}
	
	@Bean
	public LettuceConnectionFactory lettuceConnectionFactory() {
		return new LettuceConnectionFactory(redisIp,redisPort);
	}

}
