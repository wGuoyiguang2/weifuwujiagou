package com.pay.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
//MyBatisPlus表映射注解
public class OrderRefund implements Serializable {

    private String id;
    private String orderNo;
    private Integer refundType;
    private String orderSkuId;
    private String username;
    private Integer status;
    private Date createTime;
    private Integer money;
}