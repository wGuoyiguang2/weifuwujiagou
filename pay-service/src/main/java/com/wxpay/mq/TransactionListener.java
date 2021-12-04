package com.wxpay.mq;

import com.alibaba.fastjson.JSON;
import com.wxpay.entity.PayLog;
import com.wxpay.service.PayLogService;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/*****
 * @Author:
 * @Description:
 ****/
@Component
@RocketMQTransactionListener(txProducerGroup = "rocket")
public class TransactionListener implements RocketMQLocalTransactionListener{

    @Autowired
    private PayLogService payLogService;


    /****
     * 当向RocketMQ的Broker发送Half消息成功之后，调用该方法
     * @param msg:发送的消息
     * @param arg:额外参数
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            //========================本地事务控制===================
            //消息
            String result = new String((byte[]) msg.getPayload(),"UTF-8");
            PayLog payLog = JSON.parseObject(result,PayLog.class);
            // 其他的 方整合到 add 里面
            payLogService.add(payLog);
            //========================本地事务控制===================
        } catch (Exception e) {
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        return RocketMQLocalTransactionState.COMMIT;
    }

    /***
     * 超时回查
     * @param msg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        return RocketMQLocalTransactionState.COMMIT;
    }
}
