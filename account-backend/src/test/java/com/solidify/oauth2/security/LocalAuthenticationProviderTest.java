package com.solidify.oauth2.security;

import org.junit.Test;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocalAuthenticationProviderTest {

    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    AuthenticationProvider provider = new LocalAuthenticationProvider(userRepository, passwordEncoder);

    @Test(expected = BadCredentialsException.class)
    public void shouldFailToAuthenticateForInvalidUserName() {
        // given
        String invalidUserName = "";
        String password = "";

        Authentication input = mock(Authentication.class);

        when(input.getName()).thenReturn(invalidUserName);
        when(input.getCredentials()).thenReturn(password);

        // when
        provider.authenticate(input);
    }

    @Test(expected = BadCredentialsException.class)
    public void shouldFailToAuthenticateForInvalidPassword() {
        // given
        String userName = "";
        String invalidPassword = "";

        Authentication input = mock(Authentication.class);

        when(input.getName()).thenReturn(userName);
        when(input.getCredentials()).thenReturn(invalidPassword);

        // when
        provider.authenticate(input);
    }

    @Test
    public void shouldAuthenticateUser() {
        // given
        String userName = "bar";
        String password = "barsecret";

        Authentication input = mock(Authentication.class);

        when(input.getName()).thenReturn(userName);
        when(input.getCredentials()).thenReturn(password);

        User user = new User();
        user.setPassword(password);
        user.setEmail(userName);
        user.setEnabled(Boolean.TRUE);

        when(userRepository.findByEmailAndEnabled(userName, Boolean.TRUE)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(Boolean.TRUE);

        // when
        Authentication authentication = provider.authenticate(input);

        // then
        assertNotNull(authentication);
    }

    @Test(expected = BadCredentialsException.class)
    public void shouldNotAuthenticateDisabledUser() {
        // given
        String userName = "bar";
        String password = "barsecret";

        Authentication input = mock(Authentication.class);

        when(input.getName()).thenReturn(userName);
        when(input.getCredentials()).thenReturn(password);

        User user = new User();
        user.setPassword(password);
        user.setEmail(userName);
        user.setEnabled(Boolean.FALSE);

        when(userRepository.findByEmailAndEnabled(userName, Boolean.TRUE)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(Boolean.TRUE);

        // when
        provider.authenticate(input);
    }


    @Test
    public void shouldSupportLoginFormAuthentication() {
        // then
        assertTrue(provider.supports(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void shouldNotSupportAllAuthenticationTokens() {
        // then
        assertFalse(provider.supports(TestingAuthenticationToken.class));
    }
}