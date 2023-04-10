package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.MyFile;
import com.example.service.MyFileService;
import com.example.service.SliceFileService;
import com.example.utils.MinioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Date;
import java.util.List;
import java.util.Vector;

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
                boolean isExist = minioUtils.JudgeFileMD5(fileMD5, fileName);
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
            InputStream in = minioUtils.getObject(object);
            streams.add(in);
        }
        SequenceInputStream sequenceInputStream = new SequenceInputStream(streams.elements());
        minioUtils.putObject(sequenceInputStream, folderPath + name, contentType);
        // 拆分文件名和后缀名
        String[] split = name.split("\\.");
        MyFile fileInfo = MyFile.builder()
                .identifier(fileMD5)
                .myFileName(split[0])
                .postfix(split[1])
                .parentFolderId(folderId)
                .size(size)
                .type(1)
                .uploadTime(new Date())
                .build();
        myFileService.save(fileInfo);
    }
}
