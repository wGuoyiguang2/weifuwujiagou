package com.pay.controller;

import com.pay.common.RespResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/*****
 * @Author:
 * @Description:
 ****/
@Controller
@RequestMapping("pay-service/callBack")
public class CallBackController {


    @RequestMapping(value = "/aa")
    public String  aab() {

        return "aa";
    }

    @RequestMapping(value = "updateOrderStatus")
    public RespResult<Map> updateOrderStatus(String orderNo,Integer retryTimes) throws Exception {
        System.out.println("收到回调 ，订单号为 "+orderNo);
        if (retryTimes <= 5){
            return RespResult.error("订单数据修改失败!");
        }
        return RespResult.ok("订单数据修改成功!");
    }



}
