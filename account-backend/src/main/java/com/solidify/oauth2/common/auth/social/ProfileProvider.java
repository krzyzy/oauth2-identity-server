package com.solidify.oauth2.common.auth.social;

/**
 * Created by tomasz on 19.10.16.
 */
public interface ProfileProvider {

    UserProfile fetchUserProfile();

}
