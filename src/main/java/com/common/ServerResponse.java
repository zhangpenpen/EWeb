package com.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @program: EWeb
 * @description:
 * @author: zpp
 * @create: 2018-08-27 20:57
 **/
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//序列化json，除去value为null的元素
public class ServerResponse<T> {
    private int status;//返回状态
    private String msg;//错误信息
    private T date;

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
        System.out.println("s");
    }

    private ServerResponse(int status, T date) {
        this.status = status;
        this.date = date;
        System.out.println("t");
    }

    private ServerResponse(int status, String msg, T date) {
        this.status = status;
        this.msg = msg;
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getDate() {
        return date;
    }

    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccess(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    public static <T> ServerResponse<T> createBySuccess(T date){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),date);
    }
    public static <T> ServerResponse<T> createBySuccess(String msg,T date){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,date);
    }
    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }


    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }
}
