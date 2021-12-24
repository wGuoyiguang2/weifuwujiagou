package com.pay.service.impl;

import com.pay.api.OrderFeignApi;
import com.pay.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
