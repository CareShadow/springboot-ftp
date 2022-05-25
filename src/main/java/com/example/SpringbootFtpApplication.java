package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@MapperScan("com.example.dao")
public class SpringbootFtpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootFtpApplication.class, args);
    }

}
