package com.gateway.service;


import com.baomidou.mybatisplus.service.IService;
import com.gateway.entity.PayLog;

/*****
 * @Author:
 * @Description:
 ****/
public interface PayLogService extends IService<PayLog> {

    void add(PayLog payLog);
}
