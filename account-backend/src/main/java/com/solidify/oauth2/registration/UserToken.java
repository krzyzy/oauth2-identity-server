package com.solidify.oauth2.registration;


import com.solidify.oauth2.user.LocalUser;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_token")
public class UserToken {

    @Id
    @Column(name = "token")
    private String token;

    @ManyToOne
    private LocalUser user;

    @Column(name = "expired")
    private Boolean expired;

    @Column(name = "expiration_date")
    private String expirationDate;

    public LocalUser getUser() {
        return user;
    }

    public void setUser(LocalUser user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
