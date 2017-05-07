package com.solidify.oauth2.registration;

import org.junit.Test;
import org.springframework.ui.Model;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UserRegistrationControllerTest {
    private UserRegistrationService service = mock(UserRegistrationService.class);
    private UserRegistrationController controller = new UserRegistrationController(service);

    @Test
    public void should_register_new_user() {
        // given
        RegistrationForm form = createRegistrationForm();
        Model model = mock(Model.class);
        // when
        controller.submitRegistrationForm(form, model);
        // then
        verify(service).registerUser(form);
    }

    @Test
    public void should_process_verification_token() {
        final String token = "1";
        RegistrationTokenForm form = new RegistrationTokenForm();
        form.setToken(token);
        // when
        controller.registerTokenForm(form, mock(Model.class));

        // then
        verify(service).registerToken(token);
    }

    private RegistrationForm createRegistrationForm() {
        RegistrationForm form = new RegistrationForm();
        form.setEmail("cartman@gmail.com");
        form.setFirstName("Tom");
        form.setLastName("Cruise");
        form.setPassword(new char[]{'a', 'b', 'c', 'd'});
        form.setRetypedPassword(new char[]{'a', 'b', 'c', 'd'});
        return form;
    }
}