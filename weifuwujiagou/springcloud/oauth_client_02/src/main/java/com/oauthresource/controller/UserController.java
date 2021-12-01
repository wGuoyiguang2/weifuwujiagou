package com.oauthresource.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


@RestController
public class UserController {


    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "i am client1"+authentication.getName() + Arrays.toString(authentication.getAuthorities().toArray());
    }


    @GetMapping("/client2-hello")
    public String hello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "i am client2"+authentication.getName() + Arrays.toString(authentication.getAuthorities().toArray());
    }

}
