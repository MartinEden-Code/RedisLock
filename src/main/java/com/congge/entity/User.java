package com.congge.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.congge.common.BasePlusEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据库映射对象
 */
@Data
@TableName("sys_user")
public class User extends BasePlusEntity<User> implements Serializable{

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("email")
    private String email;

    @TableField("age")
    private Integer age;

    @TableField("phone")
    private String phone;

    private String id;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
