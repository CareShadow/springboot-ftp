package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.MyFile;
import com.example.pojo.FileVO;
import com.example.pojo.TableResultVO;
import com.example.service.MyFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ImgsController
 * @Description TODO
 * @Author admin
 * @Date 2022/2/26 21:08
 * @Version 1.0
 **/
@Controller
@RequestMapping(value = "/admin/image")
public class ImagesController {
    @Autowired
    private MyFileService myFileService;
    /**
     * 功能描述：返回图片文件具体数据
     * @param: [imageName]
     * @return: java.lang.String
     * @auther: lxl
     * @date: 2022/2/26 21:11
     */
    @GetMapping(value = "/list")
    @ResponseBody
    public TableResultVO imageList(Map<String,Object> map,String imageName){
        List<MyFile> imageList = myFileService.list(new QueryWrapper<MyFile>().lambda()
                .eq(MyFile::getType, 1).like(MyFile::getMyFileName,imageName));
        List<FileVO> fileVOList = new ArrayList<>();
        for(MyFile myFile:imageList){
            FileVO fileVO = new FileVO();
            fileVO.setId(myFile.getMyFileId());
            fileVO.setType(myFile.getType());
            fileVO.setUploadTime(myFile.getUploadTime());
            fileVO.setSize(myFile.getSize());
            fileVO.setPostfix(myFile.getPostfix());
            fileVO.setName(myFile.getMyFileName());
            fileVO.setDownloadTime(myFile.getDownloadTime());
            fileVOList.add(fileVO);
        }
        TableResultVO tableResultVO = new TableResultVO();
        tableResultVO.setCode(0);
        tableResultVO.setCount(imageList.size());
        tableResultVO.setMessage("");
        tableResultVO.setData(fileVOList);
        return tableResultVO;
    }
    /**
     * 功能描述 跳转图片文件页面
     * @param: []
     * @return: java.lang.String
     * @auther: lxl
     * @date: 2022/2/27 20:51
     */
    @GetMapping(value = "/page")
    public String imagePage(){
        return "imgs-list";
    }
}
