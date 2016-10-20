package com.solidify.oauth2.social;

import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubUserProfile;

/**
 * Created by tomasz on 19.10.16.
 */
public class GithubProfileProvider implements ProfileProvider {

    private GitHub gitHub;

    public GithubProfileProvider(GitHub gitHub) {
        this.gitHub = gitHub;
    }

    public UserProfile fetchUserProfile() {
        GitHubUserProfile profile = gitHub.userOperations().getUserProfile();

        return new UserProfile(
                String.valueOf(profile.getId()),
                profile.getName(),
                "N/A",
                "N/A",
                profile.getEmail(),
                profile.getUsername(),
                profile.getProfileImageUrl()
        );
    }
}
