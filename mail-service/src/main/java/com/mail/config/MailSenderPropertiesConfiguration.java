package com.mail.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @author guoyiguang  生成 JavaMailSenderImpl bean 交给 spring 管理
 * @description $
 * @date 2021/12/28$
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.mail", name = "host")
class MailSenderPropertiesConfiguration {
    private final MailProperties properties;
    MailSenderPropertiesConfiguration(MailProperties properties) {
        this.properties = properties;
    }
    @Bean
    @ConditionalOnMissingBean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(properties.getHost());
        // sender.setPort(PORT);
        sender.setUsername(properties.getUsername());
        sender.setPassword(properties.getPassword());
        sender.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        //p.setProperty("mail.smtp.timeout", timeout);
        p.setProperty("mail.smtp.auth", "false");
        p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.setProperty("from",properties.getFrom());
        // 设置发件人属性值
        sender.setJavaMailProperties(p);

        return sender;
    }


}