package com.third.controller;


import com.alibaba.cloud.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author guoyiguang
 * @description $
 * @date 2021/12/29$
 */

@RestController
@RequestMapping("/third")
public class ThirdLoginController {


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
            }

            return  "hh";

// http://192.168.5.213/api/ec-levault/zentao/aa?system=yapi&uid=591&tokenValue=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjU5MSwiaWF0IjoxNjQwNzc5NjE2LCJleHAiOjE2NDEzODQ0MTZ9.DN8pul1Pn0PRAIDxlFMFurrIimm3MD63EsDHE7Ekzz8


        }






        return null ;
    }
}
