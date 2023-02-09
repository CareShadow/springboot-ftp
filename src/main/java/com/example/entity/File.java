package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName file
 */
@TableName(value ="file")
@Data
public class File implements Serializable {
    /**
     * 文件MD5值

     */
    @TableId(value = "fileMD5")
    private String fileMD5;

    /**
     * 文件名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 文件大小

     */
    @TableField(value = "size")
    private Double size;

    /**
     * 后缀名
     */
    @TableField(value = "postfix")
    private String postfix;

    /**
     * 预览路径
     */
    @TableField(value = "path")
    private String path;

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
        File other = (File) that;
        return (this.getFileMD5() == null ? other.getFileMD5() == null : this.getFileMD5().equals(other.getFileMD5()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getSize() == null ? other.getSize() == null : this.getSize().equals(other.getSize()))
            && (this.getPostfix() == null ? other.getPostfix() == null : this.getPostfix().equals(other.getPostfix()))
            && (this.getPath() == null ? other.getPath() == null : this.getPath().equals(other.getPath()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFileMD5() == null) ? 0 : getFileMD5().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getSize() == null) ? 0 : getSize().hashCode());
        result = prime * result + ((getPostfix() == null) ? 0 : getPostfix().hashCode());
        result = prime * result + ((getPath() == null) ? 0 : getPath().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fileMD5=").append(fileMD5);
        sb.append(", name=").append(name);
        sb.append(", size=").append(size);
        sb.append(", postfix=").append(postfix);
        sb.append(", path=").append(path);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}