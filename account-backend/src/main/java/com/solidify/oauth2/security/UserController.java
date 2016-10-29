package com.solidify.oauth2.security;

import com.solidify.oauth2.security.resource.UserChangePasswordForm;
import com.solidify.oauth2.web.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@RestController
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserRepository repository;
    private final UserTransformer toDto;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository repository, UserTransformer toDto, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.toDto = toDto;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("/api/user")
    public Object user() {
        Object userPrincipals = getUserPrincipals();

        if (userPrincipals instanceof String) {
            LOGGER.debug("Local user authenticated, requesting details");
            return toDto.apply(
                    repository.findByEmail(userPrincipals.toString())
            );
        }
        LOGGER.debug("Authenticated by external source user, requesting details");
        return userPrincipals;
    }

    @RequestMapping(value = "/api/user/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage changeUserPassword(@RequestBody UserChangePasswordForm form) {
        // TODO: wrap in exception handler
        if (ofNullable(form.getPassword()).map(String::trim).orElse("").isEmpty()) {
            return ResponseMessage.createError("New password is required");
        }
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            return ResponseMessage.createError("Passwords do not match");
        }
        LOGGER.info("Requested user password change");

        User user = getUser().orElseThrow(() -> new SecurityException("User could not be found in local resources"));
        user.setPassword(passwordEncoder.encode(form.getPassword()));

        repository.save(user);
        LOGGER.info("Changes password for user {}", user.getEmail());
        return ResponseMessage.createOk("Password has been updated");
    }

    private Optional<User> getUser() {
        Object userPrincipals = getUserPrincipals();
        if (userPrincipals instanceof String) {
            return ofNullable(repository.findByEmail(userPrincipals.toString()));
        }
        return empty();
    }

    private Object getUserPrincipals() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
