package com.pay.controller;


import com.pay.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/*****
 * @Author:
 * @Description:
 ****/
@RestController
@RequestMapping("/pay-service/callBack")
public class CallBackController {

    @Autowired
    PayLogService payLogService ;


    @RequestMapping(value = "/aa")
    public String  aab() {

        return "aa";
    }

    @GetMapping(value = "/updateOrderStatus")
    public String updateOrderStatus(String orderNo,Integer retryTimes) throws Exception {
        System.out.println("收到回调 ，订单号为 "+orderNo+"retryTimes 为"+retryTimes);
        if  (null != retryTimes){
            if (retryTimes <= 5){
                return "订单数据修改失败!";
            }
        }else{
            return "订单数据修改失败!" ;
        }
        return  "订单数据修改成功!" ;
    }



    @GetMapping(value = "/notifyAfterPay")
    public String notifyAfterPay(String orderId,Integer status) throws Exception {
      return payLogService.updateOrderInfoAfterPay(orderId,status);
    }



}
