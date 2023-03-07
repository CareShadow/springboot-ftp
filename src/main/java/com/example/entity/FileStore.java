package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("file_store")
public class FileStore implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 文件仓库ID
     */
    @TableId(value = "file_store_id", type = IdType.AUTO)
    private Integer fileStoreId;

    /**
     * 主人ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 当前容量（单位KB）
     */
    @TableField("current_size")
    private Integer currentSize;

    /**
     * 最大容量（单位KB）
     */
    @TableField("max_size")
    private Integer maxSize;

    /**
     * Minio桶名字
     */

    @TableField("file_store_name")
    private String fileStoreName;


}
