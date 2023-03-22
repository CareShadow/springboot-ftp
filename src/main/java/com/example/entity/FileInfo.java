package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName t_file_info
 */
@TableName(value ="t_file_info")
@Data
@Builder
public class FileInfo implements Serializable {
    /**
     * 标识
     */
    @TableId(value = "identifier")
    private String identifier;

    /**
     * 文件名称
     */
    @TableField(value = "filename")
    private String filename;

    /**
     * 类型
     */
    @TableField(value = "type")
    private String type;

    /**
     * 文件大小
     */
    @TableField(value = "total_size")
    private Long totalSize;

    /**
     * 路径
     */
    @TableField(value = "location")
    private String location;

    /**
     * 来源
     */
    @TableField(value = "parent_folder_id")
    private Integer parentFolderId;

    /**
     * 上传人
     */
    @TableField(value = "upload_by")
    private String uploadBy;

    /**
     * 上传时间
     */
    @TableField(value = "upload_time")
    private LocalDateTime uploadTime;

    /**
     * 
     */
    @TableField(value = "postfix")
    private String postfix;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}