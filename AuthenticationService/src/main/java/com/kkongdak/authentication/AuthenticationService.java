package com.kkongdak.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AuthenticationService {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationService.class, args);
    }
}