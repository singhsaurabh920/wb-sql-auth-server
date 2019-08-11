package org.wb.auth.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.wb.auth.entity.Role;
import org.wb.auth.entity.User;
import org.wb.auth.utils.RoleUtils;

public class CustomOauth2RequestFactory extends DefaultOAuth2RequestFactory {
	
	@Autowired
	private TokenStore tokenStore;
	
	@Autowired
	@Qualifier(value = "customUserDetailsService")
	private UserDetailsService userDetailsService;

	public CustomOauth2RequestFactory(ClientDetailsService clientDetailsService) {
		super(clientDetailsService);
	}


	@Override
	public TokenRequest createTokenRequest(Map<String, String> requestParameters,ClientDetails authenticatedClient) {
		if (requestParameters.get("grant_type").equals("refresh_token")) {
			OAuth2RefreshToken oAuth2RefreshToken=tokenStore.readRefreshToken(requestParameters.get("refresh_token"));
			OAuth2Authentication authentication = tokenStore.readAuthenticationForRefreshToken(oAuth2RefreshToken);
			User user = (User) userDetailsService.loadUserByUsername(authentication.getName());
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		return super.createTokenRequest(requestParameters, authenticatedClient);
	}
}
