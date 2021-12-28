package com.mail.config;

import lombok.Data;
import org.apache.tomcat.util.digester.DocumentProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * @author guoyiguang   TODO  这个貌似没有读取到
 * @description $
 * @date 2021/12/28$
 */
@ConfigurationProperties(prefix = "spring.mail")
@Component
@Data
public class MailProperties {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private String host;
    private Integer port;
    private String username;
    // ① 授权码 ② 后者 发件人邮箱登录密码也可
    private String password;
    private String protocol = "smtp";
    // 发件人真实邮箱
    private String from;
    private Charset defaultEncoding = DEFAULT_CHARSET;
    private Map<String, String> properties = new HashMap<>();
}