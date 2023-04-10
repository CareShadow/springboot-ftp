package com.example.pojo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName SplitChunkInfoVO
 * @Description TODO
 * @Author CareShadow
 * @Date 2023/3/7 21:37
 * @Version 1.0
 **/

@Data
public class SplitChunkInfoVO {
    private int chunkNumber;
    private long chunkSize;
    private long currentChunkSize;
    private long totalSize;
    private String identifier;
    private String filename;
    private String relativePath;
    private int totalChunks;
    private MultipartFile file;
    private int folderId;
}
