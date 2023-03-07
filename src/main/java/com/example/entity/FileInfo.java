package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName t_file_info
 */
@TableName(value ="t_file_info")
@Data
public class FileInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 文件名称
     */
    @TableField(value = "filename")
    private String filename;

    /**
     * 标识
     */
    @TableField(value = "identifier")
    private String identifier;

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
     * 删除标记
     */
    @TableField(value = "del_flag")
    private String delFlag;

    /**
     * 来源
     */
    @TableField(value = "ref_project_id")
    private String refProjectId;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}