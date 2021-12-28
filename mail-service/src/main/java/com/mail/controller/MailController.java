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





    @PostMapping(value = "/sendMail")
    public MailVo sendMail(@RequestBody  MailVo mailVo) throws Exception {
        MailVo mailVo1 = mailService.sendMail(mailVo);
        return mailVo1;
    }



    @PostMapping(value = "/sendAttachmentMail")
    public void sendAttachmentMail(String to, String subject, String html, String filePath) throws Exception {
        // String to, String subject, String html, String filePath
        mailService.sendAttachmentMail( to,  subject,  html,  filePath);
    }












}
