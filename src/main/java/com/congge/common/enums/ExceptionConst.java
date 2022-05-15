package com.congge.common.enums;

public enum ExceptionConst {

    RULE_SCENE_CODE_EXIST(11001, "Rule scene's code is already exist"),
    DATA_NOT_EXISTS(11002, "Data not exist");
    ;

    private int errorCode;

    private String msg;

    ExceptionConst(int errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public static ExceptionConst checkByCode(int code) {
        for (ExceptionConst type : ExceptionConst.values()) {
            if (type.getErrorCode() == code) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "code=" + this.errorCode + ", msg=" + this.msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMsg() {
        return msg;
    }
}
