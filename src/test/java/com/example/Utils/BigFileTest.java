package com.example.Utils;

import com.example.dao.FileFolderMapper;
import com.example.dao.UserMapper;
import com.example.pojo.ResourceVO;
import com.example.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

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
    @Resource
    private FileFolderMapper fileFolderMapper;
    @Resource
    private UserMapper userMapper;

    @Test
    void testMysqlFunction() {
        String folderPath = fileFolderMapper.getFolderPath(3);
        log.info("folder_path: {}", folderPath);
    }

    @Test
    void generateJWT() {
        String token = JwtUtil.generate("admin");
        log.info(token);
    }

    @Test
    void testGetRoleAndResource() {
        List<ResourceVO> roleMapResource = userMapper.getRoleMapResource();
        log.info("debug....");
    }
}
