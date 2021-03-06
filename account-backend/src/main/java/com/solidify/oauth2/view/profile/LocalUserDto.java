package com.solidify.oauth2.view.profile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocalUserDto {

    private Long id;

    private String login;

    private String uaasIdentityId;

}
