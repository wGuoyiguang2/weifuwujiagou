package com.order;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

// exclude = { DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class }
@SpringBootApplication()
// 开启服务注册发现功能
@EnableDiscoveryClient

@MapperScan(basePackages = "com.order.mapper")
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @Bean
   public RestTemplate getTemplate(){
        return new RestTemplate();

    }

}
