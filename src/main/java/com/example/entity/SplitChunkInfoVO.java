package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName t_chunk_info
 */
@TableName(value ="t_chunk_info")
@Data
public class SplitChunkInfoVO implements Serializable {
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
    private Long currentChunkSize;

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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SplitChunkInfoVO other = (SplitChunkInfoVO) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getChunkNumber() == null ? other.getChunkNumber() == null : this.getChunkNumber().equals(other.getChunkNumber()))
            && (this.getChunkSize() == null ? other.getChunkSize() == null : this.getChunkSize().equals(other.getChunkSize()))
            && (this.getCurrentChunkSize() == null ? other.getCurrentChunkSize() == null : this.getCurrentChunkSize().equals(other.getCurrentChunkSize()))
            && (this.getIdentifier() == null ? other.getIdentifier() == null : this.getIdentifier().equals(other.getIdentifier()))
            && (this.getFilename() == null ? other.getFilename() == null : this.getFilename().equals(other.getFilename()))
            && (this.getRelativePath() == null ? other.getRelativePath() == null : this.getRelativePath().equals(other.getRelativePath()))
            && (this.getTotalChunks() == null ? other.getTotalChunks() == null : this.getTotalChunks().equals(other.getTotalChunks()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getChunkNumber() == null) ? 0 : getChunkNumber().hashCode());
        result = prime * result + ((getChunkSize() == null) ? 0 : getChunkSize().hashCode());
        result = prime * result + ((getCurrentChunkSize() == null) ? 0 : getCurrentChunkSize().hashCode());
        result = prime * result + ((getIdentifier() == null) ? 0 : getIdentifier().hashCode());
        result = prime * result + ((getFilename() == null) ? 0 : getFilename().hashCode());
        result = prime * result + ((getRelativePath() == null) ? 0 : getRelativePath().hashCode());
        result = prime * result + ((getTotalChunks() == null) ? 0 : getTotalChunks().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", chunkNumber=").append(chunkNumber);
        sb.append(", chunkSize=").append(chunkSize);
        sb.append(", currentChunksize=").append(currentChunkSize);
        sb.append(", identifier=").append(identifier);
        sb.append(", filename=").append(filename);
        sb.append(", relativePath=").append(relativePath);
        sb.append(", totalChunks=").append(totalChunks);
        sb.append(", type=").append(type);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}