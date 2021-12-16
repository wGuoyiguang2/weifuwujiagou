package com.pay.service;


import com.pay.entity.PayLog;
import java.util.Map;

public interface WeixinPayService {

    // 预支付订单创建方法------>(目的)获取支付地址
    Map<String,String> preOrder(Map<String,String> dataMap) throws Exception;


    /***
     * 退款申请操作
     */
    Map<String,String> refund(Map<String,String> dataMap) throws Exception;


    /****
     * 主动查询支付结果
     * outno:订单编号
     */
    PayLog result(String outno) throws Exception;
}
