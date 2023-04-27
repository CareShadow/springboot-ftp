package com.example.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.User;
import com.example.service.UserService;

import javax.annotation.Resource;

/**
 * @ClassName RequestContext
 * @Description TODO
 * @Author admin
 * @Date 2022/5/23 15:00
 * @Version 1.0
 **/
public final class RequestContext {

    @Resource
    private static UserService userService;

    private static final ThreadLocal<String> user = new ThreadLocal<String>();

    public static void add(String userName) {
        user.set(userName);
    }

    public static void remove() {
        user.remove();
    }


    public static User getCurrentUser() {
        String username = user.get();
        User entity = userService.getOne(new QueryWrapper<User>().lambda().eq(User::getUserName, username).last("LIMIT 1"));
        return entity;
    }
}
