package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.FileFolder;
import com.example.pojo.FileVO;
import com.example.pojo.Result;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lxl
 * @since 2022-05-22
 */
public interface FileFolderService extends IService<FileFolder> {
    List<FileVO> getFolderList(Integer id, Integer userId);
    Integer getFolderCount(Integer id);
    String getFolderPath(Integer folderID);
    String getMinIOPath(Integer folderID);
    String getFolderID(Integer folderID);
    Result<Object> createNewFolder(Integer parentFolderId, String folderName) throws Exception;
}
