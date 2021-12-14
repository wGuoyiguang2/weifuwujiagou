package com.gateway.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gateway.entity.PayLog;
import com.gateway.mapper.PayLogMapper;
import com.gateway.service.PayLogService;
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
