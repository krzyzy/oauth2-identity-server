package com.solidify.oauth2.registration;

import com.solidify.oauth2.mail.MailService;
import com.solidify.oauth2.user.LocalUser;
import com.solidify.oauth2.user.LocalUserRepository;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class UserRegistrationServiceTest {

    private LocalUserRepository userRepository = mock(LocalUserRepository.class);
    private PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private UserTokenRepository tokenRepository = mock(UserTokenRepository.class);
    private MailService mailService = mock(MailService.class);

    private UserRegistrationService service = new UserRegistrationService(
            userRepository,
            passwordEncoder,
            tokenRepository,
            mailService);

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
        verify(tokenRepository, never()).save(any(UserToken.class));
    }

    @Test
    public void should_register_new_user() {
        // given
        String email = "chuck@norris.com";
        UserRegistrationRequest request = mock(UserRegistrationRequest.class);
        when(request.getEmail()).thenReturn(email);
        when(request.getPassword()).thenReturn(new char[]{'a'});

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        // when
        service.registerUser(request);

        // then
        verify(userRepository).save(any(LocalUser.class));
        verify(tokenRepository).save(any(UserToken.class));
    }
}