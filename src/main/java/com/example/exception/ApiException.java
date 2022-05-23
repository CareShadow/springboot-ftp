package com.example.exception;

/**
 * @ClassName ApiException
 * @Description TODO
 * @Author admin
 * @Date 2022/5/23 18:01
 * @Version 1.0
 **/
public class ApiException extends Exception{
    private String msg;
    private int code;
    public ApiException(String msg){
        super(msg);
    }
}
