package com.solidify.oauth2.social;

import java.io.Serializable;

public class UserProfile implements Serializable {

    private final String id;
    private final String name;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String username;
    private final String imageUrl;

    public UserProfile(String id, String name, String firstName, String lastName, String email, String username, String imageUrl) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }
}