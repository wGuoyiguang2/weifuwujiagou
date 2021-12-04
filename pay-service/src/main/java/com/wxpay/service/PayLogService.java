package com.wxpay.service;


import com.baomidou.mybatisplus.service.IService;
import com.wxpay.entity.PayLog;

/*****
 * @Author:
 * @Description:
 ****/
public interface PayLogService extends IService<PayLog> {

    void add(PayLog payLog);
}
