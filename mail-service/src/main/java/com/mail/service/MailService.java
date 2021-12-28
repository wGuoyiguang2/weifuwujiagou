package com.mail.service;

import com.mail.entity.MailVo;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

/**
 * @author guoyiguang
 * @description $
 * @date 2021/12/28$
 */
public interface MailService {

    /**
     * 功能描述 发送邮件
     * @author guoyiguang
     * @date 2021/12/28
     * @param
     * @return
     */
    MailVo sendMail(MailVo mailVo);



    void sendAttachmentMail(String to, String subject, String html, String filePath) throws UnsupportedEncodingException, MessagingException;
}
