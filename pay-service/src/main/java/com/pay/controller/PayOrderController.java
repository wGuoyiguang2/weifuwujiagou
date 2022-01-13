package com.pay.controller;




import com.pay.entity.PayLog;
import com.pay.mapper.PayLogMapper;
import com.pay.service.OrderService;
import com.pay.service.PayLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

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



    @Autowired
    private PayLogMapper payLogMapper;




    
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





    /**
     *
     *  update order status
     **/
    @RequestMapping(value = "/getAllPayLogs")
    public List<PayLog> getAllPayLogs() throws Exception {
        log.info("enter in getAllPayLogs params ");
       // EntityWrapper<PayLog> wrapper = new EntityWrapper<>();
        List<PayLog> payLogs =  payLogMapper.selectList(null);

        return payLogs;
    }











}
