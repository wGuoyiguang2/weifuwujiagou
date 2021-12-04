package com.oauthresource.service;


import com.baomidou.mybatisplus.service.IService;
import com.oauthresource.entity.PayLog;

/*****
 * @Author:
 * @Description:
 ****/
public interface PayLogService extends IService<PayLog> {

    void add(PayLog payLog);
}
