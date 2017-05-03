package com.solidify.oauth2.registration;

interface UserRegistrationRequest {

    String getFirstName();

    String getLastName();

    String getEmail();

    char[] getPassword();

}
