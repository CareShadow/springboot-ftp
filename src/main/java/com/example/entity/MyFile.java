package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lxl
 * @since 2022-02-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("my_file")
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
     * 文件存储路径
     */
    @TableField("my_file_path")
    private String myFilePath;

    /**
     * 下载次数
     */
    @TableField("download_time")
    private Integer downloadTime;

    /**
     * 上传时间
     */
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


}
