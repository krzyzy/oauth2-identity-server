package com.solidify.oauth2.security;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmailAndEnabled(String email, Boolean enabled);

}