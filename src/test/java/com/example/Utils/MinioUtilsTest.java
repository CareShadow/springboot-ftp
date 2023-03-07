package com.example.Utils;

import com.example.utils.MinioUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;

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
        minioUtils.putObject(new ByteArrayInputStream(new byte[]{}, 0, -1), "path/to/", "");
    }

    @Test
    void testJudgeFileMD5() throws Exception {
        minioUtils.JudgeFileMD5("001-第一讲-BeanFactory与ApplicationContext_1.mp4");
    }
}
