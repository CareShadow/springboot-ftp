package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lxl
 * @since 2022-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 閻劍鍩汭D
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 鐢ㄦ埛鍚?
     */
    @TableField("user_name")
    private String userName;

    /**
     * 鐎靛棛鐖?
     */
    @TableField("password")
    private String password;

    /**
     * 娉ㄥ唽鏃堕棿
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")    //将Date转换成String,一般后台传值给前台时
    @TableField("register_time")
    private Date registerTime;

    /**
     * 澶村儚鍦板潃
     */
    @TableField("image_path")
    private String imagePath;


}
