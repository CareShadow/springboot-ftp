package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.File;
import com.example.service.FileService;
import com.example.dao.FileMapper;
import org.springframework.stereotype.Service;

/**
* @author CareShadow
* @description 针对表【file】的数据库操作Service实现
* @createDate 2023-02-09 23:17:43
*/
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService{

}




