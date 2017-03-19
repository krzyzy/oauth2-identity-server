package com.solidify.oauth2.user;

import com.solidify.oauth2.common.auth.local.LocalAuthenticationProvider;
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

    LocalUserRepository localUserRepository = mock(LocalUserRepository.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    AuthenticationProvider provider = new LocalAuthenticationProvider(localUserRepository, passwordEncoder);

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
        String login = "bar";
        String password = "barsecret";

        Authentication input = mock(Authentication.class);

        when(input.getName()).thenReturn(login);
        when(input.getCredentials()).thenReturn(password);

        LocalUser localUser = new LocalUser();
        localUser.setId(123l);
        localUser.setPassword(password);
        localUser.setLogin(login);

        when(localUserRepository.findByLogin(login)).thenReturn(localUser);
        when(passwordEncoder.matches(password, localUser.getPassword())).thenReturn(Boolean.TRUE);

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

        LocalUser localUser = new LocalUser();
        localUser.setId(123l);
        localUser.setPassword(password);
        localUser.setLogin(userName);

        when(localUserRepository.findByLogin(userName)).thenReturn(localUser);
        when(passwordEncoder.matches(password, localUser.getPassword())).thenReturn(Boolean.TRUE);

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