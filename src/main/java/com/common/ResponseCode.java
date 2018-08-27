package com.common;

/**
 * @program: EWeb
 * @description: 返回状态，返回信息 枚举
 * @author: zpp
 * @create: 2018-08-27 21:11
 **/
public enum ResponseCode {
    SUCCESS(0,"SUCCESS"),ERROR(1,"ERROR"),NEED_LOGIN(10,"NEED_LOGIN"),ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");
    private final  int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
