package com.solidify.oauth2.security;

import com.solidify.oauth2.security.resource.UserDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
class UserTransformer implements Function<User, UserDto> {

    @Override
    public UserDto apply(User input) {
        UserDto dto = new UserDto();
        dto.setId(input.getId());
        dto.setEmail(input.getEmail());
        dto.setEnabled(input.getEnabled());
        dto.setFirstName(input.getFirstName());
        dto.setLastName(input.getLastName());
        return dto;
    }
}
