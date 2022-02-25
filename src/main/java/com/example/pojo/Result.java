package com.example.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Result
 * @Description TODO
 * @Author admin
 * @Date 2022/2/11 13:57
 * @Version 1.0
 **/
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int resultCode; //状态码 401，403，404
    private String message; //消息
    private T data;//路径
}
