package com.solidify.oauth2.user.registration;

import com.solidify.oauth2.mail.MailService;
import com.solidify.oauth2.user.LocalUser;
import com.solidify.oauth2.user.LocalUserRepository;
import com.solidify.oauth2.user.TokenService;
import com.solidify.oauth2.user.UserToken;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class UserRegistrationServiceTest {

    private LocalUserRepository userRepository = mock(LocalUserRepository.class);
    private PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private TokenService tokenService = mock(TokenService.class);
    private MailService mailService = mock(MailService.class);

    private UserRegistrationService service = new UserRegistrationService(
            userRepository,
            passwordEncoder,
            mailService,
            tokenService);

    @Test
    public void should_reject_new_user_registration_when_email_already_taken() {
        // given
        String email = "chuck@norris.com";
        UserRegistrationRequest request = mock(UserRegistrationRequest.class);
        when(request.getEmail()).thenReturn(email);
        when(request.getPassword()).thenReturn(new char[]{'a'});
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(LocalUser.class)));

        // when
        try {
            service.registerUser(request);
            fail("Should not allow to create new user");
        } catch (IllegalArgumentException ignore) {
        }

        // then
        verify(userRepository, never()).save(any(LocalUser.class));
        verify(tokenService, never()).save(any(UserToken.class));
    }

    @Test
    public void should_register_new_user() {
        // given
        String email = "chuck@norris.com";
        UserRegistrationRequest request = mock(UserRegistrationRequest.class);
        when(request.getEmail()).thenReturn(email);
        when(request.getPassword()).thenReturn(new char[]{'a'});

        when(tokenService.createToken(UserToken.TokenType.REGISTRATION)).thenReturn(new UserToken());
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        // when
        service.registerUser(request);

        // then
        verify(userRepository).save(any(LocalUser.class));
        verify(tokenService).save(any(UserToken.class));
    }

    @Test
    public void should_activate_uer_by_token() {
        // given
        String tokenId = "1";

        LocalUser user = new LocalUser();
        UserToken token = new UserToken();
        token.setUser(user);
        when(tokenService.getToken(tokenId, UserToken.TokenType.REGISTRATION)).thenReturn(token);
        // when
        service.activateUserByToken(tokenId);

        // then
        verify(userRepository).save(user);
        assertTrue(user.getEnabled());
    }
}