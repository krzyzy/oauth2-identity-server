package com.solidify.oauth2.security;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Access(AccessType.FIELD)
public class User {

    @Id
    @Column(name = "id")
    private Long id;

    @Email
    @Column(name = "email", updatable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled")
    private Boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
