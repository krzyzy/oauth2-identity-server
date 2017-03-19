package com.solidify.oauth2.user;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Access(AccessType.FIELD)
public class LocalUser {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "login", updatable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
