package com.solidify.oauth2.common.auth.local;

import com.solidify.oauth2.user.LocalUser;
import com.solidify.oauth2.user.LocalUserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class LocalAuthenticationProvider implements AuthenticationProvider {

    private static final Set<Class> SUPPORTED_AUTHORISATIONS = new HashSet<>(singletonList(UsernamePasswordAuthenticationToken.class));

    private final LocalUserRepository localUserRepository;
    private final PasswordEncoder passwordEncoder;

    public LocalAuthenticationProvider(LocalUserRepository localUserRepository, PasswordEncoder passwordEncoder) {
        this.localUserRepository = localUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();
        LocalUser localUser = localUserRepository.findByLogin(userName);
        validateUser(localUser, password);

        return new UsernamePasswordAuthenticationToken(
                localUser.getLogin(),
                authentication.getCredentials(),
                emptyList());
    }

    private void validateUser(LocalUser localUser, String password) {
        if (localUser == null || !isPasswordCorrect(localUser, password)) {
            throw new BadCredentialsException("Incorrect user or password");
        }
    }

    private boolean isPasswordCorrect(LocalUser localUser, String password) {
        return passwordEncoder.matches(password, localUser.getPassword());
    }

    public boolean supports(Class<?> aClass) {
        return SUPPORTED_AUTHORISATIONS.contains(aClass);
    }
}
