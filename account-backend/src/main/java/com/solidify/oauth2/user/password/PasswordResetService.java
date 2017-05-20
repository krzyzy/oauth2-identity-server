package com.solidify.oauth2.user.password;

import com.solidify.oauth2.mail.MailService;
import com.solidify.oauth2.user.LocalUser;
import com.solidify.oauth2.user.LocalUserRepository;
import com.solidify.oauth2.user.TokenService;
import com.solidify.oauth2.user.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.solidify.oauth2.user.UserToken.TokenType.PASSWORD_CHANGE;
import static java.util.Collections.singletonList;

@Service
public class PasswordResetService {

    private final LocalUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final TokenService tokenService;

    @Autowired
    public PasswordResetService(LocalUserRepository userRepository,
                                PasswordEncoder passwordEncoder,
                                MailService mailService,
                                TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.tokenService = tokenService;
    }

    void sendPasswordChangeToken(String email) {
        LocalUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        UserToken token = tokenService.createToken(PASSWORD_CHANGE);
        token.setUser(user);
        user.getRegistrationTokens().add(token);
        sendTokenEmail(user, token.getToken());

        userRepository.save(user);
        tokenService.save(token);
    }

    void resetUserPassword(String tokenId, char[] newPassword) {
        UserToken token = tokenService.getToken(tokenId, PASSWORD_CHANGE);
        LocalUser user = token.getUser();
        user.setPassword(passwordEncoder.encode(String.copyValueOf(newPassword)));
        userRepository.save(user);
        tokenService.expireToken(token);
    }

    private void sendTokenEmail(LocalUser user, String token) {
        mailService.send(
                singletonList(user.getEmail()),
                "Password reset token",
                "You have requested for a password change. Your token is: " + token);
    }

}
