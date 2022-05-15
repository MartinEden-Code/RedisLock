package com.congge.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorCodeEnums {

    SUCCESS(0000,"操作成功"),

    /* ----- 1、参数异常 ---- */
    PARAM_ERROR(1001,"参数异常"),
    PARAM_NULL(1002,"参数为空"),
    PARAM_FORMAT_ERROR(1003,"参数格式不正确"),
    PARAM_VALUE_ERROR(1004,"参数值不正确"),

    /* ----- 2、系统异常 ---- */
    SYSTEM_ERROR(2001,"系统异常"),
    UNKNOW_ERROR(2002,"未知异常"),

    /* ----- 3、业务类异常 ---- */
    XXX_ERROR(3001,"系统异常"),
    INSERT_ERROR(3002,"新增失败"),
    UPPDATE_ERROR(3003,"修改失败"),
    DELETE_ERROR(3004,"删除失败"),
    RATE_LIMIT_ERROR(3005,"限流异常"),

    FILE_UPLOAD_ERROR(3006,"文件上传失败"),
    ;

    private int code;
    private String message;


}
