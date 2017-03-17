package com.solidify.oauth2.user.social.facebook;

import com.google.common.collect.ImmutableMap;
import com.solidify.oauth2.integration.uaas.UaasUserMapping;
import com.solidify.oauth2.integration.uaas.UaasUserMappingRepository;
import com.solidify.oauth2.user.social.ProfileProvider;
import com.solidify.oauth2.integration.uaas.UaasResourceClient;
import com.solidify.oauth2.integration.uaas.UserDto;
import com.solidify.oauth2.integration.uaas.UserForm;
import com.solidify.oauth2.user.social.UserProfile;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by tomasz on 19.10.16.
 */
@Transactional
public class FacebookProfileProvider implements ProfileProvider {

    private static final String SOURCE = "facebook";
    private final Facebook facebook;
    private final UaasUserMappingRepository uaasUserMappingRepository;
    private final UaasResourceClient uaasClient;

    public FacebookProfileProvider(
            Facebook facebook,
            UaasUserMappingRepository uaasUserMappingRepository,
            UaasResourceClient uaasClient
    ) {
        this.facebook = facebook;
        this.uaasUserMappingRepository = uaasUserMappingRepository;
        this.uaasClient = uaasClient;
    }

    public UserProfile fetchUserProfile() {
        User profile = facebook.userOperations().getUserProfile();
        Optional<UaasUserMapping> identityMappingOptional = uaasUserMappingRepository.getBySource("facebook", profile.getId());
        if (identityMappingOptional.isPresent()) {
            return getUserProfile(profile, identityMappingOptional.get());
        } else {
            UserDto user;
            List<UserDto> users = uaasClient.getUser(SOURCE, String.valueOf(profile.getId()));
            if (users.isEmpty()) {
                user = uaasClient.createUser(
                        new UserForm(
                                profile.getName(),
                                SOURCE,
                                String.valueOf(profile.getId()),
                                ImmutableMap.<String, String>builder()
                                        .put("email", profile.getEmail())
                                        .build()
                        )
                );
            } else if (users.size()>1) {
                throw new IllegalStateException("two entries in uaas related to github identity: "+users);
            } else {
                user = users.get(0);
            }
            UaasUserMapping identityMapping = new UaasUserMapping("facebook", profile.getId(), user.getId());
            uaasUserMappingRepository.save(identityMapping);
            return getUserProfile(profile, identityMapping);
        }
    }

    private UserProfile getUserProfile(User profile, UaasUserMapping uaasUserMapping) {
        return new UserProfile(
                uaasUserMapping.getUaasId(),
                profile.getName(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getEmail(),
                profile.getEmail(),
                this.facebook.getBaseGraphApiUrl() + profile.getId() + "/picture"
        );
    }
}
