package com.solidify.oauth2.user.registration;

import com.solidify.oauth2.mail.MailService;
import com.solidify.oauth2.user.LocalUser;
import com.solidify.oauth2.user.LocalUserRepository;
import com.solidify.oauth2.user.TokenService;
import com.solidify.oauth2.user.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Collections.singletonList;

@Service
public class UserRegistrationService {

    private final LocalUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final TokenService tokenService;

    @Autowired
    public UserRegistrationService(LocalUserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   MailService mailService,
                                   TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.mailService = mailService;
    }

    void registerUser(UserRegistrationRequest form) {
        LocalUser user = createUser(form);

        if (userRepository.findByEmail(user.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Email already taken");
        }

        UserToken token = tokenService.createToken(UserToken.TokenType.REGISTRATION);
        token.setUser(user);
        user.getRegistrationTokens().add(token);
        sendTokenEmail(user, token.getToken());
        tokenService.save(token);
        userRepository.save(user);
    }

    void activateUserByToken(String tokenId) {
        UserToken token = tokenService.getToken(tokenId, UserToken.TokenType.REGISTRATION);
        LocalUser user = token.getUser();
        user.setEnabled(Boolean.TRUE);
        userRepository.save(user);
        tokenService.expireToken(token);
    }

    private void sendTokenEmail(LocalUser user, String token) {
        mailService.send(
                singletonList(user.getEmail()),
                "Registration verification",
                "Thank you for the registration. Your token is: " + token);
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
