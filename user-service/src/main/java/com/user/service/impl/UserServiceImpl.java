package com.user.service.impl;


import com.user.service.UserService;
import com.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author guoyiguang
 * @description $
 * @date 2022/1/12$
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;



   // @GlobalTransactional(name = "updateOrderInfoAfterPay",rollbackFor = Exception.class)
    //@Transactional
    @Override
  public Integer updateUserById(String id, Integer status) {



        int a =  1/0;
        return a;
    }
}
