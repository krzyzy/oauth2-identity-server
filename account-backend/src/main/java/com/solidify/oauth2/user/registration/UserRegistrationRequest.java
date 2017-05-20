package com.solidify.oauth2.user.registration;

interface UserRegistrationRequest {

    String getFirstName();

    String getLastName();

    String getEmail();

    char[] getPassword();

}
