package com.solidify.oauth2.mail;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailServiceImpl implements MailService {

    @Override
    public void send(List<String> recipients, String body, String title) {
        //TODO: send email
    }
}
