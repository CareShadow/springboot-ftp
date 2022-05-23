package com.example.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @ClassName UserLoginterceptor
 * @Description TODO
 * @Author admin
 * @Date 2022/2/11 14:33
 * @Version 1.0
 **/
@Component
public class UserLoginInterceptor implements HandlerInterceptor {
    // 正则表达式
    private static final Pattern pattern  = Pattern.compile("\\b/admin/\\b");
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri =request.getRequestURI();
        if(!pattern.matcher(uri).find()&& Objects.isNull(request.getSession().getAttribute("id"))){
            request.getSession().setAttribute("errorMsg","请重新登录");
            response.sendRedirect(request.getContextPath()+"/admin/v1/login");
            return false;
        }else{
            request.getSession().removeAttribute("errorMsg");
            return true;
        }
    }
}
