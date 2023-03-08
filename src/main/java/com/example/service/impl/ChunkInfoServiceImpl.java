package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.ChunkInfo;
import com.example.service.ChunkInfoService;
import com.example.dao.ChunkInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author CareShadow
* @description 针对表【t_chunk_info】的数据库操作Service实现
* @createDate 2023-03-08 20:50:09
*/
@Service
public class ChunkInfoServiceImpl extends ServiceImpl<ChunkInfoMapper, ChunkInfo>
    implements ChunkInfoService{

}




