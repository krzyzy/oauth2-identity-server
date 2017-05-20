package com.solidify.oauth2.user.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PasswordChangeController {

    private final PasswordResetService service;

    @Autowired
    public PasswordChangeController(PasswordResetService service) {
        this.service = service;
    }

    @RequestMapping(value = "/password/forgot-password", method = RequestMethod.GET)
    public String getForgotPasswordForm(Model model) {
        model.addAttribute("form", new ForgotPasswordForm());
        return "password/forgot-password-full";
    }

    @RequestMapping(value = "/password/forgot-password", method = RequestMethod.POST)
    public String submitForgotPasswordForm(@ModelAttribute ForgotPasswordForm form, Model model) {
        if (form.getEmail() == null) {
            model.addAttribute("error", "Required email address");
        } else {
            service.sendPasswordChangeToken(form.getEmail());
            model.addAttribute("message", "Email has been send.");
        }

        return "password/forgot-password-full";
    }

    @RequestMapping(value = "/password/reset/{token}", method = RequestMethod.GET)
    public String getChangePasswordForm(@PathVariable("token") String token, Model model) {
        model.addAttribute("form", new ChangePasswordForm());
        return "password/form";
    }

    @RequestMapping(value = "/password/reset/", method = RequestMethod.POST)
    public String submitChangePassword(@ModelAttribute ChangePasswordForm form) {
        return "password/form";
    }
}
