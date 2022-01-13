package com.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.order.entity.Oorder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/*****
 * @Author:
 * @Description:
 ****/
@Mapper
//@Repository
public interface OrderMapper extends BaseMapper<Oorder> {
}
