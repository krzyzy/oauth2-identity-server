package com.solidify.oauth2.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimeProvider {

    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }

}
