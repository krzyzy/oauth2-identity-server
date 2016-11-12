package com.solidify.oauth2.security;

import com.solidify.oauth2.security.resource.UserChangePasswordForm;
import com.solidify.oauth2.security.resource.UserDto;
import com.solidify.oauth2.web.ResponseMessage;
import com.solidify.oauth2.web.ResponseStatus;
import org.dom4j.IllegalAddException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserProfileControllerTest {
    UserRepository repository = mock(UserRepository.class);
    UserTransformer toDto = mock(UserTransformer.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    UserProfileController controller = new UserProfileController(repository, toDto, passwordEncoder);

    @Test
    public void shouldReturnLocalUser() {
        // given
        final String userName = "bar@gmail.com";
        setPrincipals(userName);

        UserDto userDetails = mock(UserDto.class);

        when(repository.findByEmail(userName)).thenReturn(mock(User.class));
        when(toDto.apply(any(User.class))).thenReturn(userDetails);
        // when

        Object userPrincipals = controller.user();

        // then
        assertEquals(userDetails, userPrincipals);
        verify(repository).findByEmail(userName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectEmptyUpdatePasswordForm() {
        // given
        UserChangePasswordForm input = new UserChangePasswordForm();

        // when
        controller.changeUserPassword(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectInvalidUpdatePasswordForm() {
        // given
        UserChangePasswordForm input = new UserChangePasswordForm();
        input.setPassword("1");
        input.setConfirmPassword("2");

        // when
        controller.changeUserPassword(input);
    }

    @Test
    public void shouldUpdateUserPassword() {
        // given
        UserChangePasswordForm input = new UserChangePasswordForm();
        input.setPassword("bartest");
        input.setConfirmPassword("bartest");

        String userLogin = "bar";
        setPrincipals(userLogin);

        User model = new User();
        when(repository.findByEmail(userLogin)).thenReturn(model);

        when(passwordEncoder.encode(any())).thenReturn("saltyPassword");

        // when
        ResponseMessage message = controller.changeUserPassword(input);

        // then
        assertEquals(ResponseStatus.OK, message.getStatus());
        assertNotNull(message.getMessage());

        verify(repository).save(model);
    }

    @Test
    public void shouldThrowExceptionWhenUserIsNotPErsistedInLocalDatabase() {
        // given
        UserChangePasswordForm input = new UserChangePasswordForm();
        input.setPassword("bartest");
        input.setConfirmPassword("bartest");

        String userLogin = "bar";
        setPrincipals(userLogin);

        when(repository.findByEmail(userLogin)).thenReturn(null);

        when(passwordEncoder.encode(any())).thenReturn("saltyPassword");

        // when
        try {
            controller.changeUserPassword(input);
            fail("Should throw security exception");
        } catch (Exception ex) {
            // ok
        }
        // then
        verify(repository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }


    private void setPrincipals(Object object) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(object);
        SecurityContext securityContext = mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void should_extract_exception_message_to_response() {
        // given
        IllegalArgumentException ex = new IllegalAddException("Required field");

        // when
        ResponseMessage response = controller.handleException(ex);

        // then
        assertNotNull(response.getMessage());
        assertEquals(ResponseStatus.ERROR, response.getStatus());
    }
}