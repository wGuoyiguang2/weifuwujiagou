package com.user.controller;


import com.user.entity.User;
import com.user.mapper.UserMapper;
import com.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;


/*****
 * @Author:
 * @Description:
 ****/
@RestController
@RequestMapping(value = "/user")
@CrossOrigin
@Slf4j
public class OrderController {




    @Autowired
    private UserService userService;



    @Autowired
    private UserMapper userMapper;



    @GetMapping(value = "/hello")
    public String pay() throws Exception {
        return "hello!I am order-service!!";
    }



    @GetMapping("/detail")
    public User getUserInfo(String id) {
        Optional<User> optional = userMapper.findById(id);
        return optional.orElseGet(User::new);
    }














}
