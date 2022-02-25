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
@TableName("file_folder")
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
    @TableField("time")
    private Date time;
    /**
     * 文件夹路径
     */
    @TableField("file_folder_path")
    private String fileFolderPath;


}
