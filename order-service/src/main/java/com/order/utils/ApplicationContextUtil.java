package com.order.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author guoyiguang
 * @description $
 * @date 2021/12/24$
 */

@Component
@Slf4j
public class ApplicationContextUtil implements ApplicationContextAware {


    // 声明一个静态变量保存
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("applicationContext正在初始化,application:{}",applicationContext);
        this.applicationContext=applicationContext;
    }

    public static <T> T getBean(Class<T> clazz){
        if(applicationContext==null){
            log.info("applicationContext是空的");
        }else{
            log.info("applicationContext不是空的");
        }
        return applicationContext.getBean(clazz);
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

}
