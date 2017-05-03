package com.solidify.oauth2.registration;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UserRegistrationControllerTest {
    private UserRegistrationService service = mock(UserRegistrationService.class);
    private UserRegistrationController controller = new UserRegistrationController(service);

    @Test
    public void should_register_new_user() {
        // given
        RegistrationForm form = createRegistrationForm();
        // when
        controller.submitRegistrationForm(form);
        // then
        verify(service).registerUser(form);
    }

    @Test
    public void should_process_verification_token() {
        final String token = "1";

        // when
        controller.registerToken(token);

        // then
        verify(service).registerToken(token);
    }

    private RegistrationForm createRegistrationForm() {
        RegistrationForm form = new RegistrationForm();
        form.setEmail("cartman@gmail.com");
        form.setFirstName("Tom");
        form.setLastName("Cruise");
        form.setHasAcceptedPolicy(Boolean.TRUE);
        form.setPassword(new char[]{'a', 'b', 'c', 'd'});
        form.setRetypedPassword(new char[]{'a', 'b', 'c', 'd'});
        return form;
    }
}