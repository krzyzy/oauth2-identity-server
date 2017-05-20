package com.solidify.oauth2.user.registration;

public class RegistrationForm implements UserRegistrationRequest{

    private String firstName;

    private String lastName;

    private String email;

    private char[] password;

    private char[] retypedPassword;

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public char[] getRetypedPassword() {
        return retypedPassword;
    }

    public void setRetypedPassword(char[] retypedPassword) {
        this.retypedPassword = retypedPassword;
    }

}
