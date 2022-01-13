package com.order.entity;

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
@TableName("o_order")
public class Oorder {

    private String id;
    private Integer status;
    private String content;
    private String description;
    private Date createTime;
}