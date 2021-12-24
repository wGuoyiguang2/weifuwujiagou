package com.pay.controller;



import com.pay.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*****
 * @Author:
 * @Description:
 ****/
@RestController
@RequestMapping(value = "/pay-order")
@CrossOrigin
@Slf4j
public class PayOrderController {

    @Autowired
    OrderService orderService ;

    
    /**
     *
     *  update order status
     **/
    @RequestMapping(value = "/updateOrderStatus")
    public String updateOrderStatus(String orderNo,String status) throws Exception {
        log.info("enter in updateOrderStatus params : orderNo {} ,status {} ",orderNo,status);
        String result = orderService.updateOrderStatus(orderNo, status);
        return result;
    }











}
