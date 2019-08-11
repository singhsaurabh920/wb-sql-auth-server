package org.wb.auth.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
@EnableAuthorizationServer
public class AuthrizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Value("${check-user-scopes}")
	private Boolean checkUserScopes;

	@Autowired
	private DataSource dataSource;

	@Autowired
	@Qualifier(value = "passwordEncoder")
	private PasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier(value = "customUserDetailsService")
	private UserDetailsService userDetailsService;

	@Autowired
	@Qualifier(value = "customClientDetailService")
	private ClientDetailsService clientDetailsService;

	@Autowired
	@Qualifier(value = "authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	@Bean
	OAuth2RequestFactory oAuth2RequestFactory(){
		CustomOauth2RequestFactory defaultOAuth2RequestFactory= new CustomOauth2RequestFactory(clientDetailsService);
		defaultOAuth2RequestFactory.setCheckUserScopes(checkUserScopes);
		return defaultOAuth2RequestFactory;
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new CustomTokenEnhancer();
		converter.setKeyPair(new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "password".toCharArray()).getKeyPair("jwt"));
		return converter;
	}

	@Bean
	public TokenEndpointAuthenticationFilter tokenEndpointAuthenticationFilter() {
		return new TokenEndpointAuthenticationFilter(authenticationManager, oAuth2RequestFactory());
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(clientDetailsService).build();
		/*clients.inMemory()
				.withClient("wbclient")
				.secret(passwordEncoder.encode("wbclient"))
				.scopes("read","write","trust")
				.authorizedGrantTypes("password","implicit","refresh_token","authorization_code")
				.additionalInformation("email:wbadmin@worldbuild.com")
				.accessTokenValiditySeconds(60*60)
				.refreshTokenValiditySeconds(24*60*60);*/
		//clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
	}
	
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore())
				 .requestFactory(oAuth2RequestFactory())
				 .tokenEnhancer(jwtAccessTokenConverter())
				 .authenticationManager(authenticationManager)
				 .userDetailsService(userDetailsService);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}