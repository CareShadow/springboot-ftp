package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/file")
    public void uploadSliceFile(@RequestParam("chunk") MultipartFile file, Integer index) throws InterruptedException {
        log.debug("文件大小为 {} {}", file.getSize(), index);
        Thread.sleep(2000);
    }
}
