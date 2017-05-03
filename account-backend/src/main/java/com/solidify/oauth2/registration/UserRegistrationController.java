package com.solidify.oauth2.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

@Controller
public class UserRegistrationController {

    private final UserRegistrationService service;

    @Autowired
    public UserRegistrationController(UserRegistrationService service) {
        this.service = service;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String submitRegistrationForm(@ModelAttribute RegistrationForm form) {
        validateRegistrationForm(form);

        service.registerUser(form);

        return "registration/success";
    }

    @RequestMapping(value = "/registration/{token}", method = RequestMethod.GET)
    public String registerToken(@PathVariable("token") String token) {
        service.registerToken(token);
        return "registration/token-registered";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String getRegistrationView(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "registration";
    }

    private void validateRegistrationForm(RegistrationForm input) {
        checkArgument(isNotEmpty(input.getEmail()), "Email is required");
        checkArgument(isNotEmpty(input.getFirstName()), "First name is required");
        checkArgument(isNotEmpty(input.getLastName()), "Last name is required");
        checkArgument(isNotEmpty(input.getPassword()), "Password is required");
        checkArgument(isNotEmpty(input.getRetypedPassword()), "Retyped password is required");
        checkArgument(Arrays.equals(input.getPassword(), input.getRetypedPassword()), "Passwords do not match");
    }

    private boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }

    private boolean isNotEmpty(char[] input) {
        return input != null && input.length > 0;
    }
}
