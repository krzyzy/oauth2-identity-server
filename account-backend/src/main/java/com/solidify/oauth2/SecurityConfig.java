package com.solidify.oauth2;

import com.solidify.oauth2.security.LocalAuthenticationProvider;
import com.solidify.oauth2.security.UserRepository;
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
    public LocalAuthenticationProvider localAuthenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder){
        return new LocalAuthenticationProvider(userRepository, passwordEncoder);
    }
}
