package com.solidify.oauth2.user;

import com.solidify.oauth2.view.profile.LocalCredentialsChangeForm;
import com.solidify.oauth2.view.profile.LocalUserDto;
import com.solidify.oauth2.view.profile.UserProfileViewController;
import com.solidify.oauth2.view.profile.UserTransformer;
import com.solidify.oauth2.common.web.ResponseMessage;
import com.solidify.oauth2.common.web.ResponseStatus;
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
public class LocalUserProfileViewControllerTest {
    LocalUserRepository repository = mock(LocalUserRepository.class);
    UserTransformer toDto = mock(UserTransformer.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    UserProfileViewController controller = new UserProfileViewController(repository, toDto, passwordEncoder);

    @Test
    public void shouldReturnLocalUser() {
        // given
        final String userName = "bar@gmail.com";
        setPrincipals(userName);

        LocalUserDto userDetails = mock(LocalUserDto.class);

        when(repository.findByLogin(userName)).thenReturn(mock(LocalUser.class));
        when(toDto.apply(any(LocalUser.class))).thenReturn(userDetails);
        // when

        Object userPrincipals = controller.user();

        // then
        assertEquals(userDetails, userPrincipals);
        verify(repository).findByLogin(userName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectEmptyUpdatePasswordForm() {
        // given
        LocalCredentialsChangeForm input = new LocalCredentialsChangeForm();

        // when
        controller.changeUserPassword(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectInvalidUpdatePasswordForm() {
        // given
        LocalCredentialsChangeForm input = new LocalCredentialsChangeForm();
        input.setPassword("1");
        input.setConfirmPassword("2");

        // when
        controller.changeUserPassword(input);
    }

    @Test
    public void shouldUpdateUserPassword() {
        // given
        LocalCredentialsChangeForm input = new LocalCredentialsChangeForm();
        input.setPassword("bartest");
        input.setConfirmPassword("bartest");

        String userLogin = "bar";
        setPrincipals(userLogin);

        LocalUser model = new LocalUser();
        when(repository.findByLogin(userLogin)).thenReturn(model);

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
        LocalCredentialsChangeForm input = new LocalCredentialsChangeForm();
        input.setPassword("bartest");
        input.setConfirmPassword("bartest");

        String userLogin = "bar";
        setPrincipals(userLogin);

        when(repository.findByLogin(userLogin)).thenReturn(null);

        when(passwordEncoder.encode(any())).thenReturn("saltyPassword");

        // when
        try {
            controller.changeUserPassword(input);
            fail("Should throw security exception");
        } catch (Exception ex) {
            // ok
        }
        // then
        verify(repository, never()).save(any(LocalUser.class));
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