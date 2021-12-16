package com.pay.controller;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.pay.common.RespResult;
import com.pay.entity.PayLog;
import com.pay.service.WeixinPayService;
import com.pay.utils.AESUtil;
import com.pay.utils.Base64Util;
import com.pay.utils.MD5;
import com.pay.utils.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*****
 * @Author:
 * @Description:
 ****/
@RestController
@RequestMapping(value = "/pay-service/wx")
@CrossOrigin
public class WeixinPayController {

//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private WeixinPayService weixinPayService;

    @Autowired
    private Signature signature;

    //秘钥->MD5（skey）
    @Value("${payconfig.weixin.key}")
    private String skey;


    @GetMapping(value = "/hello")
    public RespResult<Map> pay() throws Exception {
        return RespResult.ok("hello!I am weixinpay service!!");
    }


    /****
     * 微信支付获取二维码
     */
    @GetMapping(value = "/pay")
    //public RespResult<Map> pay(@RequestParam Map<String,String> dataMap) throws Exception {
    public RespResult<Map> pay(@RequestParam("ciptext") String ciphertext) throws Exception {
        //ciphertext->AES->移除签名数据signature->MD5==signature?
        Map<String, String> dataMap = signature.security(ciphertext);

        Map<String, String> map = weixinPayService.preOrder(dataMap);
        if(map!=null){
            map.put("orderNumber",dataMap.get("out_trade_no"));
            map.put("money",dataMap.get("total_fee"));
            return RespResult.ok(map);
        }
        return RespResult.error("支付系统繁忙，请稍后再试！");
    }

    /****
     * 查询订单支付状态 （用户付款后 前端  定时任务查询  支付中，支付完成） ，（相当于还款中，还款完成）
     */
    @GetMapping(value = "/result/{outno}")
    public RespResult<PayLog> result(@PathVariable(value = "outno")String outno) throws Exception{
        PayLog payLog = weixinPayService.result(outno);
        return RespResult.ok(payLog);
    }



    /****
     * 支付结果回调
     */
    @RequestMapping(value = "/result")
    public String result(HttpServletRequest request) throws Exception{
        //读取网络输入流 (ps：是xml 格式的，传输还是字节数组)
        ServletInputStream is = request.getInputStream();

        //定义接收输入流对象（输出流）
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        //将网络输入流读取到输出流中
        byte[] buffer = new byte[1024];
        int len=0;
        while ((len=is.read(buffer))!=-1){
            os.write(buffer,0,len);
        }

        //关闭资源
        os.close();
        is.close();

        //将支付结果的XML结构转换成Map结构 （会不会 转成自定义对象？可以的）
        String xmlResult = new String(os.toByteArray(),"UTF-8");
        Map<String, String> map = WXPayUtil.xmlToMap(xmlResult);
        System.out.println("xmlResult:"+xmlResult);
        //判断支付结果状态  日志状态：2 成功 ， 7 失败
        int status = 7;
        // return_code/result_code
        if(map.get("return_code").equals(WXPayConstants.SUCCESS) && map.get("result_code").equals(WXPayConstants.SUCCESS)){
            status=2;
        }

        //创建日志对象  （ps ： out_trade_no 是自己系统里的订单号，也是订单ID，将唯一业务id作为表数据id）
        PayLog payLog = new PayLog(map.get("out_trade_no"),status,JSON.toJSONString(map),map.get("out_trade_no"),new Date());

        //构建消息
        //Message<String> message = MessageBuilder.withPayload(JSON.toJSONString(payLog)).build();
        //发消息
        //成功： (主要 是往支付记录里添加  日志 ，支付记录id为 订单id；订单状态变更)
        //失败：修改订单状态，库存回滚
        //TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction("rocket", "log", message, null);

        //Map 响应数据（ps：给 微信系统 返回 xml 格式的字符串）
        Map<String,String> resultResp = new HashMap<String,String>();
        resultResp.put("return_code","SUCCESS");
        resultResp.put("return_msg","OK");
        return WXPayUtil.mapToXml(resultResp);
    }


    /****
     * 申请退款 是在 mq 里发起的
     */



    /****
     *
     *  微信服务器返回的req_info 是加密的，解密后的字段包括
     *
     *  refund_status
     *        SUCCESS-退款成功
     *        CHANGE-退款异常
     *        REFUNDCLOSE—退款关闭
     *
     *  success_time 退款成功时间
     *
     * 2017-12-15 09:46:01
     * 发起退款申请的时候  写了回调地址
     * 官方文档上说明：需要解密的
     * 申请退款状态   (回调)
     *

     */
    @RequestMapping(value = "/refund/result")
    public String refund(HttpServletRequest request) throws Exception{
        //读取网络输入流
        ServletInputStream is = request.getInputStream();

        //定义接收输入流对象（输出流）
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        //将网络输入流读取到输出流中
        byte[] buffer = new byte[1024];
        int len=0;
        while ((len=is.read(buffer))!=-1){
            os.write(buffer,0,len);
        }

        //关闭资源
        os.close();
        is.close();

        //将支付结果的XML结构转换成Map结构
        String xmlResult = new String(os.toByteArray(),"UTF-8");
        Map<String, String> map = WXPayUtil.xmlToMap(xmlResult);
        System.out.println("退款数据-xmlResult:"+xmlResult);

        //获取退款信息（加密了-AES）
        String reqinfo = map.get("req_info");
        // 对商户key做md5,得到32位小写key*
        String key = MD5.md5(skey);
        // key*对加密串B做AES-256-ECB解密
        byte[] decode = AESUtil.encryptAndDecrypt(Base64Util.decode(reqinfo), key, 2);
        System.out.println("退款解密后的数据："+new String(decode, "UTF-8"));


        // TODO  给mq 发送 退款 申请
        //Map 响应数据
        Map<String,String> resultResp = new HashMap<String,String>();
        resultResp.put("return_code","SUCCESS");
        resultResp.put("return_msg","OK");
        return WXPayUtil.mapToXml(resultResp);
    }
}
