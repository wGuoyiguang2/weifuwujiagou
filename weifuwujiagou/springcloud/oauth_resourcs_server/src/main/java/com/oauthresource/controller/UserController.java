package com.oauthresource.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @PreAuthorize(value = "hasAnyAuthority('p1')")
    @RequestMapping("/user/p1")
    public String getAllStudents1(){
        return "getAllStudents2 ...";
    }

    @PreAuthorize(value = "hasAnyAuthority('p2')")
    @RequestMapping("/user/p2")
    public String getAllStudents2(){
        return "getAllStudents2 ...";
    }
}
