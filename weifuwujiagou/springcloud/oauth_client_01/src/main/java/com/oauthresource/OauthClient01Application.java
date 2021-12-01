package com.oauthresource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

@SpringBootApplication
public class OauthClient01Application {

    public static void main(String[] args) {
        SpringApplication.run(OauthClient01Application.class, args);
    }

}
