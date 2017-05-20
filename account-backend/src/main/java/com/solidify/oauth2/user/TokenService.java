package com.solidify.oauth2.user;

import com.solidify.oauth2.user.UserToken.TokenType;
import com.solidify.oauth2.util.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenService {

    private static final int TOKEN_MAX_AGE_IN_MINUTES = 30;

    private final UserTokenRepository tokenRepository;
    private final TimeProvider timeProvider;

    @Autowired
    public TokenService(UserTokenRepository tokenRepository, TimeProvider timeProvider) {
        this.tokenRepository = tokenRepository;
        this.timeProvider = timeProvider;
    }

    public void save(UserToken token) {
        tokenRepository.save(token);
    }

    public UserToken createToken(TokenType type) {
        UserToken token = new UserToken();
        token.setToken(UUID.randomUUID().toString());
        token.setExpired(Boolean.FALSE);
        token.setType(type);
        token.setExpirationDate(timeProvider.getLocalDateTime().plusMinutes(TOKEN_MAX_AGE_IN_MINUTES).toString());
        return token;
    }

    public void expireToken(UserToken token) {
        token.setExpired(true);
        tokenRepository.save(token);
    }

    public UserToken getToken(String tokenId, TokenType type) {
        UserToken token = tokenRepository.findByTokenAndType(tokenId, type)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        if (token.isExpired()) {
            throw new IllegalArgumentException("Token has expired");
        }
        if (token.getExpirationDate() == null ||
                LocalDateTime.parse(token.getExpirationDate()).isBefore(timeProvider.getLocalDateTime())) {
            throw new IllegalArgumentException("Token has expired");
        }
        return token;
    }
}
