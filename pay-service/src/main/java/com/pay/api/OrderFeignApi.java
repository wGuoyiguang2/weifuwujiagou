package com.pay.api;


import com.netflix.client.ClientException;
import com.pay.config.FeignConfig;
import com.pay.feignFailback.OrderFeignFailback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 */
@FeignClient(
        //  order-service target micro service name
        name = "order-service",
        fallback = OrderFeignFailback.class,
        // package  headers to next  micro service request
        configuration = FeignConfig.class)
public interface OrderFeignApi {

    /**
     * jia  @RequestParam de  yuanyin :Method has too many Body parameters
     *
     *  update order status
     **/
    @RequestMapping(value = "/order/updateOrderStatus")
    String updateOrderStatus(@RequestParam("orderNo") String orderNo, @RequestParam("status") String status);



    /**
     * jia  @RequestParam de  yuanyin :Method has too many Body parameters
     *
     *  update order status
     **/
    @RequestMapping(value = "/order/updateOrderById")
    Integer updateOrderById(@RequestParam("id") String id, @RequestParam("status") Integer status) ;




}
