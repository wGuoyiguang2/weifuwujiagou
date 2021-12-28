package com.mail.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

/**
 * @author guoyiguang
 * @description $
 * @date 2021/12/28$
 */
@Data
public class MailVo {

    private String id;
    private String from;
    private String to;
    private String subject;
    private String text;
    private LocalDateTime sentDate;
    private String cc;
    private String bcc;
    private String status;
    private String error;
    @JsonIgnore
    private MultipartFile[] multipartFiles;
}
