package com.order.common;


import org.springframework.web.bind.annotation.*;


/*****
 * @Author:
 * @Description:
 ****/
@RestController
@RequestMapping(value = "/order")
@CrossOrigin
public class OrderController {



    @GetMapping(value = "/hello")
    public String pay() throws Exception {
        return "hello!I am order-service!!";
    }


}
