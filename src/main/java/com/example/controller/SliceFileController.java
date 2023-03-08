package com.example.controller;

import com.example.constants.HttpStatusEnum;
import com.example.pojo.Result;
import com.example.pojo.SplitChunkInfoVO;
import com.example.service.SliceFileService;
import com.example.utils.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SliceFileController
 * @Description TODO
 * @Author CareShadow
 * @Date 2023/2/5 21:47
 * @Version 1.0
 **/
@RestController
@RequestMapping("/slice")
@Slf4j
public class SliceFileController {

    @Resource
    private SliceFileService sliceFileService;

    /**
     * @Description 获取分片, 拿到MD5值
     * @Param [chunk]
     * @Return java.lang.String
     * @Date 2023/3/8 20:51
     * @Author CareShadow
     * @Version 1.0
     **/
    @GetMapping("/chunk")
    public Result<Map> uploadChunk(SplitChunkInfoVO chunk) {
        // 在Minio客户端MD5就是文件名
        String md5 = chunk.getIdentifier();
        boolean isExits = sliceFileService.checkFile(md5);
        Map<String, Object> data = new HashMap<>();
        if (isExits) {
            data.put("skipUpload", true);
            return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, data);
        }else {
            // 查找分片

        }
        return null;
    }
}
