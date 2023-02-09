package com.example.service;


import com.example.pojo.Result;

import java.io.InputStream;
import java.util.Map;

/**
 * @ClassName SliceFileService
 * @Description TODO
 * @Author CareShadow
 * @Date 2023/2/5 21:47
 * @Version 1.0
 **/
public interface SliceFileService {
    // 检查文件是否存在
    Result<Boolean> checkFile(String fileMD5);
    // 检查分块文件是否存在
    Result<Map> checkChunk(String fileMD5, int index);
    // 上传分块
    Result<Map> uploadChunk(String fileMD5, InputStream in);
}
