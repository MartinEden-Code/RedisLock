package com.congge.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseResult<T> implements Serializable {

    private Boolean success;

    /**
     * 编码
     */
    private int code;

    /**
     * 结果描述
     */
    private String message;

    private T result;

    /**
     * 成功的返回方法
     * @param result
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> success(T result){
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setSuccess(Boolean.TRUE);
        responseResult.setResult(result);
        return responseResult;
    }

    /**
     * 成功的返回方法
     * @param result
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> success(T result,int code){
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setSuccess(Boolean.TRUE);
        responseResult.setResult(result);
        responseResult.setCode(code);
        return responseResult;
    }

    public static <T> ResponseResult<T> success(String message){
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setSuccess(Boolean.TRUE);
        responseResult.setMessage(message);
        return responseResult;
    }

    /**
     * 成功返回的方法
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> success(int code,String message){
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setSuccess(Boolean.TRUE);
        responseResult.setCode(code);
        responseResult.setMessage(message);
        return responseResult;
    }

    /**
     * 失败的返回结果
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> fail(int code,String message) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setSuccess(Boolean.FALSE);
        responseResult.setCode(code);
        responseResult.setMessage(message);
        return responseResult;
    }

    /**
     * 失败的返回结果
     * @param errorCodeEnums
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> fail(ErrorCodeEnums errorCodeEnums) {
        return fail(errorCodeEnums.getCode(), errorCodeEnums.getMessage());
    }

}
