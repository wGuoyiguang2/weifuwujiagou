package com.mail.controller;


import com.mail.entity.MailVo;
import com.mail.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/*****
 * @Author:
 * @Description:
 ****/
@RestController
@RequestMapping(value = "/mail")
@CrossOrigin
@Slf4j
public class MailController {



    @Autowired
    MailService mailService ;





    @GetMapping(value = "/hello")
    public String mail() throws Exception {
        return "hello!I am order-service!!";
    }


    @PostMapping(value = "/sendMail")
    public MailVo sendMail(@RequestBody  MailVo mailVo) throws Exception {
        MailVo mailVo1 = mailService.sendMail(mailVo);
        return mailVo1;
    }









}
