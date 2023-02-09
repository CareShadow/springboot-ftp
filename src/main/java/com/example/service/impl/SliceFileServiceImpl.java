package com.example.service.impl;

import com.example.constants.HttpStatusEnum;
import com.example.pojo.Result;
import com.example.service.SliceFileService;
import com.example.utils.MinioUtils;
import com.example.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

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
    
    @Override
    public Result<Boolean> checkFile(String fileMD5) {
        // 检查Minio文件系统是否有文件 若有直接返回，检查数据数据是否存在

        try {
            boolean isExist = minioUtils.JudgeFileMD5(fileMD5);
            return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, isExist);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.getResultByHttp(HttpStatusEnum.INTERNAL_SERVER_ERROR, false);
        }
    }

    @Override
    public Result<Map> checkChunk(String fileMD5, int index) {
        return null;
    }

    @Override
    public Result<Map> uploadChunk(String fileMD5, InputStream in) {
       return null;
    }
}
