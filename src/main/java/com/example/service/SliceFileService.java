package com.example.service;


import java.io.InputStream;
import java.util.List;

/**
 * @ClassName SliceFileService
 * @Description TODO
 * @Author CareShadow
 * @Date 2023/2/5 21:47
 * @Version 1.0
 **/
public interface SliceFileService {
    // 检查文件是否存在
    boolean checkFile(String fileMD5);
    // 检查分块文件是否存在
    List<Integer> checkChunk(String fileMD5);
    // 上传分块
    void uploadChunk(String fileMD5, InputStream in);
}
