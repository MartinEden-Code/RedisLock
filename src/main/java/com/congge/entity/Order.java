package com.congge.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.congge.common.BasePlusEntity;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_order")
public class Order extends BasePlusEntity<Order> implements Serializable {

    private String id;

    @TableField("order_status")
    private Integer orderStatus;

    @TableField("receiver_name")
    private String receiverName;

    @TableField("receiver_phone")
    private String receiverPhone;

    @TableField("order_amount")
    private Double orderAmount;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
