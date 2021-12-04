package com.pay;

import com.github.wxpay.sdk.WXPay;
import com.pay.config.WeixinPayConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayServiceApplication.class, args);
    }


    @Bean
    public WXPay wxPay(WeixinPayConfig weixinPayConfig) throws Exception {
        return new WXPay(weixinPayConfig);
    }

}
