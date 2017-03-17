package com.solidify.oauth2.user.local;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
class UserTransformer implements Function<LocalUser, LocalUserDto> {

    @Override
    public LocalUserDto apply(LocalUser input) {
        LocalUserDto dto = LocalUserDto.builder()
                .id(input.getId())
                .login(input.getLogin())
                .build();
        dto.setId(input.getId());
        dto.setLogin(input.getLogin());
        return dto;
    }
}
