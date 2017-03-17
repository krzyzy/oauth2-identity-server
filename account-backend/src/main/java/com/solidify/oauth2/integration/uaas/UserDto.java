package com.solidify.oauth2.integration.uaas;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by daniel on 15.02.15.
 */
@Data
@NoArgsConstructor
@ToString
public class UserDto {

    private String id;

    private String displayName;

    private String avatarUrl;

}
