package com.congge.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_user")
public class TUser extends Model implements Serializable {

    @TableField("id")
    private int id;

    @TableField("name")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("bir")
    private Date bir;

    @TableField("sex")
    private String sex;

    @TableField("address")
    private String address;

}
