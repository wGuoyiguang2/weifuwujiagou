package com.mail.controller;


import com.mail.entity.MailReq;
import com.mail.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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




    /**
     * 功能描述 说明：   multipartFiles 前端传 多个 文件，后台 用 MultipartFile[] 接受
     * @author guoyiguang
     * @date 2021/12/28
     * @param
     * @return
     */
    @PostMapping(value = "/sendAttachmentMail")
    public MailReq sendAttachmentMail(String to, String subject, String text, MultipartFile[] multipartFiles) throws Exception {
        MailReq MailReq = new MailReq();
        MailReq.setTo(to);
        MailReq.setSubject(subject);
        MailReq.setText(text);
        if (multipartFiles.length > 0){
            MailReq.setMultipartFiles(multipartFiles);
        }

        MailReq mailReq1 = mailService.sendMail(MailReq);
        return mailReq1;
    }














}
