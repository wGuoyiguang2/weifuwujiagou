package com.mail.service;

import com.mail.entity.MailReq;

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
    MailReq sendMail(MailReq mailReq);
}
