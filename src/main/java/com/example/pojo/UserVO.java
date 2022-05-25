package com.example.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName UserVO
 * @Description TODO
 * @Author admin
 * @Date 2022/5/24 21:33
 * @Version 1.0
 **/
@Data
public class UserVO {
    private Long userId;
    private String userName;
    private Date registerTime;
    private String imagePath;
    private String name;
}
