package com.solidify.oauth2.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserRepository repository;
    private final UserTransformer toDto;

    @Autowired
    public UserController(UserRepository repository, UserTransformer toDto) {
        this.repository = repository;
        this.toDto = toDto;
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

    private Object getUserPrincipals() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
