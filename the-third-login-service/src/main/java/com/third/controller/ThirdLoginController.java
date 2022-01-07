package com.third.controller;


import com.alibaba.cloud.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * @author guoyiguang   mian  MI
 * @description $
 * @date 2021/12/29$
 */

@RestController
@RequestMapping("/third")
public class ThirdLoginController {


    @Autowired
    RestTemplate restTemplate ;


    /**
     * 功能描述 http://192.168.5.213/api/ec-levault/zentao/aa?system=yapi&tokenValue=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjU5MSwiaWF0IjoxNjQwNzc4MDIzLCJleHAiOjE2NDEzODI4MjN9.FjVQ4MAe_ONbn7I4FgIKvo9jIR5Geyt9L576KuR3KwU
     * @author guoyiguang
     * @date 2021/12/29
     * @param
     * @return
     */
    @RequestMapping(value = "/aa", method = RequestMethod.GET)
    public String dis(String system,String tokenValue,String uid, HttpServletResponse res) throws IOException {

        Cookie cookie00 = new Cookie("1_yapi_token",tokenValue);

        Cookie cookie01 = new Cookie("_yapi_token",tokenValue);

        res.addCookie(cookie00);
        res.addCookie(cookie01);

        if (StringUtils.isNotBlank(system)){
            if (system.equals("zentao")){
                // 解决跨域的时候，cookie 无法写入问题
                res.sendRedirect("http://zentao.cloudnk.cn/my?zentaosid=df0a922732939f34cfa58aab06a25132");
            }else if (system.equals("yapi")){
                // http://localhost/zentao/aa?system=yapi

                Cookie cookie0 = new Cookie("_yapi_token",tokenValue);
                cookie0.setDomain("192.168.5.213");
                cookie0.setPath("/");
                Cookie cookie1 = new Cookie("_yapi_uid",uid);
                cookie1.setDomain("192.168.5.213");
                cookie1.setPath("/");

                res.addCookie(cookie0);
                res.addCookie(cookie1);

                res.sendRedirect("http://192.168.5.213:31010/group/403");
            }else if (system.equals("jenkins")){
                // get cookie  url
                // post  j_username=guoyiguang6&j_password=123456&from=%2F&Submit=%E7%99%BB%E5%BD%95

//                ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8080/j_spring_security_check", String.class);
//                System.out.println(responseEntity.getBody());



                Cookie cookie0 = new Cookie("JSESSIONID.b98ee2a5",tokenValue);
                cookie0.setDomain("localhost");
                //cookie0.setDomain("192.168.60.84");
                cookie0.setPath("/");
                res.addCookie(cookie0);


                Cookie cookie1 = new Cookie("aa","aa");
                cookie1.setDomain("localhost");
                //cookie1.setDomain("192.168.60.84");
                cookie1.setPath("/");
                res.addCookie(cookie1);



                // url de dizhi buyiding shi houtai
                // cookie shezhi chneggonghou  url de houtai  qingqiu jiu hui zai request header  xianshi
                // denglu de shihou  zhiyou j shengxiao
               res.sendRedirect("http://localhost:8080/asynchPeople/");




            }else if (system.equals("sonarqube")){


                Cookie cookie0 = new Cookie("JWT-SESSION",tokenValue);
                cookie0.setDomain("localhost");
                //cookie0.setDomain("192.168.60.84");
                cookie0.setPath("/");
                res.addCookie(cookie0);


                Cookie cookie1 = new Cookie("sonarqube","sonarqube");
                cookie1.setDomain("localhost");
                //cookie1.setDomain("192.168.60.84");
                cookie1.setPath("/");
                res.addCookie(cookie1);



                res.sendRedirect("http://localhost:9000/projects/");

            }



            return  "hh";

// http://192.168.5.213/api/ec-levault/zentao/aa?system=yapi&uid=591&tokenValue=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjU5MSwiaWF0IjoxNjQwNzc5NjE2LCJleHAiOjE2NDEzODQ0MTZ9.DN8pul1Pn0PRAIDxlFMFurrIimm3MD63EsDHE7Ekzz8


        }






        return null ;
    }
}
