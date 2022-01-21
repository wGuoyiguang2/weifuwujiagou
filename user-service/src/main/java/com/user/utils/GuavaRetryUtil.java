package com.order.utils;

import com.github.rholder.retry.*;
import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;


/**
 * @program: springboot
 * @description: guava 重试工具类
 * @author: gyg
 * @create: 2021-10-24 11:13
 **/
@Slf4j
public class GuavaRetryUtil {

    public static Retryer<Boolean>  getIncrementingRetryer(){

        //定义重试机制  ，通过RetryerBuilder建造一个重试器
        Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
                // 可以设置各种各样的触发条件、要求,出现异常时(或者特定异常)重试
                // 2.方法返回值等于指定值时重试 ，（String、Integer、Boolean等等都可以，甚至对象等）
                // 3.不等待秒数直接重试 , 等待多少秒重试 ,固定等待多少秒重试 ,增长式等待秒数重试等
                //retryIf 重试条件
                .retryIfException()
                .retryIfRuntimeException()
                .retryIfExceptionOfType(Exception.class)
                .retryIfException(Predicates.equalTo(new Exception()))
                .retryIfResult(Predicates.equalTo(false))

                //等待策略：每次请求间隔1s
                //noWait不等时间直接重试
                //fixedWait 固定X秒后重试
                //randomWait 随机等待设置范围内的时间重试
                //incrementingWait 第一个参数为第一次重试时间，后面会通过设置间隔递增秒数重试

                //.withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
                // 1 ; 1+2 ; 1+2+2 ;  特点：时间间隔是固定的
                .withWaitStrategy(WaitStrategies.incrementingWait(1L,TimeUnit.SECONDS,2L ,TimeUnit.SECONDS))

                //停止策略 : 尝试请求6次
                .withStopStrategy(StopStrategies.stopAfterAttempt(6))


                //  重试监听器，每次重试调用监听器里面的方法（可以多个，按照顺序调用）
                .withRetryListener(new MyRetryListener())
                //.withRetryListener(new MyRetryListener2())
                .build();

        return retryer;



    }


    private static class MyRetryListener implements RetryListener {
        @Override
        public <V> void onRetry(Attempt<V> attempt) {
            System.out.println("回调监听器 一，当前是第："+attempt.getAttemptNumber()+ "次执行");

        }
    }

//    private static class MyRetryListener2 implements RetryListener {
//        @Override
//        public <V> void onRetry(Attempt<V> attempt) {
//            System.out.println("回调监听器 二：记录日志"+attempt.getAttemptNumber());
//        }
//    }





}
