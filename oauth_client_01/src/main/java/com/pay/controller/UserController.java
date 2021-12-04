package com.pay.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Arrays;


@RestController
public class UserController {

    @Resource
    RestTemplate restTemplate ;


    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "i am client1"+authentication.getName() + Arrays.toString(authentication.getAuthorities().toArray());
    }


    @GetMapping("/client1-hello")
    public String hello() {
        // 获取用户信息，所以 资源服务也要启动
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "i am client1"+authentication.getName() + Arrays.toString(authentication.getAuthorities().toArray());
    }


    // 回调 URL
    // index.html 里配置了
    // <a href="http://localhost:8080/oauth/authorize?client_id=javaboy&response_type=code&scope=all&redirect_uri=http://localhost:8082/index.html">第三方登录</a>
    // 获取到code 码之后 调用下面的接口
//    @GetMapping("/index")
//    public String hello2(String code) {
//        ModelAndView modelAndView = new ModelAndView();
//        if (code != null) {
//            System.out.println("index.html controller ");
//            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//            map.add("code", code);
//            map.add("client_id", "c1");
//            map.add("client_secret", "secret");
//            map.add("redirect_uri", "http://localhost:8091/index.html");
//            map.add("grant_type", "authorization_code");
//            // 根据授权码获取令牌
//            Map<String,String> resp = restTemplate.postForObject("http://localhost:8080/oauth/token", map, Map.class);
//            String access_token = resp.get("access_token");
//            System.out.println(access_token);
//            // 根据令牌去获取资源
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Authorization", "Bearer " + access_token);
//            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
//            ResponseEntity<String> entity = restTemplate.exchange("http://localhost:8081/admin/hello", HttpMethod.GET, httpEntity, String.class);
//            //model.addAttribute("msg", entity.getBody());
//
//            // 回显数据
//            modelAndView.addObject("msg", entity.getBody());
//
//
//        }
//
//        return "index";
//    }

}
