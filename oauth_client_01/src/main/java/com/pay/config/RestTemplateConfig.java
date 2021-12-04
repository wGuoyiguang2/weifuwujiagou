package com.pay.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
@Configuration
public class RestTemplateConfig   {


    @Bean
    RestTemplate  getRestTempalte()  {
      return   new RestTemplate();
    }

}
