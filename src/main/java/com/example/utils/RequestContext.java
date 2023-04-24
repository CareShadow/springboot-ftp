package com.example.utils;

/**
 * @ClassName RequestContext
 * @Description TODO
 * @Author admin
 * @Date 2022/5/23 15:00
 * @Version 1.0
 **/
public final class RequestContext {
    private static final ThreadLocal<String> user = new ThreadLocal<String>();

    public static void add(String userName) {
        user.set(userName);
    }

    public static void remove() {
        user.remove();
    }

    public static String getCurrentUserName() {
        return user.get();
    }
}
