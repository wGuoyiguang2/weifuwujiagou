package com.pay.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/*****
 * @Author:
 * @Description:
 ****/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pay_log")
public class PayLog {

    private String id;
    private Integer status;
    private String content;
    private String payId;
    private Date createTime;
}