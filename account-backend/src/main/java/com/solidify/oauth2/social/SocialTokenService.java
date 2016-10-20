package com.solidify.oauth2.social;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.social.oauth2.AccessGrant;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tomasz on 19.10.16.
 */
public class SocialTokenService implements ResourceServerTokenServices {

    protected final Log logger = LogFactory.getLog(this.getClass());
    private final String clientId;
    private ProfileProviderFactory profileProviderFactory;

    public SocialTokenService(ProfileProviderFactory profileProviderFactory, String clientId) {
        this.profileProviderFactory = profileProviderFactory;
        this.clientId = clientId;
    }

    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        AccessGrant accessGrant = new AccessGrant(accessToken);
        ProfileProvider profileProvider = profileProviderFactory.createProfileProvider(accessGrant);
        UserProfile user = profileProvider.fetchUserProfile();
        return this.extractAuthentication(user);
    }

    private OAuth2Authentication extractAuthentication(UserProfile user) {
        List authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
        OAuth2Request request = new OAuth2Request((Map)null, this.clientId, (Collection)null, true, (Set)null, (Set)null, (String)null, (Set)null, (Map)null);

        return new OAuth2Authentication(request, new UsernamePasswordAuthenticationToken(
                user,
                "N/A",
                authorities
        ));
    }

    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }


}
