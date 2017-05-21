package com.solidify.oauth2.mail;


import java.util.List;

public interface MailService {

    void send(List<String> recipients, String body, String title);
}
