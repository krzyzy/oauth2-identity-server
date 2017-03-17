package com.solidify.oauth2.user.local;

import com.solidify.oauth2.web.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static java.util.Optional.ofNullable;
import static java.util.stream.StreamSupport.stream;

@RestController
public class UserProfileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileController.class);

    private final LocalUserRepository repository;
    private final UserTransformer toDto;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserProfileController(LocalUserRepository repository, UserTransformer toDto, PasswordEncoder passwordEncoder) {
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
                    repository.findByLogin(userPrincipals.toString())
            );
        }
        LOGGER.debug("Authenticated by external source user, requesting details");
        return userPrincipals;
    }

    @RequestMapping(value = "/api/user/password", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage changeUserPassword(@RequestBody LocalCredentialsChangeForm form) {
        validatePasswordChangeForm(form);

        LOGGER.info("Requested user password change");

        LocalUser localUser = getUser();
        localUser.setPassword(passwordEncoder.encode(form.getPassword()));

        repository.save(localUser);
        LOGGER.info("Changed password for user {}", localUser.getId());
        return ResponseMessage.createOk("Password has been updated");
    }

    private void validatePasswordChangeForm(@RequestBody LocalCredentialsChangeForm form) {
        if (ofNullable(form.getPassword()).map(String::trim).orElse("").isEmpty()) {
            throw new IllegalArgumentException("New password is required");
        }
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }

    @RequestMapping(value = "/api/user/profile", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage changeUserProfile(@RequestBody UserProfileForm form) {

        LocalUser localUser = getUser();
        LOGGER.info("Requested user profile change");
        // TODO: update identity attributes

        repository.save(localUser);
        LOGGER.info("Updated profile for user {}", localUser.getLogin());
        return ResponseMessage.createOk("Profile has been updated");
    }

    private LocalUser getUser() {
        Object userPrincipals = getUserPrincipals();
        if (userPrincipals instanceof String) {
            return ofNullable(repository.findByLogin(userPrincipals.toString()))
                    .orElseThrow(() -> new SecurityException("User could not be found in local resources"));
        }
        throw new SecurityException("User could not be found in local resources");
    }

    private Object getUserPrincipals() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseMessage handleException(IllegalArgumentException ex) {
        return ResponseMessage.createError(ex.getMessage());
    }

}
