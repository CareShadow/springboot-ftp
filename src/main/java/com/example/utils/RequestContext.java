package com.example.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName RequestContext
 * @Description TODO
 * @Author admin
 * @Date 2022/5/23 15:00
 * @Version 1.0
 **/
public class RequestContext {
    public static HttpServletRequest getCurrentRequest(){
        return ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest();
    }
    public static Long getCurrentUserId(){
        return (Long) getCurrentRequest().getSession().getAttribute("id");
    }
}
