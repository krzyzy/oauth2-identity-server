package com.solidify.oauth2.common.auth.social;

import org.springframework.social.oauth2.AccessGrant;

/**
 * Created by tomasz on 19.10.16.
 */
public interface ProfileProviderFactory {

    ProfileProvider createProfileProvider(AccessGrant accessGrant);

}
