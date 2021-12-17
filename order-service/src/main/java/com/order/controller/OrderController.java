package com.order.controller;


import com.order.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/*****
 * @Author:
 * @Description:
 ****/
@RestController
@RequestMapping(value = "/order")
@CrossOrigin
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










}
