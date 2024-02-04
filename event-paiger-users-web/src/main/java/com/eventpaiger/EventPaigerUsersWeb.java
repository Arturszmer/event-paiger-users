package com.eventpaiger;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class EventPaigerUsersWeb {
    public static void main(String[] args) {
        SpringApplication.run(EventPaigerUsersWeb.class, args);
    }
}
