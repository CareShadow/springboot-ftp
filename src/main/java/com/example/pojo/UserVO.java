package com.example.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "MMM d, yyyy h:mm a", timezone = "GMT+8", locale = "en_US")
    private Date registerTime;
    private String imagePath;
    private String roleName;
    private Integer roleId;
}
