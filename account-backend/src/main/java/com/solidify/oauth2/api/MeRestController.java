package com.solidify.oauth2.api;

import com.solidify.oauth2.user.LocalUserRepository;
import com.solidify.oauth2.view.profile.UserTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by tomasz on 18.03.17.
 */
@Controller
public class MeRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeRestController.class);

    private final LocalUserRepository repository;
    private final UserTransformer toDto;

    @Autowired
    public MeRestController(LocalUserRepository repository, UserTransformer toDto) {
        this.repository = repository;
        this.toDto = toDto;
    }

    @RequestMapping("/api/me")
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

    private Object getUserPrincipals() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
