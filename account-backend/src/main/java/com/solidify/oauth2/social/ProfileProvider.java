package com.solidify.oauth2.social;

import org.springframework.social.connect.*;

/**
 * Created by tomasz on 19.10.16.
 */
public interface ProfileProvider {

    UserProfile fetchUserProfile();

}
