package com.solidify.oauth2.user;


import javax.persistence.*;

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

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TokenType type;

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

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public enum TokenType {
        REGISTRATION, PASSWORD_CHANGE
    }

}
