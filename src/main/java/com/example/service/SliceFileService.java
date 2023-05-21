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
    boolean checkFile(String fileMD5, String fileName, String path);

    // 检查分块文件是否存在
    List<Integer> checkChunk(String fileMD5);

    // 上传分块
    void uploadChunk(String fileMD5, InputStream in, int index);

    // 下载minio文件
    void mergeFile(String fileMD5, int totalChunks, String contentType,
                   String name, Integer folderId, String folderPath, Double size) throws Exception;

    // 判断是否合并
    boolean isCheckMerge(String fileMD5, Long userId);
}
