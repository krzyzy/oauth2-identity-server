package com.solidify.oauth2.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.singletonList;

public class LocalAuthenticationProvider implements AuthenticationProvider {

    private static final Set<Class> SUPPORTED_AUTHORISATIONS = new HashSet<Class>(singletonList(UsernamePasswordAuthenticationToken.class));

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LocalAuthenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userRepository.findByEmail(userName);
        validateUser(password, user);

        return new UsernamePasswordAuthenticationToken(userName, password, Collections.<GrantedAuthority>emptyList());
    }

    private void validateUser(String password, User user) {
        if (user == null || !isPasswordCorrect(password, user)) {
            throw new BadCredentialsException("Incorrect user or password");
        }
        if (!user.getEnabled()) {
            throw new BadCredentialsException("Please activate your account");
        }
    }

    private boolean isPasswordCorrect(String password, User user) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public boolean supports(Class<?> aClass) {
        return SUPPORTED_AUTHORISATIONS.contains(aClass);
    }
}
