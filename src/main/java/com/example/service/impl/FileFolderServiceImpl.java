package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.FileFolder;
import com.example.dao.FileFolderMapper;
import com.example.pojo.FileVO;
import com.example.service.FileFolderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lxl
 * @since 2022-05-22
 */
@Service
public class FileFolderServiceImpl extends ServiceImpl<FileFolderMapper, FileFolder> implements FileFolderService {

    @Autowired(required = false)
    private FileFolderMapper fileFolderMapper;

    @Override
    public List<FileVO> getFolderList(Integer id) {
        QueryWrapper<FileFolder> queryWrapper = new QueryWrapper<>(FileFolder.builder().parentFolderId(id).build());
        List<FileFolder> fileFolders = fileFolderMapper.selectList(queryWrapper);
        List<FileVO> fileVOList = new ArrayList<>();
        for (FileFolder a : fileFolders) {
            FileVO fileVO = new FileVO();
            fileVO.setId(a.getFileFolderId());
            fileVO.setName(a.getFileFolderName());
            fileVO.setUploadTime(a.getTime());
            fileVOList.add(fileVO);
        }
        return fileVOList;
    }

    @Override
    public String getMinIOPath(Integer folderID) {
        return fileFolderMapper.getFolderMinIOPath(folderID);
    }

    @Override
    public String getFolderID(Integer folderID) {
        return fileFolderMapper.getFolderIdPath(folderID);
    }

    @Override
    public Integer getFolderCount(Integer id) {
        QueryWrapper<FileFolder> queryWrapper = new QueryWrapper<>(FileFolder.builder().parentFolderId(id).build());
        Integer count = fileFolderMapper.selectCount(queryWrapper);
        return count;
    }

    @Override
    public String getFolderPath(Integer folderID) {
        String folderPath = fileFolderMapper.getFolderPath(folderID);
        return folderPath;
    }

}
