package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.MyFile;
import com.example.entity.User;
import com.example.service.MyFileService;
import com.example.service.SliceFileService;
import com.example.utils.MinioUtils;
import com.example.utils.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.*;

/**
 * @ClassName SliceFileServiceImpl
 * @Description TODO
 * @Author CareShadow
 * @Date 2023/2/8 22:22
 * @Version 1.0
 **/
@Service
public class SliceFileServiceImpl implements SliceFileService {
    @Autowired
    private MinioUtils minioUtils;
    @Autowired
    private MyFileService myFileService;

    @Override
    public boolean checkFile(String fileMD5, String fileName, String path) {
        // 检查Minio文件系统是否有文件 若有直接返回，检查数据数据是否存在
        // 数据库有文件信息, Minio里面没有
        MyFile fileInfo = myFileService.getOne(new QueryWrapper<MyFile>().lambda().eq(MyFile::getIdentifier, fileMD5));
        if (fileInfo != null) {
            try {
                boolean isExist = minioUtils.JudgeFileMD5(path, fileName);
                return isExist;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public List<Integer> checkChunk(String fileMD5) {
        // 查看文件夹中的文件数量 返回回来
        try {
            List<Integer> uploaderChunk = minioUtils.getUploaderChunk(fileMD5);
            return uploaderChunk;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void uploadChunk(String fileMD5, InputStream in, int index) {
        // 根据MD5创建文件夹, 把分块文件上传上去,更新数据库信息
        try {
            minioUtils.putObject(in, fileMD5 + "/chunks/" + index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mergeFile(String fileMD5, int totalChunks, String contentType,
                          String name, Integer folderId, String folderPath, Double size) throws Exception {
        Vector<InputStream> streams = new Vector<>();
        for (int i = 1; i <= totalChunks; i++) {
            String object = fileMD5 + "/chunks/" + i;
            InputStream in = minioUtils.getObject("slice", object);
            streams.add(in);
        }
        SequenceInputStream sequenceInputStream = new SequenceInputStream(streams.elements());
        minioUtils.putObject(sequenceInputStream, folderPath + name, contentType);
        // 设置文件类型
        int fileType = judgeFileType(contentType);
        User currentUser = RequestContext.getCurrentUser();
        // 拆分文件名和后缀名
        String[] split = name.split("\\.");
        MyFile fileInfo = MyFile.builder()
                .identifier(fileMD5)
                .myFileName(split[0])
                .postfix(split[1])
                .parentFolderId(folderId)
                .size(String.format("%.2f", size))
                .type(fileType)
                .uploadTime(new Date())
                .userId(currentUser.getUserId())
                .build();
        myFileService.save(fileInfo);
    }

    /**
     * @Description 根据文件上传的content-Type来设置文件类型 1.图片 2.视频 3.word 4.ppt 5.excel 6.pdf 7.音频 8.其他
     * @Param [contentType]
     * @Return int
     * @Date 2023/4/22 11:34
     * @Author CareShadow
     * @Version 1.0
     **/
    public int judgeFileType(String contentType) {
        List<String> imageContentType = Arrays.asList(new String[]{"image/jpeg", "image/webp", "image/png", "image/gif", "image/svg+xml"});
        List<String> audioContentType = Arrays.asList(new String[]{"audio/mpeg", "audio/ogg", "audio/wav"});
        List<String> videoContentType = Arrays.asList(new String[]{"video/mp4", "video/ogg", "video/webm"});
        int fileType = 8;
        if (imageContentType.contains(contentType)) {
            fileType = 1;
        } else if (audioContentType.contains(contentType)) {
            fileType = 7;
        } else if (videoContentType.contains(contentType)) {
            fileType = 2;
        } else if ("application/pdf".equals(contentType)) {
            fileType = 6;
        } else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
            fileType = 5;
        } else if ("application/vnd.openxmlformats-officedocument.presentationml.presentation".equals(contentType)) {
            fileType = 4;
        } else if ("application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(contentType)) {
            fileType = 3;
        }
        return fileType;
    }
}
