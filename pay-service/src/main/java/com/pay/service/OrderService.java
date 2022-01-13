package com.pay.service;

import org.springframework.web.bind.annotation.RequestParam;

public interface OrderService {


    /**
     * 功能描述 call order-service  to  updateOrderStatus
     * @author guoyiguang
     * @date 2021/12/24
     * @param
     * @return
     */
     String updateOrderStatus(String orderNo,String status);


    Integer updateOrderById(String id,Integer status) ;
}
