package com.example.Utils;

import com.example.dao.FileFolderMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ClassName BigFileTest
 * @Description TODO
 * @Author CareShadow
 * @Date 2023/2/5 11:10
 * @Version 1.0
 **/
@SpringBootTest
@Slf4j
public class BigFileTest {
    @Autowired
    private FileFolderMapper fileFolderMapper;

    @Test
    void testMysqlFunction() {
        String folderPath = fileFolderMapper.getFolderPath(3);
        log.info("folder_path: {}", folderPath);
    }
}
