package com.solidify.oauth2.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LocalUserRepository extends CrudRepository<LocalUser, Long> {

    LocalUser findByLogin(String login);

    Optional<LocalUser> findByEmail(String email);
}