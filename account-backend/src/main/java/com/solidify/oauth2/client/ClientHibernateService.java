package com.solidify.oauth2.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by tomasz on 16.10.16.
 */
@Component
@Qualifier("hibernate")
@Transactional
public class ClientHibernateService implements ClientDetailsService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientHibernateService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Client one = clientRepository.findOne(clientId);

        BaseClientDetails details = new BaseClientDetails();
        details.setClientId(clientId);
        details.setClientSecret(one.getClientSecret());
        details.setAuthorizedGrantTypes(one.getAuthorizedGrantTypes());
        details.setRegisteredRedirectUri(one.getRegisteredRedirectUri());
        details.setScope(one.getScope());
        details.setAutoApproveScopes(one.getAutoApproveScopes());

        details.setAccessTokenValiditySeconds(one.getAccessTokenValiditySeconds());
        details.setAdditionalInformation(one.getAdditionalInformation());
        details.setAuthorities(one.getAuthorities());
        return details;
    }
}
