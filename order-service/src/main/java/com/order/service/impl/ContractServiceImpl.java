package com.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.order.service.ContractService;
import com.order.utils.GuavaRetryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@Service

@Slf4j
public class ContractServiceImpl implements ContractService {


    @Autowired
    RestTemplate  restTemplate ;

    @Override
    public Boolean sendContractRetry(String url, String orderNo) throws ExecutionException, RetryException {

        Retryer<Boolean> incrementingRetryer = GuavaRetryUtil.getIncrementingRetryer();

        //定义请求实现
        Callable<Boolean> callable = new Callable<Boolean>() {
            int times = 1;

            @Override
            public Boolean call() throws Exception {
                log.info("重试调用={}", times);
                times++;
                return sendContract(url,orderNo,times);
            }
        };
        //利用重试器调用请求
        // 底层实现：  ExecutorService executor  有的策略底层是 线程池，比如 直接重试 ； 有休眠 的话 Thread.sleep(sleepTime) ，会让当前线程进入休眠
        //  Future future = this.executor.submit(callable);
        //
        //        try {
        //            return future.get(timeoutDuration, timeoutUnit);

        Boolean result = null;
        // 手动 try catch 防止 重试异常
        try {
            result = incrementingRetryer.call(callable);
        } catch (ExecutionException e) {
            log.error("发送合同重试异常，异常原因{}",e.getMessage());
            return false ;
        } catch (RetryException e) {
            log.error("发送合同重试异常，异常原因{}",e.getMessage());
            return false ;
        }
        return result;

    }


    /**
     * 发送业务方法 模拟
     *
     * @return
     */
    public Boolean sendContract(String url, String orderNo,Integer retryTimes) {

        ResponseEntity<String> responseEntity = null;
        try {

           // retryTimes":6,"orderNo":"VIP20211215000001"
            // 外部系统一般都要 try catch
            url= url+"?orderNo="+orderNo+"&retryTimes="+retryTimes;
            log.info("开始调用第三方接口 入参为 {}",url );
            responseEntity = restTemplate.getForEntity(url, String.class);
            log.info("调用第三方接口结束 返回结果为 {}", JSON.toJSONString(responseEntity));
        } catch (RestClientException e) {
            log.error("连接异常，异常原因 {}",e.getMessage());
            return false;
        }
        log.info("调用 支付服务 返回报文  {}",responseEntity);
        if (responseEntity.getBody().contains("订单数据修改成功")){
            return true;
        }
        return false;
    }
}
