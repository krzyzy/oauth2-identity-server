package com.solidify.oauth2.client;

import com.solidify.oauth2.client.Client;
import com.solidify.oauth2.client.ClientRepository;
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

        BaseClientDetails baseClientDetails = new BaseClientDetails();
        baseClientDetails.setClientId(clientId);
        baseClientDetails.setClientSecret(one.getClientSecret());
        baseClientDetails.setAuthorizedGrantTypes(one.getAuthorizedGrantTypes());
        baseClientDetails.setRegisteredRedirectUri(one.getRegisteredRedirectUri());
        baseClientDetails.setScope(one.getScope());
        baseClientDetails.setAutoApproveScopes(one.getAutoApproveScopes());

        baseClientDetails.setAccessTokenValiditySeconds(one.getAccessTokenValiditySeconds());
        baseClientDetails.setAdditionalInformation(one.getAdditionalInformation());
        baseClientDetails.setAuthorities(one.getAuthorities());
        return baseClientDetails;
    }
}
