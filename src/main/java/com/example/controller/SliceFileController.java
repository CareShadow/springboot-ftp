package com.example.controller;

import com.example.constants.HttpStatusEnum;
import com.example.pojo.Result;
import com.example.pojo.SplitChunkInfoVO;
import com.example.service.FileFolderService;
import com.example.service.SliceFileService;
import com.example.utils.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private FileFolderService fileFolderService;

    /**
     * @Description 获取分片, 拿到MD5值
     * @Param [chunk]
     * @Return java.lang.String
     * @Date 2023/3/8 20:51
     * @Author CareShadow
     * @Version 1.0
     **/

    //   /slice/chunk
    @GetMapping("/chunk")
    public Result<Map> checkChunk(SplitChunkInfoVO chunk) {
        // 在Minio客户端MD5就是文件名
        String md5 = chunk.getIdentifier();
        String filename = chunk.getFilename();
        String folderPath = fileFolderService.getFolderPath(chunk.getFolderId());
        boolean isExits = sliceFileService.checkFile(md5, filename, folderPath);
        Map<String, Object> data = new HashMap<>();
        if (isExits) {
            data.put("skipUpload", true);
        } else {
            // 查找分片
            List<Integer> chunkList = sliceFileService.checkChunk(md5);
            data.put("uploaded", chunkList);
        }
        return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, data);
    }

    /**
     * @Description 上传分片文件
     * @Param [chunk]
     * @Return com.example.pojo.Result
     * @Date 2023/3/9 21:03
     * @Author CareShadow
     * @Version 1.0
     **/
    @PostMapping("/chunk")
    public Result uploadChunk(SplitChunkInfoVO chunk) throws IOException {
        MultipartFile file = chunk.getFile();
        InputStream in = file.getInputStream();
        int index = chunk.getChunkNumber();
        String md5 = chunk.getIdentifier();
        sliceFileService.uploadChunk(md5, in, index);
        return ResultGenerator.getResultByHttp(HttpStatusEnum.OK);
    }

    /**
     * @Description TODO
     * @Param [identifier, totalChunks, contentType, name]
     * @Return com.example.pojo.Result
     * @Date 2023/3/11 10:19
     * @Author CareShadow
     * @Version 1.0
     **/
    @PostMapping("/merge")
    public Result mergeFile(String identifier, String totalChunks, String contentType,
                            String name, Integer folderId, Double size) throws Exception {
        String folderPath = fileFolderService.getFolderPath(folderId);
        boolean isExits = sliceFileService.checkFile(identifier, name, folderPath);
        if (isExits) {
            return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, "上传成功");
        }
        log.info("标识:{}, content-Type:{}", identifier, contentType);
        sliceFileService.mergeFile(identifier, Integer.valueOf(totalChunks), contentType, name, folderId, folderPath, size);
        log.debug("合并成功  md5:{}", identifier);
        return ResultGenerator.getResultByHttp(HttpStatusEnum.OK);
    }

}
