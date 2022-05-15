package com.congge.common.exception;

import com.congge.common.enums.ExceptionConst;

public class BusinessException extends RuntimeException {

    /**
     * 错误编码
     */
    private int errorCode = 0;

    public BusinessException(int errorCode, String msg){
        super(msg);
        this.errorCode = errorCode;
    }

    public BusinessException(ExceptionConst exceptionConst){
        super(exceptionConst.getMsg());
        this.errorCode = exceptionConst.getErrorCode();
    }

    public BusinessException(ExceptionConst exceptionConst, String extMsg){
        super(exceptionConst.getMsg() + ":" + extMsg);
        this.errorCode = exceptionConst.getErrorCode();
    }

    public BusinessException(int errorCode, Throwable throwable){
        super(throwable);
        this.errorCode = errorCode;
    }

    public BusinessException(){
        super();
    }

    public BusinessException(String msg){
        super(msg);
    }

    public BusinessException(String msg, Throwable throwable){
        super(msg,throwable);
    }

    public Throwable getException(){
        return super.getCause();
    }

    public int getErrorCode() {
        return errorCode;
    }



}
