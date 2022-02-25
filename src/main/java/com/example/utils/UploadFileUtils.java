package com.example.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @ClassName UploadFileUtils
 * @Description TODO
 * @Author admin
 * @Date 2022/2/12 13:44
 * @Version 1.0
 **/
public class UploadFileUtils {
    /**
     * @Description: 获取图片后缀
     * @Param: [file]
     * @return: java.lang.String
     * @date: 2019/8/24 15:27
     */
    public static String getSuffixName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw new RuntimeException("获取图片后缀失败");
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * @Description: 生成文件名称通用方法
     * @Param: [suffixName] 图片后缀
     * @return: java.lang.String
     * @date: 2019/8/24 15:29
     */
    public static String getNewFileName(String suffixName) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        int random = new Random().nextInt(100);
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(random).append(suffixName);
        return tempName.toString();
    }
}
