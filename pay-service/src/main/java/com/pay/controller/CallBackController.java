package com.pay.controller;

import com.pay.common.RespResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/*****
 * @Author:
 * @Description:
 ****/
@RestController
@RequestMapping("/pay-service/callBack")
public class CallBackController {


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



}
