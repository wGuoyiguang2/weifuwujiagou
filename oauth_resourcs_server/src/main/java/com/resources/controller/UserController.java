package com.resources.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;


@RestController
public class UserController {

    //资源服务器提供的获取 用户信息的接口
    @GetMapping("/user")
    public Principal getCurrentUser(Principal principal) {
        return principal;
    }


    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
    @GetMapping("/admin/hello")
    public String admin() {
        return "admin";
    }

}
