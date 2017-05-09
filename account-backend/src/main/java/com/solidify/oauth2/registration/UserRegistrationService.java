package com.solidify.oauth2.registration;

import com.solidify.oauth2.mail.MailService;
import com.solidify.oauth2.user.LocalUser;
import com.solidify.oauth2.user.LocalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;

@Service
public class UserRegistrationService {

    private final LocalUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserTokenRepository tokenRepository;
    private final MailService mailService;

    @Autowired
    public UserRegistrationService(LocalUserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   UserTokenRepository tokenRepository,
                                   MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.mailService = mailService;
    }

    void registerUser(UserRegistrationRequest form) {
        LocalUser user = createUser(form);

        if (userRepository.findByEmail(user.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Email already taken");
        }

        UserToken token = createUserToken();
        token.setUser(user);
        user.getTokens().add(token);
        sendTokenEmail(user, token.getToken());

        userRepository.save(user);
        tokenRepository.save(token);
    }

    void registerToken(String tokenId) {
        UserToken token = tokenRepository.findOne(tokenId);
        if (token == null) {
            throw new IllegalArgumentException("Invalid token");
        }
        if (token.isExpired()) {
            throw new IllegalArgumentException("Token has expired");
        }
        if (LocalDateTime.parse(token.getExpirationDate()).isBefore(getLocalDateTime())) {
            throw new IllegalArgumentException("Token has expired");
        }
        token.setExpired(true);
        LocalUser user = token.getUser();
        user.setEnabled(Boolean.TRUE);
        userRepository.save(user);
        tokenRepository.save(token);
    }

    private LocalDateTime getLocalDateTime(){
        return LocalDateTime.now();
    }

    private void sendTokenEmail(LocalUser user, String token) {
        mailService.send(
                singletonList(user.getEmail()),
                "Registration verification",
                "Thank you for the registration. Your token is: " + token);
    }

    private UserToken createUserToken() {
        UserToken token = new UserToken();
        token.setToken(UUID.randomUUID().toString());
        token.setExpired(Boolean.FALSE);
        token.setExpirationDate(getLocalDateTime().plusMinutes(30).toString());
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
