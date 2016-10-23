package com.solidify.oauth2.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.singletonList;

@Component
public class LocalAuthenticationProvider implements AuthenticationProvider {

    private static final Set<Class> SUPPORTED_AUTHORISATIONS = new HashSet<Class>(singletonList(UsernamePasswordAuthenticationToken.class));

    private static final String TEST_USER = "bar";
    private static final String TEST_PASSWORD = "barsecret";

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (TEST_USER.equals(userName) && TEST_PASSWORD.equals(password)) {
            return new UsernamePasswordAuthenticationToken(userName, password, Collections.<GrantedAuthority>emptyList());
        }

        throw new BadCredentialsException(String.format("Failed to authenticate user '%s'", userName));
    }

    public boolean supports(Class<?> aClass) {
        return SUPPORTED_AUTHORISATIONS.contains(aClass);
    }
}
