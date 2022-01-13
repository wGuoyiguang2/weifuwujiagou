package com.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.netflix.client.ClientException;
import com.pay.api.OrderFeignApi;
import com.pay.entity.PayLog;
import com.pay.mapper.PayLogMapper;
import com.pay.service.OrderService;
import com.pay.service.PayLogService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private PayLogMapper payLogMapper;

    @Autowired
    private OrderService orderService;







    /***
     * log
     * @param payLog
     */
    @Override
    public void add(PayLog payLog) {
        //删除
        payLogMapper.deleteById(payLog.getId());
        //增加
        payLogMapper.insert(payLog);
    }


    @Override
    //@GlobalTransactional
    @Transactional
    public String updateOrderInfoAfterPay(String orderId,Integer status) {

        //删除
        payLogMapper.deleteById(orderId);
        //增加
        PayLog payLog = new PayLog();
        payLog.setPayId(orderId);
        payLog.setId(orderId);
        payLog.setStatus(status);
        payLog.setCreateTime(new Date());
        payLogMapper.insert(payLog);

        // 调用外部服务，更新订单状态

        Integer i = orderService.updateOrderById(orderId, status);

        if(i == 1){
            return "success";
        }else{
            // 接入 seata 此处不需要 抛异常
            return "false";
        }


    }


}
