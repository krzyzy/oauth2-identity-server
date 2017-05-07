package com.solidify.oauth2.mail;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DummyMailService implements MailService{

    @Override
    public void send(List<String> recipents, String body, String title) {

    }
}
