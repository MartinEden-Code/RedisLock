package com.congge.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.congge.common.BasePlusEntity;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_order_item")
public class OrderItem extends BasePlusEntity<OrderItem> implements Serializable {

    private String id;

    @TableField("order_id")
    private String orderId;

    @TableField("product_id")
    private String productId;

    @TableField("purchase_price")
    private Double purchasePrice;

    @TableField("purchase_num")
    private Integer purchaseNum;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
