package com.mail.service.impl;

import com.mail.entity.MailReq;
import com.mail.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.mail.internet.AddressException;
import javax.mail.internet.ParseException;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author guoyiguang
 * @description $
 * @date 2021/12/28$
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {


    @Autowired
    private JavaMailSenderImpl mailSender;

    public MailReq sendMail(MailReq mailReq) {

        if (StringUtils.isEmpty(mailReq.getTo())) {
            throw new RuntimeException("邮件收信人不能为空");
        }
        if (StringUtils.isEmpty(mailReq.getSubject())) {
            throw new RuntimeException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(mailReq.getText())) {
            throw new RuntimeException("邮件内容不能为空");
        }
        try {
            sendMimeMail(mailReq);
            return  mailReq ;
        } catch (Exception e) {
            log.error("发送邮件失败:", e);
            mailReq.setStatus("fail");
            mailReq.setError(e.getMessage());
            return mailReq;
        }

    }


    private void sendMimeMail(MailReq mailReq) {
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            mailReq.setFrom(mailSender.getJavaMailProperties().getProperty("from"));
            messageHelper.setFrom(mailSender.getJavaMailProperties().getProperty("from"));
            messageHelper.setTo(mailReq.getTo().split(","));
            messageHelper.setSubject(mailReq.getSubject());
            messageHelper.setText(mailReq.getText());
            if (!StringUtils.isEmpty(mailReq.getCc())) {
                messageHelper.setCc(mailReq.getCc().split(","));
            }
            if (!StringUtils.isEmpty(mailReq.getBcc())) {
                messageHelper.setCc(mailReq.getBcc().split(","));
            }
            if (mailReq.getMultipartFiles() != null) {
                for (MultipartFile multipartFile : mailReq.getMultipartFiles()) {
                    messageHelper.addAttachment(multipartFile.getOriginalFilename(), multipartFile);
                }
            }
            if (StringUtils.isEmpty(mailReq.getSentDate())) {
                mailReq.setSentDate(LocalDateTime.now());
                messageHelper.setSentDate(new Date());
            }
            mailSender.send(messageHelper.getMimeMessage());
            mailReq.setStatus("ok");
            log.info("发送邮件成功：{}->{}", mailReq.getFrom(), mailReq.getTo());
        }
        catch (MailPreparationException mse) {
            log.error("发送邮件失败 , MailPreparationException：{}"    , mse.getMessage());
        }
        catch (MailParseException mse) {
            log.error("发送邮件失败 , MailParseException：{}"   , mse.getMessage());
        }
        catch (MailAuthenticationException mse) {
            //535 Error: authentication failed 发送人账号，密码，或者授权码 错误
            if ( mse.getMessage().contains("authentication failed")){
                log.error("发送邮件失败 ------>>>>>> 发送人邮箱认证失败！ ");
            }else{
                log.error("发送邮件失败 , MailAuthenticationException：{}"   , mse.getMessage());
            }
        }
        catch (MailSendException mse) {

            // javax.mail.SendFailedException: Invalid Addresses;
            log.error("发送邮件失败 , MailSendException：{}" , mse.getMessage());
            if (mse.getMessage().contains("Invalid Addresses")){
                // 入库
                log.error("发送邮件失败 , MailSendException：{}" ,"目标地址无效");
            }
        }
        catch (ParseException  mse) {
            if(mse instanceof  AddressException){
                log.error("发送邮件失败 ,目标地址不合法, AddressException：{}" + mse.getMessage());
            }else{
                log.error("发送邮件失败 , ParseException：" + mse);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




}
