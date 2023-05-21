package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.constants.HttpStatusEnum;
import com.example.dao.FileFolderMapper;
import com.example.entity.FileFolder;
import com.example.entity.User;
import com.example.pojo.FileVO;
import com.example.pojo.Result;
import com.example.service.FileFolderService;
import com.example.utils.FilePathUtils;
import com.example.utils.MinioUtils;
import com.example.utils.RequestContext;
import com.example.utils.ResultGenerator;
import io.minio.ObjectWriteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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
@Slf4j
public class FileFolderServiceImpl extends ServiceImpl<FileFolderMapper, FileFolder> implements FileFolderService {

    @Autowired(required = false)
    private FileFolderMapper fileFolderMapper;
    @Autowired
    private MinioUtils minioUtils;

    @Override
    @Transactional(isolation = Isolation.DEFAULT, timeout = -1, propagation = Propagation.REQUIRED)
    public Result<Object> createNewFolder(Integer parentFolderId, String folderName) throws Exception {
        User currentUser = RequestContext.getCurrentUser();
        String minioFolderName = FilePathUtils.folderNameGenerator();
        FileFolder fileFolder = FileFolder.builder()
                .parentFolderId(parentFolderId)
                .fileFolderName(folderName)
                .time(new Date())
                .minioPath(minioFolderName)
                .userId(currentUser.getUserId())
                .build();
        boolean isSaved = this.save(fileFolder);
        if (isSaved) {
            // 在MinIO服务器创建路径及文件夹
            // 获取文件夹路径
            String folderPath = this.getFolderPath(parentFolderId) + minioFolderName + "/";
            ObjectWriteResponse response = minioUtils.createFolderPath(folderPath);
            log.info("该文件的标签： ", response.etag());
            return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, "创建成功", response.etag());
        }
        return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, "创建失败", isSaved);
    }

    @Override
    public List<FileVO> getFolderList(Integer id, Long userId) {
        QueryWrapper<FileFolder> queryWrapper = new QueryWrapper<>(FileFolder.builder().parentFolderId(id).userId(userId).build());
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
    public boolean batchDelete(List<Integer> ids) {
        if(ids.isEmpty()) return true;
        return fileFolderMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public String getFolderPath(Integer folderID) {
        String folderPath = fileFolderMapper.getFolderPath(folderID);
        return folderPath;
    }

}
