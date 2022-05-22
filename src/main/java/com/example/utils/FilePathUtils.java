package com.example.utils;

import com.example.entity.FileFolder;
import com.example.service.FileFolderService;
import com.example.service.MyFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @ClassName FilePathUtils
 * @Description TODO
 * @Author admin
 * @Date 2022/5/22 11:09
 * @Version 1.0
 **/
@Component
public class FilePathUtils {

    @Autowired(required =  false)
    private FileFolderService fileFolderService;
    /**
     * 功能描述：获取父文件夹路径
     * 思路: 深度遍历
     * @param: [Integer] 父文件路径id
     * @return: java.lang.String
     * @auther: lxl
     * @date: 2022/5/22 11:13
     */
    public String getFilePath(Integer id){
        if(id==0) return "/";
        List<String> path = new ArrayList<>();
        while(id!=0){
            FileFolder fileFolder = fileFolderService.getById(id);
            path.add(fileFolder.getFileFolderName());
            id = fileFolder.getParentFolderId();
        }
        Collections.reverse(path);
        StringBuilder sb = new StringBuilder();
        for(String name : path){
            sb.append("/").append(name);
        }
        return sb.toString();
    }

}
