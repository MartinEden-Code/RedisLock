package com.congge.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.congge.common.BasePlusEntity;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_product")
public class Product extends BasePlusEntity<Product> implements Serializable {

    private String id;

    @TableField("product_name")
    private String productName;

    @TableField("price")
    private Double price;

    @TableField("count")
    private Integer count;

    @TableField("product_desc")
    private String productDesc;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
