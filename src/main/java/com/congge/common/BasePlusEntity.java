package com.congge.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 共有属性继承类
 */
@Data
public class BasePlusEntity<T extends Model> extends Model implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String id;

    protected String remarks;

    @TableField(value = "create_date", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createDate;

    @TableField(value = "update_date", fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updateDate;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    protected String createBy;

    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    protected String updateBy;

    @JSONField(serialize = false)
    @TableField(value = "del_flag", fill = FieldFill.INSERT_UPDATE)
    @TableLogic
    @JsonIgnore
    protected Integer delFlag;

    public BasePlusEntity(String id) {
        this();
        this.id = id;
    }

    public BasePlusEntity() {

    }

}
