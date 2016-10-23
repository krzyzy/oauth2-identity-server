package com.solidify.oauth2.security;

import org.junit.Test;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocalAuthenticationProviderTest {

    AuthenticationProvider provider = new LocalAuthenticationProvider();

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
    public void shouldAuthenticateuser() {
        // given
        String userName = LocalAuthenticationProvider.TEST_USER;
        String invalidPassword = LocalAuthenticationProvider.TEST_PASSWORD;

        Authentication input = mock(Authentication.class);

        when(input.getName()).thenReturn(userName);
        when(input.getCredentials()).thenReturn(invalidPassword);

        // when
        Authentication authentication = provider.authenticate(input);

        // then
        assertNotNull(authentication);
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