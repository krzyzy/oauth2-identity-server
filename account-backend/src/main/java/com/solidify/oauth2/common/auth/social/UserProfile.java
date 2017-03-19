package com.solidify.oauth2.common.auth.social;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class UserProfile implements Serializable {

    private final String uaasIdentityId;
    private final String name;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String username;
    private final String imageUrl;

}