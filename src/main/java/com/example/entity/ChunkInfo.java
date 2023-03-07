package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_chunk_info
 */
@TableName(value ="t_chunk_info")
@Data
public class ChunkInfo implements Serializable {
    /**
     * 主键
     */
    @TableField(value = "id")
    private String id;

    /**
     * 分片编号
     */
    @TableField(value = "chunk_number")
    private Long chunkNumber;

    /**
     * 分片大小
     */
    @TableField(value = "chunk_size")
    private Long chunkSize;

    /**
     * 校验大小
     */
    @TableField(value = "current_chunkSize")
    private Long currentChunksize;

    /**
     * 标记
     */
    @TableField(value = "identifier")
    private String identifier;

    /**
     * 文件名
     */
    @TableField(value = "filename")
    private String filename;

    /**
     * 文件路径
     */
    @TableField(value = "relative_path")
    private String relativePath;

    /**
     * 校验分片数量
     */
    @TableField(value = "total_chunks")
    private Long totalChunks;

    /**
     * 类型
     */
    @TableField(value = "type")
    private Long type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}