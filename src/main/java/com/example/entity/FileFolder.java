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
public class FileFolder implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 文件夹ID
     */
    @TableId(value = "file_folder_id", type = IdType.AUTO)
    private Integer fileFolderId;

    /**
     * 鏂囦欢澶瑰悕绉?
     */
    @TableField("file_folder_name")
    private String fileFolderName;

    /**
     * 鐖舵枃浠跺すID
     */
    @TableField("parent_folder_id")
    private Integer parentFolderId;

    /**
     * 鍒涘缓鏃堕棿
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")    //将Date转换成String,一般后台传值给前台时
    @TableField("time")
    private Date time;


}
