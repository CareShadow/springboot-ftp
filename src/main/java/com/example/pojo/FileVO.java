package com.example.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName FileVO
 * @Description TODO
 * @Author admin
 * @Date 2022/2/13 14:41
 * @Version 1.0
 **/
@Data
public class FileVO {
    //文件夹没有文件后缀，文件大小，文件下载次数
    //文件id
    private Integer id;
    //文件名称
    private String name;
    //文件后缀
    private String postfix;
    //文件大小
    private String size;
    //文件下载次数
    private Integer downloadTime;
    //文件类型
    private Integer type;
    //文件上传时间
    @JsonFormat(pattern = "MMM d, yyyy h:mm a", timezone = "GMT+8", locale = "en_US")
    private Date uploadTime;
}
