package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.FileInfo;
import com.example.service.FileInfoService;
import com.example.dao.FileInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author CareShadow
* @description 针对表【t_file_info】的数据库操作Service实现
* @createDate 2023-03-08 20:50:09
*/
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo>
    implements FileInfoService{

}




