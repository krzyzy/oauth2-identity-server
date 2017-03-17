package com.solidify.oauth2.user.local;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileForm {

    private String login;

    private String email;

    private String firstName;

    private String lastName;

}
