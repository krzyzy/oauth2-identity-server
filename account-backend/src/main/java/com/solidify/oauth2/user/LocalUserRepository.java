package com.solidify.oauth2.user;

import org.springframework.data.repository.CrudRepository;

public interface LocalUserRepository extends CrudRepository<LocalUser, Long> {

    LocalUser findByLogin(String login);
}