package com.pay.service;


import com.baomidou.mybatisplus.service.IService;
import com.pay.entity.PayLog;

/*****
 * @Author:
 * @Description:
 ****/
public interface PayLogService extends IService<PayLog> {

    void add(PayLog payLog);
}
