package com.pay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/*****
 * @Author:
 * @Description:
 ****/
@Data
@AllArgsConstructor
@NoArgsConstructor
//MyBatisPlus表映射注解
public class Order  implements Serializable {

    private String id;
    private String payType;
    private Date createTime;    //创建时间
    private Date updateTime;    //修改时间
    private Date payTime;
    private Date consignTime;
    private Date endTime;
    private String username;
    private String recipients;  //收件人
    private String recipientsMobile; //收件人手机号
    private String recipientsAddress; //收件人地址
    private String weixinTransactionId;
    private Integer totalNum;   //订单商品总数量
    private Integer moneys;     //支付总金额
    private Integer orderStatus;
    private Integer payStatus;
    private Integer isDelete;

    //购物车ID集合
    private List<String> cartIds;

}