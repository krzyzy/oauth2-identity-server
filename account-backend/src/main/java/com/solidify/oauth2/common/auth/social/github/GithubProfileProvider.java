package com.solidify.oauth2.common.auth.social.github;

import com.google.common.collect.ImmutableMap;
import com.solidify.oauth2.common.auth.social.UserProfile;
import com.solidify.oauth2.common.integration.uaas.UaasUserMapping;
import com.solidify.oauth2.common.integration.uaas.UaasUserMappingRepository;
import com.solidify.oauth2.common.auth.social.ProfileProvider;
import com.solidify.oauth2.common.integration.uaas.UaasResourceClient;
import com.solidify.oauth2.common.integration.uaas.UserDto;
import com.solidify.oauth2.common.integration.uaas.UserForm;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubUserProfile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by tomasz on 19.10.16.
 */
@Transactional
public class GithubProfileProvider implements ProfileProvider {

    private static final String SOURCE = "github";

    private GitHub gitHub;
    private final UaasUserMappingRepository uaasUserMappingRepository;
    private final UaasResourceClient uaasClient;

    public GithubProfileProvider(
            GitHub gitHub,
            UaasUserMappingRepository uaasUserMappingRepository,
            UaasResourceClient uaasClient
    ) {
        this.gitHub = gitHub;
        this.uaasUserMappingRepository = uaasUserMappingRepository;
        this.uaasClient = uaasClient;
    }

    public UserProfile fetchUserProfile() {
        GitHubUserProfile profile = gitHub.userOperations().getUserProfile();


        Optional<UaasUserMapping> identityMappingOptional = uaasUserMappingRepository.getBySource(SOURCE, String.valueOf(profile.getId()));
        if (identityMappingOptional.isPresent()) {
            return getUserProfile(profile, identityMappingOptional.get());
        } else {
            UserDto user;
            List<UserDto> users = uaasClient.getUser(SOURCE, String.valueOf(profile.getId()));
            if (users.isEmpty()) {
                user = uaasClient.createUser(
                        new UserForm(
                                profile.getUsername(),
                                SOURCE,
                                String.valueOf(profile.getId()),
                                ImmutableMap.<String, String>builder()
                                        .build()
                        )
                );
            } else if (users.size()>1) {
                throw new IllegalStateException("two entries in uaas related to github identity: "+users);
            } else {
                user = users.get(0);
            }
            UaasUserMapping identityMapping = new UaasUserMapping(SOURCE, String.valueOf(profile.getId()), user.getId());
            uaasUserMappingRepository.save(identityMapping);
            return getUserProfile(profile, identityMapping);
        }
    }

    private UserProfile getUserProfile(GitHubUserProfile profile, UaasUserMapping uaasUserMapping) {
        return new UserProfile(
                uaasUserMapping.getUaasId(),
                profile.getName(),
                "N/A",
                "N/A",
                profile.getEmail(),
                profile.getUsername(),
                profile.getProfileImageUrl()
        );
    }
}
