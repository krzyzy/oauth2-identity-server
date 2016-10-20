package com.solidify.oauth2.social;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;

/**
 * Created by tomasz on 19.10.16.
 */
public class FacebookProfileProvider implements ProfileProvider {

    private final Facebook facebook;

    public FacebookProfileProvider(Facebook facebook) {
        this.facebook = facebook;
    }

    public UserProfile fetchUserProfile() {
        User profile = facebook.userOperations().getUserProfile();
        return new UserProfile(
                profile.getId(),
                profile.getName(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getEmail(),
                profile.getEmail(),
                facebook.getBaseGraphApiUrl() + profile.getId() + "/picture"
        );
    }
}
