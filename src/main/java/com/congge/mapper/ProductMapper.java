package com.congge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.congge.entity.Order;
import com.congge.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 操作数据库dao层
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    Product selectProductById(@Param("productId") String productId);
}
