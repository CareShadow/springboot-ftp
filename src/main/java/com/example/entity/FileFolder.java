package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
@Builder
public class FileFolder implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 文件夹ID
     */
    @TableId(value = "file_folder_id", type = IdType.AUTO)
    private Integer fileFolderId;

    /**
     * 文件夹名字
     */
    @TableField("file_folder_name")
    private String fileFolderName;

    /**
     * 父文件夹ID
     */
    @TableField("parent_folder_id")
    private Integer parentFolderId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")    //将Date转换成String,一般后台传值给前台时
    @TableField("time")
    private Date time;

    /**
     * 映射路径
     */
    @TableField("minio_path")
    private String minioPath;

    /**
     * 用户Id
     */
    @TableField("user_id")
    private Integer userId;
}
