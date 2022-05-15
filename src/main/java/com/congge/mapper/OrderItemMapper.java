package com.congge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.congge.entity.Order;
import com.congge.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作数据库dao层
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {


}
