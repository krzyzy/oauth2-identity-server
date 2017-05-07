package com.solidify.oauth2.registration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

@Controller
public class UserRegistrationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationController.class);
    private final UserRegistrationService service;

    @Autowired
    public UserRegistrationController(UserRegistrationService service) {
        this.service = service;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String submitRegistrationForm(@ModelAttribute RegistrationForm form, Model model) {
        try {
            validateRegistrationForm(form);
            service.registerUser(form);
        } catch (IllegalArgumentException ex) {
            LOGGER.info("Registration form validation error {}", ex.getMessage());
            model.addAttribute("message", ex.getMessage());
            return "registration";
        }
        model.addAttribute("form", new RegistrationTokenForm());
        return "registration/token-registration-form";
    }

    @RequestMapping(value = "/registration/confirmation", method = RequestMethod.POST)
    public String registerTokenForm(@ModelAttribute RegistrationTokenForm form, Model model) {
        try {
            service.registerToken(form.getToken());
            model.addAttribute("message", "Your accont has been activated");
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "registration/token-registration-form";
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
