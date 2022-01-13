package com.pay.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pay.entity.PayLog;

/*****
 * @Author:
 * @Description: extends IService<PayLog>
 ****/
public interface PayLogService  {

    void add(PayLog payLog);

    String  updateOrderInfoAfterPay(String orderId,Integer status);




}
