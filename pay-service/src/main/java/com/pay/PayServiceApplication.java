package com.pay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
//excludeFilters这里的意思是，只要标有ExcludeFromComponetScan注解的类都不会去扫描
//@ComponentScan(value = "com", excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value= ExcludeFromComponetScan.class)})
@MapperScan(basePackages = "com.pay.mapper")
// bukaiqi  feign  diaoyong shibai
@EnableFeignClients
public class PayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayServiceApplication.class, args);
    }




}
