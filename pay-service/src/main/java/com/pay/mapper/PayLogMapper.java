package com.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pay.entity.PayLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/*****
 * @Author:
 * @Description:
 ****/
//@Mapper
@Repository
public interface PayLogMapper extends BaseMapper<PayLog> {
}
