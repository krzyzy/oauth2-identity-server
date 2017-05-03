package com.solidify.oauth2.registration;

import com.solidify.oauth2.user.LocalUser;
import com.solidify.oauth2.user.LocalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserRegistrationService {

    private final LocalUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserTokenRepository tokenRepository;

    @Autowired
    public UserRegistrationService(LocalUserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   UserTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    void registerUser(UserRegistrationRequest form) {
        LocalUser user = createUser(form);

        if (userRepository.findByEmail(user.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Email already taken");
        }

        UserToken token = createUserToken();
        token.setUser(user);
        user.getTokens().add(token);

        userRepository.save(user);
        tokenRepository.save(token);
    }

    void registerToken(String tokenId) {
        UserToken token = tokenRepository.findOne(tokenId);
        if (token == null) {
            throw new IllegalArgumentException("Invalid token");
        }
        if (token.isExpired()) {
            throw new IllegalArgumentException("Token is expired");
        }
        token.setExpired(true);
        LocalUser user = token.getUser();
        user.setEnabled(Boolean.TRUE);
        userRepository.save(user);
        tokenRepository.save(token);
    }

    private UserToken createUserToken() {
        UserToken token = new UserToken();
        token.setToken(UUID.randomUUID().toString());
        token.setExpired(Boolean.FALSE);
        return token;
    }

    private LocalUser createUser(UserRegistrationRequest form) {
        LocalUser user = new LocalUser();
        user.setLogin(form.getEmail());
        user.setEmail(form.getEmail());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setPassword(passwordEncoder.encode(String.copyValueOf(form.getPassword())));
        return user;
    }
}
