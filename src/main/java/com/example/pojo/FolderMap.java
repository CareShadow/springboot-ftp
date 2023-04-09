package com.example.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName FolderMap
 * @Description TODO
 * @Author admin
 * @Date 2022/2/20 20:22
 * @Version 1.0
 **/
@Data
@Builder
public class FolderMap {
    private String FolderName;
    private Integer FolderId;
}
