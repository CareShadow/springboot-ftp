package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.MyFile;
import com.example.dao.MyFileMapper;
import com.example.pojo.FileVO;
import com.example.service.MyFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lxl
 * @since 2022-02-11
 */
@Service
public class MyFileServiceImpl extends ServiceImpl<MyFileMapper, MyFile> implements MyFileService {
    @Autowired(required = false)
    private MyFileMapper myFileMapper;
    @Override
    public List<FileVO> getFileList(Integer id) {
        QueryWrapper<MyFile> queryWrapper = new QueryWrapper<>(new MyFile().setParentFolderId(id));
        List<MyFile> myFiles = myFileMapper.selectList(queryWrapper);
        List<FileVO> fileVOList = new ArrayList<>();
        for(MyFile file:myFiles){
            FileVO fileVO = new FileVO();
            fileVO.setId(file.getMyFileId());
            fileVO.setName(file.getMyFileName());
            fileVO.setPostfix(file.getPostfix());
            fileVO.setSize(file.getSize());
            fileVO.setType(file.getType());
            fileVO.setDownloadTime(file.getDownloadTime());
            fileVO.setUploadTime(file.getUploadTime());
            fileVOList.add(fileVO);
        }
        return fileVOList;
    }

    @Override
    public Integer getFileCount(Integer id) {
        QueryWrapper<MyFile> queryWrapper = new QueryWrapper<>(new MyFile().setParentFolderId(id));
        Integer count = myFileMapper.selectCount(queryWrapper);
        return count;
    }
}
