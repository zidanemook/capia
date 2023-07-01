package com.game.capia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CapiaApplication {

    public static void main(String[] args) {
        // Sentry.io 연결해서 !!
        SpringApplication.run(CapiaApplication.class, args);
    }

}
