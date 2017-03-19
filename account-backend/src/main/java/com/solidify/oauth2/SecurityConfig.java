package com.solidify.oauth2;

import com.solidify.oauth2.common.auth.local.LocalAuthenticationProvider;
import com.solidify.oauth2.user.LocalUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new StandardPasswordEncoder();
    }

    @Bean
    public LocalAuthenticationProvider localAuthenticationProvider(LocalUserRepository localUserRepository, PasswordEncoder passwordEncoder){
        return new LocalAuthenticationProvider(localUserRepository, passwordEncoder);
    }
}
