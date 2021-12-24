package com.order.controller;


import com.alibaba.fastjson.JSON;
import com.order.service.ContractService;
import com.order.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/*****
 * @Author:
 * @Description:
 ****/
@RestController
@RequestMapping(value = "/order")
@CrossOrigin
@Slf4j
public class OrderController {



    @Autowired
    private ContractService contractService;

    @GetMapping(value = "/hello")
    public String pay() throws Exception {
        return "hello!I am order-service!!";
    }






    /**
     *
     *  localhost:8092/order/contractService?orderNo=VIP20211215000001
     **/
    @RequestMapping(value = "/contractService")
    public String contractService(String orderNo) throws Exception {

        String url  =  "http://" +  "localhost:" + 8094 + "/pay-service/callBack/updateOrderStatus";
        contractService.sendContractRetry(url,orderNo);
        return "contractService retry test!";
    }




    /**
     *
     *  update order status
     **/
    @RequestMapping(value = "/updateOrderStatus")
    public String updateOrderStatus(String orderNo,String status) throws Exception {
        log.info("enter in updateOrderStatus params : orderNo {} ,status {} ",orderNo,status);

        HttpServletRequest request = WebUtil.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();

        Map<String,String> result = new HashMap<>();
        result.put("orderNo",orderNo);
        result.put("status",status);
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                result.put(name, values);
            }
            // return
            return JSON.toJSONString(result);
        }


        return " call updateOrderStatus success !";
    }











}
