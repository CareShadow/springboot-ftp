package com.example.pojo;

import lombok.Data;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName TableResultVO
 * @Description TODO
 * @Author admin
 * @Date 2022/2/13 14:51
 * @Version 1.0
 **/
@Data
public class TableResultVO {
    private Integer code;
    private String message;
    private Integer count;
    private List<FileVO> data;

    public static void main(String[] args) {
        String filePath = "/home";
        try {
            filePath = new String(filePath.getBytes("UTF-8"),"iso-8859-1");
            System.out.println(filePath);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
