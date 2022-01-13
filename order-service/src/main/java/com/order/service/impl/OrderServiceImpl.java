package com.order.service.impl;

import com.order.entity.Oorder;
import com.order.mapper.OrderMapper;
import com.order.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author guoyiguang
 * @description $
 * @date 2022/1/12$
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderMapper orderMapper;



    @GlobalTransactional
    @Transactional
    @Override
  public Integer updateOrderById(String id, Integer status) {

        Oorder oorder = orderMapper.selectById(id);
        oorder.setStatus(status);

        int i = orderMapper.updateById(oorder);
        log.info("update  o_order result {}",i);

        int a =  1/0;
        return i;
    }
}
