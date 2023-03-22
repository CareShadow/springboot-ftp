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
public class MyFile implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 文件ID
     */
    @TableId(value = "my_file_id", type = IdType.AUTO)
    private Integer myFileId;

    /**
     * 文件名
     */
    @TableField("my_file_name")
    private String myFileName;

    /**
     * 下载次数
     */
    @TableField("download_time")
    private Integer downloadTime;

    /**
     * 上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")    //将Date转换成String,一般后台传值给前台时
    @TableField("upload_time")
    private Date uploadTime;

    /**
     * 父文件夹ID
     */
    @TableField("parent_folder_id")
    private Integer parentFolderId;

    /**
     * 文件大小
     */
    @TableField("size")
    private Integer size;

    /**
     * 文件类型
     */
    @TableField("type")
    private Integer type;

    /**
     * 文件后缀
     */
    @TableField("postfix")
    private String postfix;

    /**
     * 预览地址
     */
    @TableField("path")
    private String path;

}
