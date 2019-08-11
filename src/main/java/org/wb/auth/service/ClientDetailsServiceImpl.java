package org.wb.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.wb.auth.constant.GrantType;
import org.wb.auth.constant.Permission;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("customClientDetailService")
public class ClientDetailsServiceImpl implements ClientService, ClientDetailsService {
    private final static String CLIENT_ID = "wbclient";
    private final static String SECRET_KEY = "wbclient";
    private final static List<String> SCOPES = new ArrayList<>();
    private final static List<String> GRANTS = new ArrayList<>();
    private final static Map<String, Object> INFOS = new LinkedHashMap<>();


    @Autowired
    @Qualifier(value = "passwordEncoder")
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        Stream.of(Permission.values()).forEach(p->{
            SCOPES.add(p.name());
        });
        Stream.of(GrantType.values()).forEach(g->{
            GRANTS.add(g.getName());
        });
        INFOS.put("client_number","9696848127");
        INFOS.put("client_email","wbadmin@worldbuild.com");
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws OAuth2Exception {
        if (clientId.equals(CLIENT_ID)) {
            BaseClientDetails clientDetails = new BaseClientDetails();
            clientDetails.setClientId(CLIENT_ID);
            clientDetails.setClientSecret(passwordEncoder.encode(SECRET_KEY));
            clientDetails.setScope(SCOPES);
            clientDetails.setAuthorizedGrantTypes(GRANTS);
            clientDetails.setAdditionalInformation(INFOS);
            clientDetails.setAccessTokenValiditySeconds(60*60); // Access token is only valid for 1 days.
            clientDetails.setRefreshTokenValiditySeconds(24*60* 60); // Refresh token is only valid for 1 hours.
            return clientDetails;
        } else {
            throw new NoSuchClientException("Client does not exists : " + clientId);
        }
    }
}
