package com.example.Utils;

import com.example.utils.MinioUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * @ClassName MinioUtilsTest
 * @Description TODO
 * @Author CareShadow
 * @Date 2023/2/4 16:09
 * @Version 1.0
 **/
@SpringBootTest
@Slf4j
public class MinioUtilsTest {
    @Autowired
    private MinioUtils minioUtils;

    @Test
    void testGetById() throws Exception {
        minioUtils.getPreViewUrl("file", "001-第一讲-BeanFactory与ApplicationContext_1.mp4");
    }

    @Test
    void testUploadFile() throws Exception {
        for (int i = 0; i < 5; i++) {
            minioUtils.putObject(new ByteArrayInputStream(new byte[]{}, 0, -1), "5f4dcc3b5aa765d61d8327deb882cf99/chunks/"+i);
        }
        minioUtils.getUploaderChunk("5f4dcc3b5aa765d61d8327deb882cf99");
    }

    @Test
    void testJudgeFileMD5() throws Exception {
        minioUtils.JudgeFileMD5("001-第一讲-BeanFactory与ApplicationContext_1.mp4");
    }

    @Test
    void  testGetUploaderChunk() throws Exception {
        List<Integer> uploaderChunk = minioUtils.getUploaderChunk("5f4dcc3b5aa765d61d8327deb882cf99");
        for (Integer integer : uploaderChunk) {
            System.out.println(integer);
        }
    }
}
