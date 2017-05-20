package com.solidify.oauth2.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserTokenRepository extends CrudRepository<UserToken, String> {

    Optional<UserToken> findByTokenAndType(String token, UserToken.TokenType type);

}