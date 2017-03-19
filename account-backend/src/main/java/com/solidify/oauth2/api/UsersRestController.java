package com.solidify.oauth2.api;

import com.solidify.oauth2.user.LocalUserRepository;
import com.solidify.oauth2.view.profile.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by tomasz on 18.03.17.
 */
@Controller
public class UsersRestController {

    private final LocalUserRepository repository;
    private final UserTransformer toDto;

    @Autowired
    public UsersRestController(LocalUserRepository repository, UserTransformer toDto) {
        this.repository = repository;
        this.toDto = toDto;
    }
}
