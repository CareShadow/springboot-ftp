package com.example.dao;

import com.example.entity.FileFolder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lxl
 * @since 2022-05-22
 */
public interface FileFolderMapper extends BaseMapper<FileFolder> {
    String getFolderPath(Integer folderID);
    String getFolderMinIOPath(Integer folderID);
    String getFolderIdPath(Integer folderID);
}
