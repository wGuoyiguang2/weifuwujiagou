package com.order.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author guoyiguang
 * @description houqu  Request duixiang  he  Response  duixiang
 * @date 2021/12/24$
 */
public class WebUtil {

    /** 获取request对象 **/
    public static HttpServletRequest getRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null){
            return null;
        }
        return ((ServletRequestAttributes)requestAttributes).getRequest();
    }
    /** 获取response对象 **/
    public static HttpServletResponse getResponse(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null){
            return null;
        }
        return ((ServletRequestAttributes)requestAttributes).getResponse();
    }
}
