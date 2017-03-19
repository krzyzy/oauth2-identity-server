package com.solidify.oauth2.view.profile;

import com.solidify.oauth2.common.integration.uaas.UaasUserMapping;
import com.solidify.oauth2.common.integration.uaas.UaasUserMappingRepository;
import com.solidify.oauth2.user.LocalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class UserTransformer implements Function<LocalUser, LocalUserDto> {

    private UaasUserMappingRepository uaasUserMappingRepository;

    @Autowired
    public UserTransformer(UaasUserMappingRepository uaasUserMappingRepository) {
        this.uaasUserMappingRepository = uaasUserMappingRepository;
    }

    @Override
    public LocalUserDto apply(LocalUser input) {
        Optional<UaasUserMapping> local = uaasUserMappingRepository.getBySource("local", String.valueOf(input.getId()));
        LocalUserDto dto = LocalUserDto.builder()
                .id(input.getId())
                .login(input.getLogin())
                .uaasIdentityId(local.map(UaasUserMapping::getUaasId).orElse(null))
                .build();
        return dto;
    }
}
