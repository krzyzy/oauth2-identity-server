package com.solidify.oauth2.security;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    UserRepository repository = mock(UserRepository.class);
    UserTransformer toDto = mock(UserTransformer.class);

    UserController controller = new UserController(repository, toDto);

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

    private void setPrincipals(Object object) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(object);
        SecurityContext securityContext = mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}