package com.resources;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableGlobalMethodSecurity(prePostEnabled = true)
// 表示这是一个资源服务器
@EnableResourceServer
@SpringBootApplication
public class OauthResourcsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthResourcsServerApplication.class, args);
    }

}
