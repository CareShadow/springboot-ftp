package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("chunk")
    public String uploadChunk()
}
