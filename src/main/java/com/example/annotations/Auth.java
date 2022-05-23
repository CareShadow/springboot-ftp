package com.example.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 运行时
@Target({ElementType.METHOD,ElementType.TYPE})  // 可作用在方法和类上
public @interface Auth {
    // 权限id,需要唯一
    long id();
    // 权限名称
    String name();
}
