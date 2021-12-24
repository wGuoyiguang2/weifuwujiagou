package com.pay.feignFailback;


import com.pay.api.OrderFeignApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * bu chang
 */
@Component
@Slf4j
public class OrderFeignFailback implements OrderFeignApi {

    @Override
    public String updateOrderStatus(String orderNo, String status) {
        log.error("diao yong  order-service  failed!!");
        return null;
    }
}
