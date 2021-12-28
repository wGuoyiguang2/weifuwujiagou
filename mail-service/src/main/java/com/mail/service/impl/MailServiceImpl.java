package com.mail.service.impl;

import com.mail.entity.MailVo;
import com.mail.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.ParseException;
import java.io.File;
import java.io.UnsupportedEncodingException;
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

    public MailVo sendMail(MailVo mailVo) {
        try {
            checkMail(mailVo);
            sendMimeMail(mailVo);
            return saveMail(mailVo);
        } catch (Exception e) {
            log.error("发送邮件失败:", e);
            mailVo.setStatus("fail");
            mailVo.setError(e.getMessage());
            return mailVo;
        }

    }

    @Override
    public void sendAttachmentMail(String to, String subject, String html, String filePath) throws UnsupportedEncodingException, MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom("Guoyiguang2@126.com", "personal");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        FileSystemResource file=new FileSystemResource(new File(filePath));
        String fileName=filePath.substring(filePath.lastIndexOf(File.separator));
        messageHelper.addAttachment(fileName,file);
        mailSender.send(mimeMessage);


    }


    private void checkMail(MailVo mailVo) {
        if (StringUtils.isEmpty(mailVo.getTo())) {
            throw new RuntimeException("邮件收信人不能为空");
        }
        if (StringUtils.isEmpty(mailVo.getSubject())) {
            throw new RuntimeException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(mailVo.getText())) {
            throw new RuntimeException("邮件内容不能为空");
        }
    }


    private void sendMimeMail(MailVo mailVo) {
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            mailVo.setFrom(getMailSendFrom());
            messageHelper.setFrom(getMailSendFrom());
            messageHelper.setTo(mailVo.getTo().split(","));
            messageHelper.setSubject(mailVo.getSubject());
            messageHelper.setText(mailVo.getText());
            if (!StringUtils.isEmpty(mailVo.getCc())) {
                messageHelper.setCc(mailVo.getCc().split(","));
            }
            if (!StringUtils.isEmpty(mailVo.getBcc())) {
                messageHelper.setCc(mailVo.getBcc().split(","));
            }
            if (mailVo.getMultipartFiles() != null) {
                for (MultipartFile multipartFile : mailVo.getMultipartFiles()) {
                    messageHelper.addAttachment(multipartFile.getOriginalFilename(), multipartFile);
                }
            }
            if (StringUtils.isEmpty(mailVo.getSentDate())) {
                mailVo.setSentDate(LocalDateTime.now());
                messageHelper.setSentDate(new Date());
            }
            mailSender.send(messageHelper.getMimeMessage());
            mailVo.setStatus("ok");
            log.info("发送邮件成功：{}->{}", mailVo.getFrom(), mailVo.getTo());
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

        }


        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private MailVo saveMail(MailVo mailVo) {

        return mailVo;
    }


    public String getMailSendFrom() {
        return mailSender.getJavaMailProperties().getProperty("from");
    }

}
