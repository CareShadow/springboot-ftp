package com.example.utils;

import com.example.constants.HttpStatusEnum;
import com.example.pojo.Result;

/**
 * @ClassName ResultGenerator
 * @Description TODO
 * @Author admin
 * @Date 2022/2/11 13:59
 * @Version 1.0
 **/
public class ResultGenerator {
    private ResultGenerator() {
    }

    public static <T> Result<T> getResultByHttp(HttpStatusEnum constants, String msg, T data) {
        Result<T> result = new Result<>();
        result.setResultCode(constants.getStatus());
        result.setMessage(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> getResultByHttp(HttpStatusEnum constants, T data) {
        Result<T> result = new Result<>();
        result.setResultCode(constants.getStatus());
        result.setMessage(constants.getContent());
        result.setData(data);
        return result;
    }

    /**
     * 自定义提示消息
     * @param constants Http枚举
     * @param msg 提示消息
     */
    public static Result<String> getResultByMsg(HttpStatusEnum constants, String msg) {
        Result<String> result = new Result<>();
        result.setResultCode(constants.getStatus());
        result.setMessage(msg);
        return result;
    }

    /**
     * @Description: 根据传入的常量返回对应result
     * @Param: [constants] http状态
     * @return: com.example.pojo.Result
     * @date: 2022/2/11 13:59
     */
    public static Result<String> getResultByHttp(HttpStatusEnum constants) {
        Result<String> result = new Result<>();
        result.setResultCode(constants.getStatus());
        result.setMessage(constants.getContent());
        return result;
    }
}
