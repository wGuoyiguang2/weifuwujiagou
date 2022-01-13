package com.pay.service.impl;

import com.pay.api.OrderFeignApi;
import com.pay.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author guoyiguang
 * @description $
 * @date 2021/12/24$
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {


    @Autowired
    OrderFeignApi orderFeignApi ;

    @Override
    public String updateOrderStatus(String orderNo, String status) {
        try {
            log.info("to  call order-service ' updateOrderStatus params : orderNo {} ,status {}",orderNo,status);
            String result = orderFeignApi.updateOrderStatus(orderNo, status);
            log.info("  called order-service ' updateOrderStatus result :  {} ",result);
            return result;
        } catch (Exception e) {
            log.error("call order-service ' updateOrderStatus failed, cause : {}",e.getMessage());
        }
        return StringUtils.EMPTY;
    }


    @Override
    public Integer updateOrderById(String id, Integer status) {

        try {
            log.info("to  call order-service ' updateOrderStatus params : orderId {} ,status {}",id,status);
            Integer result = orderFeignApi.updateOrderById(id, status);
            log.info("  called order-service ' updateOrderById result :  {} ",result);
            return result;
         } catch (Exception e) {
           log.error("call order-service ' updateOrderById failed, cause : {}",e.getMessage());
           // 抛异常的目的是防止吃掉异常
           throw new RuntimeException("修改订单状态异常");
         }

    };
}
