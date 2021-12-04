package com.wxpay.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wxpay.entity.PayLog;
import com.wxpay.mapper.PayLogMapper;
import com.wxpay.service.PayLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Resource
    private PayLogMapper payLogMapper;

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
}
